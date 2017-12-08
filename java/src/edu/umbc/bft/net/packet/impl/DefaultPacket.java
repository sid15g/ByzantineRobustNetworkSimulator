package edu.umbc.bft.net.packet.impl;

import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;

public class DefaultPacket implements Packet	{
	
	private Header header;
	private Payload payload;
	
	public DefaultPacket(Header h, Payload p) {
		this.header = h;
		this.payload = p;
	}//End of constructor
	
	
	@Override
	public Header getHeader() {
		return this.header;
	}
	@Override
	public Payload getPayload() {
		return this.payload;
	}
	
}
