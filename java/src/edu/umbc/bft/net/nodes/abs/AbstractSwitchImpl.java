package edu.umbc.bft.net.nodes.abs;

import java.util.Arrays;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.bean.PKLComparator;
import edu.umbc.bft.net.bean.SwitchManager;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public abstract class AbstractSwitchImpl extends AbstractNodeImpl implements Switch {
	
	//TODO 
	/**
	 *  1. Buffer for every node ( last sequence no. and the packet )
	 *  2. Route information from every node to every node
	 **/
	
	protected PKLComparator<String, RSAPub> comparator;
	//private ForwardingTable table;
	protected SwitchManager manager;
	
	public AbstractSwitchImpl() {
		super();
		this.manager = new SwitchManager(super.getName(), super.getPrivateKey());
		this.comparator = new PKLComparator<String, RSAPub>();
		//this.table = new ForwardingTable(this.bufferSize);
	}//End Of Constructor
	
	protected void setName(String name) {
		super.setName(name);
		this.manager.setName(name);
	}
	public final int getBufferSize() {
		return this.manager.getBufferSize();
	}
	
	@Override
	public final void ipDump() {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" | "+ Arrays.toString(super.getIPs()) );
	}
	@Override
	public void addRoute(IPAddress src, IPAddress dest, Interface i) {
		//TODO
	}
	@Override
	public void deleteRoute(IPAddress src, IPAddress dest, Interface i)	{
		//TODO
	}
	@Override
	public void forward(Interface src, Packet p)	{
		//TODO
	}
	
	@Override
	public long getSequenceNo() {
		return this.manager.getSequenceNo();
	}
	
	protected long getNextSequenceNo() {
		return this.manager.getNextSequenceNo();
	}
	
	@Override
	public void run() {
		
		/* Please do not remove sleep, it is to reduce network synchronization errors */
		try {
			Thread.currentThread();
			Thread.sleep(1000);			
		}catch(InterruptedException e) {}
		
		Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Switch started ");
		this.init();
		super.run();
		
		
	}//End Of Method
	
	
	@Override
	protected abstract void execute(Interface i, Packet p);
		
}
