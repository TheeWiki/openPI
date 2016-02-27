package server.net.login;

import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import server.Connection;
import server.Constants;
import server.Server;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.model.players.PlayerSave;
import server.net.PacketBuilder;
import server.util.ISAACCipher;
import server.util.Misc;
public class RS2LoginProtocol extends FrameDecoder {

	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2)
				return null;
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			
			if (buffer.readableBytes() < 2) {
				return null;
			}
			
			int loginType = buffer.readByte();
			if (loginType != 16 && loginType != 18) {
				System.out.println("Invalid login type: " + loginType);
				//channel.close();
				//return null;
			}
			//System.out.println("Login type = "+loginType);
			int blockLength = buffer.readByte() & 0xff;
			if (buffer.readableBytes() < blockLength) {
				return null;
			}
			
			buffer.readByte();
			
			@SuppressWarnings("unused")
			int clientVersion = buffer.readShort();
			/*if (clientVersion != 317) {
				System.out.println("Invalid client version: " + clientVersion);
				channel.close();
				return null;
			}*/
			
			buffer.readByte();
			
			for (int i = 0; i < 9; i++)
				buffer.readInt();
			
			
			buffer.readByte();
			
			int rsaOpcode = buffer.readByte();
			if (rsaOpcode != 10) {
				System.out.println("Unable to decode RSA block properly!");
				channel.close();
				return null;
			}
			
			final long clientHalf = buffer.readLong();
			final long serverHalf = buffer.readLong();
			final int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
			final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
			for (int seed = 0; seed < isaacSeed.length; seed++)
				isaacSeed[seed] += 50;
			final ISAACCipher outCipher = new ISAACCipher(isaacSeed);
			final int version = buffer.readInt();
			final String name = Misc.formatPlayerName(Misc.getRS2String(buffer));
			final String pass = Misc.getRS2String(buffer);
			channel.getPipeline().replace("decoder", "decoder", new RS2Decoder(inCipher));
			return login(channel, inCipher, outCipher, version, name, pass);
		}
		return null;
	}

	private static Client login(Channel channel, ISAACCipher inCipher, ISAACCipher outCipher, int version, String name, String pass) {
		int returnCode = 2;
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}
		if (name.length() > 12) {
			returnCode = 8;
		}
		Client requester = new Client(channel, -1);
		requester.playerName = name;
		requester.playerName2 = requester.playerName;
		requester.playerPass = pass;
		requester.outStream.packetEncryption = outCipher;
		requester.saveCharacter = false;
		requester.isActive = true;
		
		if (Connection.isNamedBanned(requester.playerName)) {
			System.out.println(requester.playerName + " is a banned account!");
			returnCode = 4;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			System.out.println(name + " is online already");
			returnCode = 5;
		}
		if (PlayerHandler.getPlayerCount() >= Constants.MAX_PLAYERS) {
			System.out.println("Player count exceeds accepted limit!");
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			System.out.println("Server updating (?)");
			returnCode = 14;
		}
		if (returnCode == 2) {
			int load = PlayerSave.loadGame(requester, requester.playerName, requester.playerPass);
			if (load == 0)
				requester.addStarter = true;
			if (load == 3) {
				returnCode = 3;
				requester.saveFile = false;
			} else {
				for (int i = 0; i < requester.playerEquipment.length; i++) {
					if (requester.playerEquipment[i] == 0) {
						requester.playerEquipment[i] = -1;
						requester.playerEquipmentN[i] = 0;
					}
				}
				if (!Server.playerHandler.newPlayerClient(requester)) {
					returnCode = 7;
					requester.saveFile = false;
				} else {
					requester.saveFile = true;
				}
			}
		}
		if(returnCode == 2) {
			requester.saveCharacter = true;
			requester.packetType = -1;
			requester.packetSize = 0;
			final PacketBuilder builder = new PacketBuilder();
			builder.put((byte) 2);
			if (requester.playerRights == 3) {
				builder.put((byte) 2);
			} else {
				builder.put((byte) requester.playerRights);
			}
			builder.put((byte) 0);
			channel.write(builder.toPacket());
		} else {
			System.out.println("returncode: " + returnCode);
			sendReturnCode(channel, returnCode);
			return null;
		}
//		synchronized (PlayerHandler.lock) {
			requester.initialize();
			requester.initialized = true;
//		}
		return requester;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}
}