package edu.umbc.bft.net.packet;

public interface Packet	{
	public Header getHeader();
	public Payload getPayload();
	
	public default boolean hasSignature()	{
		if( this.getPayload()!=null && this.getPayload().hasSignature() )
			return true;
		else
			return false;
	};
}
