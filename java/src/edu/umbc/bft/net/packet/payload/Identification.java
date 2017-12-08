package edu.umbc.bft.net.packet.payload;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.packet.impl.PayloadWithKey;

public class Identification extends PayloadWithKey	{

	private String name;
	private IPAddress id;
	
	public Identification(String name, byte[] ip)	{
		this( name, new IPAddress(ip) );
	}//End of constructor
	
	public Identification(String name, IPAddress ip) {
		this.name = name;
		this.id = ip;
	}//End of constructor
	
	public String getName() {
		return this.name;
	}
	public String getId() {
		return this.id.toString();
	}
	
	@Override
	public byte[] toByteArray()		{
		return this.toString().getBytes();		
	}//End of method
	
	
	@Override
	public String toString() {
		return "[ ("+ this.name +","+ this.id.toString() +"), "+ super.key.toString() +"]";
	}
	
}
