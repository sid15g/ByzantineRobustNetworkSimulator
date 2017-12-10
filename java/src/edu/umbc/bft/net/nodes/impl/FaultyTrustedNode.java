package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.ByzantineNode;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class FaultyTrustedNode extends TrustedNode implements ByzantineNode	{
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl) {
		super(pkl);
	}//End of Constructor
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl, int id) {
		this(pkl);
		this.setName("FTN"+ id);
	}//End of Constructor
	
	@Override
	protected final void execute(Interface i, Packet p) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		this.inject(p);
	};
	
	@Override
	public void inject(Packet p) {
		// TODO Auto-generated method stub	
	}//End Of Method
	
}
