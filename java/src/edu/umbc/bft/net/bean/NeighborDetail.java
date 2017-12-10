package edu.umbc.bft.net.bean;

import edu.umbc.bft.secure.RSAPub;

public class NeighborDetail		{
	
	private IPAddress ip;
	private String name;
	private RSAPub key;
	
	public NeighborDetail(String name, RSAPub key)		{
		this.name = name;
		this.key = key;
	}//End of constructor
	
	public NeighborDetail(String name)		{
		this(name, null);
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
	public void setIPAddress(IPAddress ip) {
		this.ip = ip;
	}
	public void setKey(RSAPub key) {
		this.key = key;
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
		return "["+ this.name +","+ this.key.toString() +"]";
	}

}
