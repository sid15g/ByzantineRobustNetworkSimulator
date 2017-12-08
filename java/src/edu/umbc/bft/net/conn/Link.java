package edu.umbc.bft.net.conn;

import edu.umbc.bft.net.nodes.Node;

public interface Link	{
	
	public Interface[] getInterfaces();
	
	public void setBandwidth(double bandwidth);
	public void setDropRate(double dropRate);
	
	public double getDropRate();
	public double getBandwidth();
	
	public LinkHandler getHandler(Node n);
	
	public default double getLinkState()	{
		return getBandwidth()*(1-getDropRate());
	};
	
	public default double compareState(Link l)	{
		return this.getLinkState() - l.getLinkState();
	};
	
	public default String tostring()	{
		
		Interface i[] = this.getInterfaces();
		
		if( this instanceof HalfDuplexLink )	{
			return i[0].tostring() +".HD."+ i[1].tostring();
		}else	{
			return i[0].tostring() +".D."+ i[1].tostring();
		}
		
	};
	
}
