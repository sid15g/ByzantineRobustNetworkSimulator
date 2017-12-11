package edu.umbc.bft.net.bean;

import edu.umbc.bft.factory.PacketFactory;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;
import edu.umbc.bft.util.Timeout;

public class IdentificationTimeout extends Timeout	{

	private PacketFactory factory;
	private InterfaceManager manager;
	
	public IdentificationTimeout(PacketFactory factory, InterfaceManager manager )	{
		super(1000);
		this.manager = manager;
		this.factory = factory;
	}//end of constructor
	
	public IdentificationTimeout(InterfaceManager manager )	{
		super(1000);
		this.factory = null;
		this.manager = manager;
	}//end of constructor
	
	public void setFactory(PacketFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public final void onTimeout() {
		Logger.sysLog(LogValues.info, this.getClass().getName(), " ["+ this.factory.getSourceNodeId() +"] Timer Ended... " );
		Packet p = this.factory.createLinkStatePacket();
		this.manager.flood(p);
	}

}
