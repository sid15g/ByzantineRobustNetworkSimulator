package edu.umbc.bft.net.packet;

import edu.umbc.bft.net.packet.impl.PayloadWithKey;

public interface Payload	{
	
	public byte[] toByteArray();
	
	public default boolean hasSignature()	{
		return this instanceof PayloadWithKey;
	};
	
}
