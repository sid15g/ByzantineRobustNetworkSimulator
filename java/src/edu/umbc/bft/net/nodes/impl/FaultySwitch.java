package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.bzt.ByzantineSwitch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class FaultySwitch extends GeneralSwitch	implements ByzantineSwitch	{

	public FaultySwitch(Map<String, RSAPub> tkl) {
		super(tkl);
	}//End of Constructor
	
	public FaultySwitch(Map<String, RSAPub> tkl, int id)	{
		this(tkl);
		this.setName("FS"+ id);
	}//End of Constructor
	
	
	@Override
	protected final void execute(Interface i, Packet p) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		super.execute(i, p);
		//this.inject(p);
	}
	
	@Override
	public void inject(Packet p) {
		// TODO Auto-generated method stub	
	}//End Of Method
	
	@Override
	public void forward(Interface src, Packet p) {
		// TODO Add faulty behavior
	}
	
	//TODO override init() by random ID packet
	
}
