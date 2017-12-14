package edu.umbc.bft.net.bean;

import edu.umbc.bft.net.conn.Link;
import edu.umbc.bft.secure.RSAPub;

public class NeighborDetail		{
	
	private IPAddress ip;
	private String name;
	private RSAPub key;
	private float cost;
	
	public NeighborDetail(String name, RSAPub key)		{
		this.cost = 64.000F * Link.DefaultDropRate;				/* default bandwidth * default drop rate */
		this.name = name;
		this.key = key;
	}//End of constructor
	
	public NeighborDetail(String name)		{
		this(name, null);
	}//End of constructor
	
	public NeighborDetail(NeighborDetail nd)		{
		this(nd.name, nd.key);
		
		if( nd.ip != null )
			this.setIPAddress(nd.ip);
		
	}//End of constructor
	
	
	public String getName() {
		return this.name;
	}
	public byte[] getId() {
		if( this.ip != null )
			return this.ip.getBytes();
		return null;
	}
	public String getIPv4() {
		if( this.ip != null )
			return this.ip.toString();
		else
			return null;
	}
	public RSAPub getKey() {
		return this.key;
	}
	public float getCost() {
		return this.cost;
	}
	public void setIPAddress(byte[] ip) {
		this.ip = new IPAddress(ip);
	}
	public void setIPAddress(IPAddress ip) {
		this.ip = ip;
	}
	public void setKey(RSAPub key) {
		this.key = key;
	}
	public void setCost(double cost) {
		this.cost = Double.valueOf(cost).floatValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof NeighborDetail )	{
			NeighborDetail n = (NeighborDetail)obj;
			return n.getName().equals(this.getName());
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		//return "["+ this.name +","+ this.cost +","+ this.key.toString() +"]";
		return "["+ this.name +","+ this.cost +"]";
	}

}
