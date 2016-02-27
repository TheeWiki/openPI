package server.net;

import org.jboss.netty.channel.Channel;

import server.model.players.Player;

public class Session {
	
	private final Channel channel;
	private Player Player;
	
	public Session(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public Player getPlayer() {
		return Player;
	}

	public void setPlayer(Player Player) {
		this.Player = Player;
	}

}
