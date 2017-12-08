package edu.umbc.bft.factory;

import edu.umbc.bft.net.bean.DatagramRoute;
import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Packet;

public interface PacketFactory	{
	
	public Header createDefaultHeader(String dest);
	public Header createFloodHeader();
	
	public Packet createIdentificationMessage();
	public Packet createDatagram(String data, DatagramRoute route);
	
}
