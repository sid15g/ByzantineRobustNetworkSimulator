package edu.umbc.bft.net.nodes.abs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.umbc.bft.factory.NameGenerator;
import edu.umbc.bft.net.bean.InterfaceManager;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.Link;
import edu.umbc.bft.net.conn.LinkHandler;
import edu.umbc.bft.net.nodes.Node;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.secure.KeyStore;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;
import edu.umbc.bft.util.Timeout;

public abstract class AbstractNodeImpl implements Node {
	
	private String name;
	private RSAPriv privateKey;
	private Set<Interface> ports;
	private InterfaceManager iManager;
	private List<LinkHandler> handlers;
	private volatile boolean running, stopped;
	
	public AbstractNodeImpl()	{
		this.stopped = this.running = false;
		this.name = NameGenerator.generate();
		this.ports = new HashSet<Interface>();
		this.privateKey = KeyStore.getNewKey();
		this.handlers = new ArrayList<LinkHandler>();
		this.iManager = new InterfaceManager(this, this.handlers);
	}//End Of Constructor
	
	protected final String subLog()	{
		return "["+ this.name +"]  ";
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	
	@Override
	public RSAPub getPublicKey() {
		return this.privateKey.getPublicKey();
	}
	protected RSAPriv getPrivateKey()	{
		return this.privateKey;
	}
	
	Timeout createTimer(Class<? extends Timeout> type) throws Exception	{
		return type.getConstructor(new Class[]{InterfaceManager.class}).newInstance(new Object[]{this.iManager});
	}
	
	@Override
	public final void shutdown() {
		this.running = false;
		this.stopped = true;
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Node closed ");
	}
	@Override
	public void addPhysicalPort(Interface i) {
		if( !this.running && !this.stopped )
			this.ports.add(i);
		else
			Logger.sysLog(LogValues.warn, this.getClass().getName(), this.subLog() +" Cannot add PhysicalPort while the network is running... ");
	}
	@Override
	public boolean hasPhysicalPort(Interface i) {
		return this.ports.contains(i);
	}
	protected String[] getIPs(){
		String []ips = new String[this.ports.size()];
		Object []arr = this.ports.toArray();
		for( int i=0; i<this.ports.size(); i++ )
			ips[i] = ((Interface)arr[i]).getIPv4();
		return ips;
	}
	
	@Override
	public final boolean attach(Interface i, Link l)	{
		if( this.ports.contains(i) )	{
			i.attachLink(l);
			LinkHandler handler = l.getHandler(this);
			return this.handlers.add(handler);
		}else	{
			Logger.sysLog(LogValues.warn, this.getClass().getName(), this.subLog() +" Cannot attach the Interface to the Link ");
			return false;
		}
	}
	
	@Override
	public void run() {
		
		int i = 0;
		this.running = true;
		final int size = this.handlers.size();
		
		while( this.running )	{
			
			LinkHandler handler = this.handlers.get(i);
			Packet p = handler.read();
			i = (i+1)%size;
			
			if( p!=null )
				this.execute(handler.getSource(), p);
			
		}//End of loop
		
	}//End Of Method
	
	
	protected final String sign(Payload p) {
		return this.privateKey.sign(p.toString());
	}
	
	
	protected final void flood(Packet p) {
		this.iManager.flood(p);
	}
	protected final void flood(Interface src, Packet p) {
		this.iManager.flood(src, p);
	}
	protected final boolean replyFrom(Interface src, Packet p)	{
		return this.iManager.replyFrom(src, p);
	}
	protected final boolean sendTo(Packet p, Interface dest) {
		return this.iManager.sendTo(p, dest);
	}
	

	protected abstract void execute(Interface i, Packet p);
	
}
