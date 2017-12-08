package edu.umbc.bft.factory;

import java.util.Random;

import edu.umbc.bft.exception.IPNotAvailableException;
import edu.umbc.bft.net.bean.IPAddress;

/** Not recommended to use many instances of IPFactory */
public final class IPFactory {

	private static Random gen;
	
	private final byte subnet;
	private byte[] ip;
	
	static{
		IPFactory.gen = new Random();
	}//end of static block
	
	public IPFactory()	{
		byte temp = (byte)gen.nextInt(256);
		this.subnet = (byte)(temp>0?temp:(temp*-1));
		this.ip = new byte[]{10, this.subnet, 0, 0};
	}//end of constructor
	
	public IPAddress createIP()	throws IPNotAvailableException {
		this.increase();
		return IPAddress.createIP(this.ip);
	}//end of method
	
	public final byte getSubnet() {
		return this.subnet;
	}
	
	private void increase() throws IPNotAvailableException	{
		if( this.ip[3] < 255 )	{
			this.ip[3]++;
		}else if( this.ip[2] < 255 )	{
			this.ip[2]++;
		}else
			throw IPNotAvailableException.exhausted();
	}//end of method
	
	
}
