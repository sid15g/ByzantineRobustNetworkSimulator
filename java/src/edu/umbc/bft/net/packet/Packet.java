package edu.umbc.bft.net.packet;

import edu.umbc.bft.net.packet.impl.FloodHeader;

public interface Packet	{
	
	public Header getHeader();
	public Payload getPayload();
	
	public default boolean toFlood()	{
		Header h = this.getHeader();
		if( h != null )
			return h instanceof FloodHeader;
		else
			return false;
	}
	
	public default boolean hasSignature()	{
		if( this.getPayload()!=null && this.getPayload().hasSignature() )
			return true;
		else
			return false;
	}
	
	/** Description of Packet : includes source, destination and seqNo. */
	public default String dscp()	{
		
		final String ptype = this.getPayload().getClass().getSimpleName();
		Header h = this.getHeader();
		
		if( h!=null && h instanceof FloodHeader )
			return "["+ ptype +"]["+ h.getSequenceNumber() +", "+ h.getSource() +"] ";
		else if( h!=null )
			return "["+  ptype +"]["+ h.getSequenceNumber() +", "+ h.getSource() +", "+ h.getDestination() +"] ";
		else
			return "[]";
		
	}//end of method
	
}
