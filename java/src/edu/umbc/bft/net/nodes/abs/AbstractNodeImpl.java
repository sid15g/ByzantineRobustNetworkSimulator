package edu.umbc.bft.net.nodes.abs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.umbc.bft.factory.NameGenerator;
import edu.umbc.bft.net.bean.NodeManager;
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

public abstract class AbstractNodeImpl implements Node {
	
	private String name;
	private RSAPriv privateKey;
	protected NodeManager manager;
	private Set<Interface> ports;
	private long currentSequenceNo;
	private List<LinkHandler> handlers;
	private volatile boolean running, stopped;
	
	public AbstractNodeImpl()	{
		this.currentSequenceNo = 1L;
		this.manager = new NodeManager();
		this.stopped = this.running = false;
		this.name = NameGenerator.generate();
		this.ports = new HashSet<Interface>();
		this.privateKey = KeyStore.getNewKey();
		this.handlers = new ArrayList<LinkHandler>();
	}//End Of Constructor
	
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
	@Override
	public long getSequenceNo() {
		return this.currentSequenceNo;
	}
	
	protected final long getNextSequenceNo() {
		return ++this.currentSequenceNo;
	}
	
	@Override
	public final void shutdown() {
		this.running = false;
		this.stopped = true;
	}
	@Override
	public void addPhysicalPort(Interface i) {
		if( !this.running && !this.stopped )
			this.ports.add(i);
		else
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Cannot add PhysicalPort while the network is running... ");
	}
	@Override
	public boolean hasPhysicalPort(Interface i) {
		return this.ports.contains(i);
	}

	@Override
	public final boolean attach(Interface i, Link l)	{
		if( this.ports.contains(i) )	{
			i.attachLink(l);
			LinkHandler handler = l.getHandler(this);
			return this.handlers.add(handler);
		}else	{
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Cannot attach the Interface to the Link ");
			return false;
		}
	}
	
	@Override
	public final void run() {
		
		int i = 0;
		this.running = true;
		final int size = this.handlers.size();
		
		while( this.running )	{
			LinkHandler handler = this.handlers.get(i);
			Packet p = handler.read();
			this.execute(handler.getSource(), p);
			i = (i+1)%size;
		}//End of loop
		
	}//End Of Method
	
	
	protected final String sign(Payload p) {
		return this.privateKey.sign(p.toString());
	}
	
	protected final void flood(Interface src, Packet p) {
		
		final int size = this.handlers.size();
		
		for( int i=0; i<size; i++ )	{
			LinkHandler handler = this.handlers.get(i);

			if( handler.isSource(src) == false )	{
				handler.send(p);
			}
		}//End of loop
		
	}//End of Method
	
	
	protected final boolean replyFrom(Interface src, Packet p)	{
		
		LinkHandler handler = src.getLink().getHandler(this);
		
		if( handler != null )	{
			handler.send(p);
			return true;
		}else
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Link Handler not found to reply ");
		
		return false;
	}//End of Method
	
	
	protected final boolean sendTo(Packet p, Interface dest) {
		
		final int size = this.handlers.size();
		
		for( int i=0; i<size; i++ )		{
			LinkHandler handler = this.handlers.get(i);
			
			if( handler.isDestination(dest) )	{
				handler.send(p);
				return true;
			}
		}//End of loop
		
		return false;
	}//End of Method
	
	
	protected abstract void execute(Interface i, Packet p);
	
}
