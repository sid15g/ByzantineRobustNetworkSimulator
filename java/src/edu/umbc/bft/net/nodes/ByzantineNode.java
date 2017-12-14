package edu.umbc.bft.net.nodes;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.packet.Packet;

public interface ByzantineNode {
	
	public void inject(Interface i, Packet p);
	
}