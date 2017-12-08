package edu.umbc.bft.net.bean;

import java.net.InetAddress;
import java.util.Comparator;

public final class IPAddress implements Comparator<byte[]>, Comparable<byte[]> {
	
	private final byte[] address;
	private final String ip;
	
	public IPAddress(InetAddress ip) {
		this(ip.getAddress());
	}//end of constructor
	
	public IPAddress(byte[] ip) {
		this.address = ip;
		this.ip = this.convert();
	}//end of constructor
	
	public static final synchronized IPAddress createIP(byte[] ip) {
		byte[] address = new byte[ip.length];
		System.arraycopy(ip, 0, address, 0, ip.length);
		return new IPAddress(address);
	}//end of method
	
	
	public final byte[] getBytes() {
		return this.address;
	}
	public final int length() {
		return this.address.length;
	}
	
	@Override
	public final String toString() {
		return this.ip;
	}
	
	private String convert()	{
		StringBuilder buffer = new StringBuilder("");

		for (int i=0; i<this.address.length; i++) {
			buffer.append(this.address[i]).append('.');
		}
		
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
	}//End of method	
	
	
	@Override
	public boolean equals(Object obj)		{
		if( obj instanceof IPAddress )	{
			IPAddress ip = (IPAddress)obj;
			return ip.toString().equals(this.ip);
		}else if( obj instanceof InetAddress )	{
			IPAddress ip = new IPAddress(((InetAddress)obj));
			return ip.toString().equals(this.ip);
		}else
			return false;
	}//End of method

	
	@Override
	public int hashCode() {
		return this.ip.hashCode();
	}
	
	@Override
	public int compareTo(byte[] o) {
		return this.compare(this.address, o);
	}
	
	@Override
	public int compare(byte[] ip1, byte[] ip2) {

		if(ip1==ip2)
			return 0;
		else if( ip1!=null && ip2!=null )		{
				
			for(int i=0; i<ip1.length; i++) {
				if( ip1[i] != ip2[i] )
					return ip1[i]-ip2[i];
			}//End of loop
			
			return 0;
			
		}else
			return -1;
		
	}//End of method
	
}
