package edu.umbc.bft.net.nodes;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.Link;
import edu.umbc.bft.secure.RSAPub;

public interface Node extends Runnable {
	
	public RSAPub getPublicKey();
	public long getSequenceNo();
	public String getName();
	public void shutdown();
	
	public void addPhysicalPort(Interface i);
	public boolean attach(Interface i, Link l);
	public boolean hasPhysicalPort(Interface i);
	
	public default boolean isFaulty()	{
		return (this instanceof ByzantineNode);
	};
	
}