package edu.umbc.bft.net.nodes.impl;

import java.util.Set;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.ByzantineNode;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;

public class FaultyTrustedNode extends TrustedNode implements ByzantineNode	{
	
	public FaultyTrustedNode(Set<RSAPub> pkl) {
		super(pkl);
	}//End of Constructor
	
	public FaultyTrustedNode(Set<RSAPub> pkl, int id) {
		this(pkl);
		this.setName("FTN"+ id);
	}//End of Constructor
	
	@Override
	protected final void execute(Interface i, Packet p) {
		this.inject();
	};
	
	@Override
	public void inject() {
		// TODO Auto-generated method stub	
	}//End Of Method
	
}
