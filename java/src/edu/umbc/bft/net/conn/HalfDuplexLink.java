package edu.umbc.bft.net.conn;

import edu.umbc.bft.net.nodes.Node;


public class HalfDuplexLink implements Link	{

	private Interface src, dest;
	
	private float dropRate;					/** [0,1] floating values; defines [0,100]% */
	private float bandwidth;				/** packets/second */
	
	public HalfDuplexLink(Interface src, Interface dest)	{
		this(src, dest, Link.DefaultDropRate);
	}//End of constructor

	public HalfDuplexLink(Interface src, Interface dest, double dropRate)	{
		this.src = src;
		this.dest = dest;
		this.bandwidth = 64.00f;
		this.dropRate = Double.valueOf(dropRate).floatValue();
	}//End of constructor
		
	
	@Override
	public Interface[] getInterfaces() {
		return new Interface[]{src, dest};
	}
	@Override
	public double getBandwidth() {
		return this.bandwidth;
	}
	@Override
	public double getDropRate() {
		return this.dropRate;
	}
	@Override
	public void setBandwidth(double bandwidth) {
		this.bandwidth = Double.valueOf(bandwidth).floatValue();
	}
	@Override
	public void setDropRate(double dropRate) {
		this.dropRate = Double.valueOf(dropRate).floatValue();
	}
	
	public boolean isSender(Interface i) {
		return this.src.equals(i);
	}
	public boolean isReceiver(Interface i) {
		return this.dest.equals(i);
	}
	
	private String getSrcIP() {
		return this.src.getIPv4();
	}
	private String getRecvIP() {
		return this.dest.getIPv4();
	}
	
	@Override
	public LinkHandler getHandler(Node n)	{
		if( n.hasPhysicalPort(src) && n.hasPhysicalPort(dest)==false )
			return new LinkHandler(src, dest);
		else
			return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof HalfDuplexLink )	{
			HalfDuplexLink dl = (HalfDuplexLink)obj;
			boolean ret = dl.getSrcIP().equals(this.getSrcIP());
			ret &= dl.getRecvIP().equals(this.getRecvIP());
			return ret;
		}else
			return false;
	}
	
	@Override
	public String toString() {
		return this.tostring();
	}
	
}
