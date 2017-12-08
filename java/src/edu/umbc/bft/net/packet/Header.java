package edu.umbc.bft.net.packet;

public interface Header {

	public String getSource();
	public String getDestination();
	
	public long getTimestamp();
	public int getSequenceNumber();
	
}