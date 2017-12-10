package edu.umbc.bft.net.packet.payload;

import edu.umbc.bft.net.packet.impl.PayloadWithKey;
import edu.umbc.bft.secure.RSAPub;

public class Identification extends PayloadWithKey	{

	private String name;
	
	public Identification(String name)	{
		this.name = name;
	}//End of constructor
	
	public String getName() {
		return this.name;
	}
	
	/** verify source from PKL given by Trusted Node */
	public boolean verifySignature(RSAPub key) {
		return key.verify(this.signature, this.toString());
	}
	
	@Override
	public byte[] toByteArray()		{
		return this.toString().getBytes();		
	}//End of method
	
	
	@Override
	public String toString()	{
		//TODO handle larger data for encryption. current limit is string length of 128 characters
		return "[ "+ this.name +","+ super.key.toString() +"]";
	}
	
}
