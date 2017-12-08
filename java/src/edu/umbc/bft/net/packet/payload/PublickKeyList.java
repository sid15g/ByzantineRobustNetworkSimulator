package edu.umbc.bft.net.packet.payload;

import java.util.Arrays;
import java.util.List;

import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.secure.RSAPub;

public class PublickKeyList implements Payload {

	private List<RSAPub> keys;
	
	public List<RSAPub> getPublicKeyList() {
		return this.keys;
	}
	
	@Override
	public byte[] toByteArray() {
		return Arrays.toString(this.keys.toArray()).getBytes();
	}
	
	@Override
	public String toString() {
		return new String(this.toByteArray()).trim();
	}

}
