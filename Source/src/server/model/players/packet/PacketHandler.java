package server.model.players.packet;

import server.model.players.Client;
import server.model.players.packet.impl.AttackPlayer;
import server.model.players.packet.impl.Bank10;
import server.model.players.packet.impl.Bank5;
import server.model.players.packet.impl.BankAll;
import server.model.players.packet.impl.BankX1;
import server.model.players.packet.impl.BankX2;
import server.model.players.packet.impl.ChallengePlayer;
import server.model.players.packet.impl.ChangeAppearance;
import server.model.players.packet.impl.ChangeRegions;
import server.model.players.packet.impl.Chat;
import server.model.players.packet.impl.ClickItem;
import server.model.players.packet.impl.ClickNPC;
import server.model.players.packet.impl.ClickObject;
import server.model.players.packet.impl.ClickingButtons;
import server.model.players.packet.impl.ClickingInGame;
import server.model.players.packet.impl.ClickingStuff;
import server.model.players.packet.impl.Commands;
import server.model.players.packet.impl.Dialogue;
import server.model.players.packet.impl.DropItem;
import server.model.players.packet.impl.FollowPlayer;
import server.model.players.packet.impl.IdleLogout;
import server.model.players.packet.impl.ItemClick2;
import server.model.players.packet.impl.ItemClick2OnGroundItem;
import server.model.players.packet.impl.ItemClick3;
import server.model.players.packet.impl.ItemOnGroundItem;
import server.model.players.packet.impl.ItemOnItem;
import server.model.players.packet.impl.ItemOnNpc;
import server.model.players.packet.impl.ItemOnObject;
import server.model.players.packet.impl.MagicOnFloorItems;
import server.model.players.packet.impl.MagicOnItems;
import server.model.players.packet.impl.MoveItems;
import server.model.players.packet.impl.MusicPacket;
import server.model.players.packet.impl.PickupItem;
import server.model.players.packet.impl.PrivateMessaging;
import server.model.players.packet.impl.RemoveItem;
import server.model.players.packet.impl.Report;
import server.model.players.packet.impl.SilentPacket;
import server.model.players.packet.impl.Trade;
import server.model.players.packet.impl.Walking;
import server.model.players.packet.impl.WearItem;


public class PacketHandler{

	private static PacketType packetId[] = new PacketType[256];
	
	static {
		
		SilentPacket u = new SilentPacket();
		packetId[86] = u; // camera movement packet
		packetId[3] = u; // idle packet (click away from client prints size 3, etc.
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[226] = u;
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[253] = new ItemClick2OnGroundItem();
		packetId[40] = new Dialogue();
		ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[218] = new Report();
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[16] = new ItemClick2();		
		packetId[75] = new ItemClick3();	
		packetId[122] = new ClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new Commands();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[139] = new Trade();
		packetId[39] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Bank5();
		packetId[43] = new Bank10();
		packetId[129] = new BankAll();
		packetId[101] = new ChangeAppearance();
		PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[59] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[135] = new BankX1();
		packetId[208] = new BankX2();
		Walking w = new Walking();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
//		packetId[60] = new ClanChat();
		packetId[74] = new MusicPacket();
	}


	public static void processPacket(Client c, int packetType, int packetSize) {	
		if(packetType == -1) {
			return;
		}
		PacketType p = packetId[packetType];
		if(p != null) {
			try {
				//System.out.println("packet: " + packetType);
				p.processPacket(c, packetType, packetSize);
			} catch(Exception e) {
					e.printStackTrace();
			}
		} else {
			System.out.println("Unhandled packet type: "+packetType+ " - size: "+packetSize);
		}
	}
	

}
