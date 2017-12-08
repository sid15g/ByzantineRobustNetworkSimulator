package edu.umbc.bft.net.packet.impl;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.packet.Header;

public class DefaultHeader implements Header	{

	private IPAddress source, destination;
	private int sequenceNo;
	private long timestamp;
	
	public DefaultHeader(IPAddress src, IPAddress dest) {
		this.source = src;
		this.destination = dest;
		this.timestamp = System.currentTimeMillis();
	}//End of constructor
	
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}
	@Override
	public String getSource() {
		return this.source.toString();
	}
	@Override
	public String getDestination() {
		return this.destination.toString();
	}
	@Override
	public int getSequenceNumber() {
		return this.sequenceNo;
	}
	
}
