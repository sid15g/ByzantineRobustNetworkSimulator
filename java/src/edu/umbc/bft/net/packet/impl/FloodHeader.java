package edu.umbc.bft.net.packet.impl;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.packet.Header;

public class FloodHeader implements Header {

	private String source;
	private long sequenceNo;
	private long timestamp;
	
	public FloodHeader(IPAddress src) {
		this();
		this.source = src.toString();
	}//End of constructor
	
	public FloodHeader(String srcID) {
		this();
		this.source = srcID;
	}//End of constructor
	
	private FloodHeader() {
		this.timestamp = System.currentTimeMillis();
	}//End of constructor
	
	public void setSequenceNo(long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public void setFakeSource(String source) {
		this.source = source;
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
		return null;
	}
	
	@Override
	public long getSequenceNumber() {
		return this.sequenceNo;
	}
	
}
