package edu.umbc.bft.net.bean;

import java.util.HashMap;
import java.util.Map;

import edu.umbc.bft.factory.PacketFactory;
import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.DefaultHeader;
import edu.umbc.bft.net.packet.impl.DefaultPacket;
import edu.umbc.bft.net.packet.impl.FloodHeader;
import edu.umbc.bft.net.packet.payload.Datagram;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.secure.RSAPriv;

public class NodeManager implements PacketFactory	{
	
	private Map<String, NodeBuffer> buffer;
	private long currentSequenceNo;
	private String name;
	private RSAPriv key;
	
	public NodeManager(String name, RSAPriv key) {
		this.key = key;
		this.name = name;
		this.currentSequenceNo = 1L;
		this.buffer = new HashMap<String, NodeBuffer>();
	}//end of constructor
	
	public long getSequenceNo() {
		return this.currentSequenceNo;
	}
	public long getNextSequenceNo() {
		return ++this.currentSequenceNo;
	}
	
	public NodeBuffer getBuffer(String nodeId) {
		if( nodeId != null )
			return this.buffer.get(nodeId);
		else
			return null;
	}
	
	public boolean checkFromBuffer(String nodeId, Packet p)	{
		if( nodeId!=null && this.buffer.containsKey(nodeId) )	{
			return this.buffer.get(nodeId).updateLastMessage(p);
		}else if( nodeId!=null && p!=null )	{
			NodeBuffer buf = new NodeBuffer(p);
			return this.buffer.put(nodeId, buf)!=null;
		}
		return false;
	}
	
	public DatagramRoute calculateRoute(String destNodeId) {
		//TODO
		return null;
	}
	
	@Override
	public Header createDefaultHeader(String dest)	{
		DefaultHeader h = new DefaultHeader(this.name, dest);
		h.setSequenceNo(this.getNextSequenceNo());
		return h;
	}
	@Override
	public Header createFloodHeader()	{
		FloodHeader h = new FloodHeader(this.name);
		h.setSequenceNo(this.getNextSequenceNo());
		return h;
	}
	@Override
	public Packet createIdentificationMessage()	{
		Identification i = new Identification(this.name);
		i.addSignature(this.key);
		return this.createPacket(i);
	}
	@Override
	public Packet createDatagram(String data, DatagramRoute route)	{
		Datagram d = new Datagram(data);
		d.setRoute(route);
		d.addSignature(key);
		return this.createPacket(d);
	}

	
	public Packet createPacket(Payload p)	{
		return this.createPacket(this.createFloodHeader(), p);
	}
	public Packet createPacket(Header h, Payload p)	{
		if( h!=null && p!=null )
			return new DefaultPacket(h, p);
		else 
			return null;
	}
	
}