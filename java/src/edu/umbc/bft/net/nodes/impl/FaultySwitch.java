package edu.umbc.bft.net.nodes.impl;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.bzt.ByzantineSwitch;
import edu.umbc.bft.net.packet.Packet;

public class FaultySwitch extends GeneralSwitch	implements ByzantineSwitch	{

	public FaultySwitch() {
		super();
	}//End of Constructor
	
	public FaultySwitch(int id)	{
		this();
		this.setName("FS"+ id);
	}//End of Constructor
	
	
	@Override
	protected final void execute(Interface i, Packet p) {
		this.inject();
	}
	
	@Override
	public void inject() {
		// TODO Auto-generated method stub	
	}//End Of Method
	
	@Override
	public void forward(Interface src, Packet p) {
		// TODO Add faulty behavior
		super.forward(src, p);
	}
	
}
