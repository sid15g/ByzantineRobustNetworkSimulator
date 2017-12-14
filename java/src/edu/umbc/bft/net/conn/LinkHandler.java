package edu.umbc.bft.net.conn;

import edu.umbc.bft.net.packet.Packet;

public class LinkHandler	{
	
	private Interface src, dest;
	
	LinkHandler(Interface src, Interface dest) {
		this.src = src;
		this.dest = dest;
	}//End of constructor

	
	public Interface getSource() {
		return this.src;
	}
	public boolean isSource(Interface i) {
		return this.src.equals(i);
	}
	public boolean isDestination(Interface i) {
		return this.dest.equals(i);
	}
	public boolean isDestNodeID(String nodeId)	{
		return this.dest.getOwner().equals(nodeId);
	}
	
	public Packet read() {
		return this.src.getInputStream().poll();
	}//End Of Method
	
	public boolean send(Packet p) {
		return this.dest.getInputStream().add(p);
	}//End of Method
	
}
