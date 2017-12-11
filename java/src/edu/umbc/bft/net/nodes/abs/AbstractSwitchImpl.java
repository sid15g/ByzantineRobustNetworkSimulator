package edu.umbc.bft.net.nodes.abs;

import java.util.Arrays;
import java.util.Timer;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.bean.IdentificationTimeout;
import edu.umbc.bft.net.bean.PKLComparator;
import edu.umbc.bft.net.bean.SwitchManager;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public abstract class AbstractSwitchImpl extends AbstractNodeImpl implements Switch {
	
	//TODO Route information from every node to every node
	
	protected PKLComparator<String, RSAPub> comparator;
	//private ForwardingTable table;
	protected SwitchManager manager;
	protected Timer timerManager;
	
	public AbstractSwitchImpl() {
		super();
		this.timerManager = new Timer();
		//this.table = new ForwardingTable(this.bufferSize);
		this.comparator = new PKLComparator<String, RSAPub>(super.getName());
		this.manager = new SwitchManager(super.getName(), super.getPrivateKey());
	}//End Of Constructor
	
	
	protected void setName(String name) {
		super.setName(name);
		this.manager.setName(name);
	}
	public final int getBufferSize() {
		return this.manager.getBufferSize();
	}
	
	@Override
	public void addRoute(IPAddress src, IPAddress dest, Interface i) {
		//TODO add when required
	}
	@Override
	public void deleteRoute(IPAddress src, IPAddress dest, Interface i)	{
		//TODO add when required
	}
	@Override
	public void forward(Interface src, Packet p)	{
		//TODO add when required
	}
	
	@Override
	public void ipDump() {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" | "+ Arrays.toString(super.getIPs()) );
	}
	@Override
	public long getSequenceNo() {
		return this.manager.getSequenceNo();
	}
	protected long getNextSequenceNo() {
		return this.manager.getNextSequenceNo();
	}
	
	
	protected boolean startIdentificationTimer()	{
		try	{
			IdentificationTimeout itimer = (IdentificationTimeout)super.createTimer(IdentificationTimeout.class);
			
			if( itimer != null )	{
				itimer.setFactory(this.manager);
				this.timerManager.schedule(itimer, itimer.getTimeoutInMillis());
				return true;
			}else	{
				Logger.sysLog(LogValues.error, this.getClass().getName(), this.subLog() +" Timer instantiation failed " );
				return false;
			}
			
		}catch(Exception e)	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.subLog() +" Timer creation failed \n"+ Logger.getStack(e) );
			return false;
		}		
	}//end of method
	
	
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
