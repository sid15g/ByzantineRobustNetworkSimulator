package edu.umbc.bft.net.bean;

import java.net.InetAddress;

public class NeighborDetail implements Comparable<IPAddress> {
	
	private String name;
	private IPAddress ip;
	private float cost;
	
	public NeighborDetail(String name, InetAddress neighbour, double cost) {
		this.name = name;
		this.ip = new IPAddress(neighbour);
		this.cost = Double.valueOf(cost).floatValue();
	}//End of constructor
	
	
	public String getName() {
		return this.name;
	}
	public byte[] getId() {
		return this.ip.getBytes();
	}
	public String getIPv4() {
		return this.ip.toString();
	}
	public float getCost() {
		return this.cost;
	}
	
	@Override
	public int compareTo(IPAddress o) {
		if( o!=null && o.equals(this.ip))
			return 0;
		else	{
			return this.ip.compareTo(o.getBytes());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof NeighborDetail )	{
			NeighborDetail n = (NeighborDetail)obj;
			return n.getIPv4().equals(this.getIPv4());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "["+ this.name +","+ this.ip.toString() + ","+ String.valueOf(this.cost) +"]";
	}

}
