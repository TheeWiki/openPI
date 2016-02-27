package server.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;

import server.model.players.Player;

public class ChannelHandler extends SimpleChannelHandler {
	
	private Session session = null;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
	if (e.getCause() instanceof ReadTimeoutException) {
			if (session.getPlayer() != null) {
				System.out.println("Player " + session.getPlayer().playerName + " timed out!");
			}
		} else if(!(e.getCause() instanceof java.io.IOException)){
			e.getCause().printStackTrace();
		}
		ctx.getChannel().close();
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof Player) {
			session.setPlayer((Player) e.getMessage());
		} else if (e.getMessage() instanceof Packet) {
			if (session.getPlayer() != null) {
				session.getPlayer().queueMessage((Packet) e.getMessage());
			}
		}
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (session == null)
			session = new Session(ctx.getChannel());
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (session != null) {
			Player Player = session.getPlayer();
			if (Player != null) {
				Player.disconnected = true;
			}
			session = null;
		}
	}

}
