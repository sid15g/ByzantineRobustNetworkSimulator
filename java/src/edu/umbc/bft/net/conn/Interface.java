package edu.umbc.bft.net.conn;


public interface Interface {
	
	public static enum Status	{
		DOWN,
		UP;
	}//End Of Enum
	
	public final int CAPACITY  = 16;
	
	public MessageStream getInputStream();
	public Interface.Status getStatus();
	public String getMACAddress();
	public String getIPv4();
	public Link getLink();
	
	public void attachLink(Link l);
	
	public default String tostring()	{
		return this.getIPv4();
	};
	
}