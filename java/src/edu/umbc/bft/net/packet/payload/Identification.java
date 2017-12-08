package edu.umbc.bft.net.packet.payload;

import edu.umbc.bft.net.packet.impl.PayloadWithKey;

public class Identification extends PayloadWithKey	{

	private String name;
	
	public Identification(String name)	{
		this.name = name;
	}//End of constructor
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public byte[] toByteArray()		{
		return this.toString().getBytes();		
	}//End of method
	
	
	@Override
	public String toString()	{
		return "[ "+ this.name +","+ super.key.toString() +"]";
	}
	
}
