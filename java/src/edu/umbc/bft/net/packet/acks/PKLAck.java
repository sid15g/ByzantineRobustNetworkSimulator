package edu.umbc.bft.net.packet.acks;

import edu.umbc.bft.net.packet.Acknowledgement;

public class PKLAck implements Acknowledgement {

	private boolean status;
	
	public PKLAck(boolean responseStatus) {
		this.status = responseStatus;
	}
	
	@Override
	public boolean status() {
		return this.status;
	}
	
	@Override
	public byte[] toByteArray()	{
		return String.valueOf(this.status).getBytes();
	}

}
