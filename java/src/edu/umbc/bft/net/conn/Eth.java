package edu.umbc.bft.net.conn;

import edu.umbc.bft.factory.NameGenerator;
import edu.umbc.bft.net.bean.IPAddress;

public class Eth implements Interface {

	private MessageStream inputBuffer;
	private Interface.Status status;
	private IPAddress ip;
	private String MAC;
	private Link link;
	
	public Eth(byte[] ip)	{
		this( new IPAddress(ip) );
	}//End of constructor
	
	public Eth(IPAddress ip)	{
		this.ip = ip;
		this.link = null;
		this.inputBuffer = null;
		this.status = Interface.Status.DOWN;
		this.MAC = NameGenerator.assignMACAddress();
	}//End of constructor

	
	@Override
	public String getMACAddress() {
		return this.MAC;
	}
	@Override
	public String getIPv4() {
		return this.ip.toString();
	}
	@Override
	public MessageStream getInputStream() {
		return this.inputBuffer;
	}
	@Override
	public Interface.Status getStatus() {
		return this.status;
	}
	
	@Override
	public Link getLink() {
		return this.link;
	}
	@Override
	public void attachLink(Link l)	{
		
		this.link = l;
		
		if( l instanceof HalfDuplexLink && ((HalfDuplexLink) l).isReceiver(this) )	{
			this.inputBuffer = null;
			this.status = Status.DOWN;
		}else	{
			this.status = Status.UP;
			this.inputBuffer = new MessageStream(Interface.CAPACITY, this.link.getDropRate());
		}
	}//End of method

	@Override
	public boolean equals(Object obj)	{
		if( obj instanceof Eth )	{
			Eth e = (Eth)obj;
			return e.getMACAddress().equals(this.MAC);
		}else
			return false;
	}
	
	@Override
	public String toString() {
		return this.tostring();
	}
	
}
