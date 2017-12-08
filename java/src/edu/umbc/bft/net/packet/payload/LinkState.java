package edu.umbc.bft.net.packet.payload;

import java.util.Arrays;
import java.util.List;

import edu.umbc.bft.net.bean.NeighborDetail;
import edu.umbc.bft.net.packet.Payload;

public class LinkState implements Payload {

	private List<NeighborDetail> neighborsList;
	
	public List<NeighborDetail> getNeighborsList() {
		return this.neighborsList;
	}
	
	@Override
	public byte[] toByteArray() {
		return this.toString().getBytes();
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.neighborsList.toArray()).trim();
	}
	
}
