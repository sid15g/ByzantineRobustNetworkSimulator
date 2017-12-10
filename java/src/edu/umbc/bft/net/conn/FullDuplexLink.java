package edu.umbc.bft.net.conn;

import edu.umbc.bft.net.nodes.Node;


public class FullDuplexLink implements Link	{
	
	private Interface inf1, inf2;

	private float dropRate;					/** [0,1] floating values; defines [0,100]% */
	private float bandwidth;				/** packets/second */
	
	public FullDuplexLink(Interface i1, Interface i2) {
		this(i1, i2, 0.03);						/** 3% */
	}//end of constructor
	
	public FullDuplexLink(Interface i1, Interface i2, double dropRate)	{
		this.inf1 = i1;
		this.inf2 = i2;
		this.bandwidth = 64.000f;
		this.dropRate = Double.valueOf(dropRate).floatValue();
	}//End of constructor

	
	@Override
	public Interface[] getInterfaces() {
		return new Interface[]{this.inf1, this.inf2};
	}
	@Override
	public double getDropRate() {
		return this.dropRate;
	}
	@Override
	public double getBandwidth() {
		return this.bandwidth;
	}
	@Override
	public void setBandwidth(double bandwidth) {
		this.bandwidth = Double.valueOf(bandwidth).floatValue();
	}
	@Override
	public void setDropRate(double dropRate) {
		this.dropRate = Double.valueOf(dropRate).floatValue();
	}
	
	@Override
	public LinkHandler getHandler(Node n)	{
		if( n.hasPhysicalPort(inf1) && n.hasPhysicalPort(inf2)==false )
			return new LinkHandler(this.inf1, this.inf2);
		else if( n.hasPhysicalPort(inf2) && n.hasPhysicalPort(inf1)==false )
			return new LinkHandler(this.inf2, this.inf1);
		else 
			return null;
	}//end of method
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof FullDuplexLink )	{
			Link l = (Link)obj;
			return l.tostring().equals(this.tostring());
		}else
			return false;
	}
	@Override
	public String toString() {
		return this.tostring();
	}
}
