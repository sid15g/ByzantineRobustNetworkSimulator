package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.ByzantineNode;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class FaultyTrustedNode extends TrustedNode implements ByzantineNode	{
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl) {
		super(pkl, tkl);
	}//End of Constructor
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl, int id) {
		this(pkl, tkl);
		this.setName("FTN"+ id);
	}//End of Constructor
	
	@Override
	protected final void execute(Interface i, Packet p) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		super.execute(i, p);
		//this.inject(p);
	};
	
	@Override
	public void inject(Packet p) {
		// TODO Auto-generated method stub	
	}//End Of Method
	
}
