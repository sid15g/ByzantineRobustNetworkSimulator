package edu.umbc.bft.net.nodes.abs;

import edu.umbc.bft.net.bean.ForwardingTable;
import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public abstract class AbstractSwitchImpl extends AbstractNodeImpl implements Switch {
	
	//TODO 
	/**
	 *  1. Buffer for every node ( last sequence no. and the packet )
	 *  2. Route information from every node to every node
	 **/
	
	private int bufferSize;
	private ForwardingTable table;
	
	public AbstractSwitchImpl() {
		super();
		this.bufferSize = 16;
		this.table = new ForwardingTable(this.bufferSize);
	}//End Of Constructor
	
	public final int getBufferSize() {
		return this.bufferSize;
	}
	
	@Override
	public final void addRoute(IPAddress src, IPAddress dest, Interface i) {
		this.table.addEntry(src, dest, i);
	}
	@Override
	public final void deleteRoute(IPAddress src, IPAddress dest, Interface i)	{
		this.deleteRoute(src, dest, i);
	}
	
	@Override
	public void forward(Interface src, Packet p)	{
		super.replyFrom(src, p);
	}
	
	@Override
	public void run() {
		
		/* Please do not remove sleep, it is to reduce network synchronization errors */
		try {
			Thread.currentThread();
			Thread.sleep(1000);			
		}catch(InterruptedException e) {}
		
		this.init();
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Node started ");
		super.run();
		
		
	}//End Of Method
	
	
	@Override
	protected abstract void execute(Interface i, Packet p);
		
}
