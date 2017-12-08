package edu.umbc.bft.net.nodes.impl;

import java.util.Set;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.abs.AbstractTrustedNodeImpl;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;

public class TrustedNode extends AbstractTrustedNodeImpl	{

	public TrustedNode(Set<RSAPub> pkl) {
		super(pkl);
	}//End of Constructor
	
	public TrustedNode(Set<RSAPub> pkl, int id)	{
		this(pkl);
		this.setName("TN"+ id);
	}//End of Constructor
	
	@Override
	protected void execute(Interface i, Packet p) {
		// TODO Auto-generated method stub
	}//End Of Method

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
	
}
