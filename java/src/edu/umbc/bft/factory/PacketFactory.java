package edu.umbc.bft.factory;

import edu.umbc.bft.net.bean.DatagramRoute;
import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.DefaultHeader;
import edu.umbc.bft.net.packet.payload.Datagram;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.secure.RSAPriv;

public class PacketFactory	{
	
	public static Header createDefaultHeader(IPAddress src, IPAddress dest, int seqNo)	{
		DefaultHeader h = new DefaultHeader(src, dest);
		h.setSequenceNo(seqNo);
		return h;
	}
	
	public static Payload createIdentificationMessage(String name, IPAddress ip, RSAPriv key)	{
		Identification i = new Identification(name, ip);
		i.addSignature(key);
		return i;
	}
	
	public static Payload createDatagram(String data, DatagramRoute route, RSAPriv key)	{
		Datagram d = new Datagram(data);
		d.setRoute(route);
		d.addSignature(key);
		return d;
	}
	
}
