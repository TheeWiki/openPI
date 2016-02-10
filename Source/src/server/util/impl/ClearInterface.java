package server.util.impl;

import server.model.players.Client;

public interface ClearInterface {

	public int getInterfaceID();
	
	public void clearInterface(Client c);
	
	public void setNewText(Client c);
	
	public void appendNewInterface(Client c);
}