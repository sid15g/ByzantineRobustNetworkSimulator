package edu.umbc.bft.net.packet.payload;

import edu.umbc.bft.net.bean.DatagramRoute;
import edu.umbc.bft.net.packet.impl.PayloadWithKey;

public class Datagram extends PayloadWithKey	{

	private DatagramRoute route;
	private String data;
	
	public Datagram(String data) {
		this.data = data;
	}//end of constructor
	
	public String getData() {
		return this.data;
	}
	public DatagramRoute getRoute() {
		return this.route;
	}
	public void setRoute(DatagramRoute route) {
		this.route = route;
	}
	
	@Override
	public byte[] toByteArray() {
		return this.toString().getBytes();
	}
	
	@Override
	public String toString() {
		return "[ "+ this.data +","+ this.route.toString() +" ]";
	}

}