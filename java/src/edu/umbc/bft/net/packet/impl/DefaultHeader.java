package edu.umbc.bft.net.packet.impl;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.packet.Header;

public class DefaultHeader implements Header	{

	private String source, destination;
	private long sequenceNo;
	private long timestamp;
	
	public DefaultHeader(IPAddress src, IPAddress dest) {
		this();
		this.source = src.toString();
		this.destination = dest.toString();
	}//End of constructor

	public DefaultHeader(String srcID, String destID) {
		this();
		this.source = srcID;
		this.destination = destID;
	}//End of constructor
	
	private DefaultHeader() {
		this.timestamp = System.currentTimeMillis();
	}//End of constructor
	
	public void setSequenceNo(long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}
	@Override
	public String getSource() {
		return this.source;
	}
	@Override
	public String getDestination() {
		return this.destination;
	}
	@Override
	public long getSequenceNumber() {
		return this.sequenceNo;
	}
	
}
