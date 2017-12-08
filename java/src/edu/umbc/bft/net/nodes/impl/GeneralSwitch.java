package edu.umbc.bft.net.nodes.impl;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.abs.AbstractSwitchImpl;
import edu.umbc.bft.net.packet.Packet;

public class GeneralSwitch extends AbstractSwitchImpl	{

	public GeneralSwitch() {
		super();
	}//End of Constructor
	
	public GeneralSwitch(int id) {
		this();
		this.setName("S"+ id);
	}//End of Constructor
	
	@Override
	protected void execute(Interface i, Packet p) {
		
		super.manager.checkFromBuffer("s2", p);
		// TODO Auto-generated method stub
		/**
		 *  Identification packet to neighbors only
		 *  Faulty Node might flood
		 *  
		 *  Link states packets are sent to the flooded to all neighbors with
		 *  neighbor list and cost of links
		 *   
		 **/
	}//End Of Method
	
}
