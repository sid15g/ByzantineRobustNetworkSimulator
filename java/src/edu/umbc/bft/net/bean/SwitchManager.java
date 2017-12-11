package edu.umbc.bft.net.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umbc.bft.factory.PacketFactory;
import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.DefaultHeader;
import edu.umbc.bft.net.packet.impl.DefaultPacket;
import edu.umbc.bft.net.packet.impl.FloodHeader;
import edu.umbc.bft.net.packet.impl.PayloadWithKey;
import edu.umbc.bft.net.packet.payload.Datagram;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.LinkState;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class SwitchManager implements PacketFactory	{
	
	private Map<String, RSAPub> trustedKeyList;
	private List<NeighborDetail> myNeighbors;
	private Map<String, NodeBuffer> buffer;
	private long currentSequenceNo;
	private byte keyListCount;
	private String name;
	private RSAPriv key;
	
	public SwitchManager(String name, RSAPriv key) {
		this.key = key;
		this.name = name;
		this.keyListCount = 0;
		this.trustedKeyList = null;
		this.currentSequenceNo = 1L;
		this.buffer = new HashMap<String, NodeBuffer>();
		this.myNeighbors = new ArrayList<NeighborDetail>();
	}//end of constructor
	
	private final String sublog()	{
		return "["+ this.name +"] ";
	}
	
	@Override
	public String getSourceNodeId() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSequenceNo() {
		return this.currentSequenceNo;
	}
	public long getNextSequenceNo() {
		return this.currentSequenceNo++;
	}
	public void increaseKeyListCount() {
		this.keyListCount++;
		Logger.sysLog(LogValues.debug, this.getClass().getName(), this.sublog() +" COUNT "+ this.keyListCount );
	}
	public void resetKeyListCount() {
		this.keyListCount = 0;
	}
	public byte getKeyListCount() {
		return this.keyListCount;
	}
	
	public int getBufferSize() {
		return this.buffer.size();
	}
	
	public NodeBuffer getBuffer(String nodeId) {
		if( nodeId != null )
			return this.buffer.get(nodeId);
		else
			return null;
	}
	public boolean checkFromBuffer(Packet p)	{
		
		if( p==null )
			return false;
		
		String nodeId = p.getHeader().getSource();
		
		if( nodeId!=null && this.buffer.containsKey(nodeId) )	{
			return this.buffer.get(nodeId).updateLastMessage(p);
		}else if( nodeId!=null && p!=null )	{
			NodeBuffer buf = new NodeBuffer(this.name, nodeId, p);
			this.buffer.put(nodeId, buf);
			return true;
		}
		return false;
		
	}//end of method
	
	public boolean verifyPayloadSignature(Packet p)	{
		return this.verifyPayloadSignature(p, this.trustedKeyList);
	}
	public boolean verifyPayloadSignature(Packet p, Map<String, RSAPub> map)	{
		if( p==null )	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Cannot verify payload - packet not found " );
			return false;
		}else if( map==null )	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Cannot verify payload - key list not found " );
			return false;
		}
			
		String src = p.getHeader().getSource();
		RSAPub key = map.get(src);
		
		if( key!=null && p.getPayload() instanceof PayloadWithKey )		{
			PayloadWithKey payload = (PayloadWithKey)p.getPayload();
			return payload.verifySignature(key);
		}else
			return false;
	}
	
	public boolean addNeighborDetail( String nodeId, RSAPub key )	{
		
		NeighborDetail n = new NeighborDetail(nodeId, key);
		
		if( nodeId != null && this.myNeighbors.contains(n)==false )	{
			this.myNeighbors.add(n);
			return true;
		}else if( nodeId != null )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +" Neighbour detail updated ->  ["+ nodeId +"]"  );
			int index = this.myNeighbors.indexOf(n);
			n = this.myNeighbors.get(index);
			n.setKey(key);
			return true;
		}else
			return false;
	}
	public boolean addNeighborDetail( Identification i )	{
		return this.addNeighborDetail(i.getName(), i.getKey());
	}	
	public String[] getMyNeighbors()	{
		String []arr = new String[this.myNeighbors.size()];
		for( int i=0; i<arr.length; i++ )	{
			arr[i] = this.myNeighbors.get(i).getName();
		}//end of loop
		return arr;
	}
	
	public int getTrustedNodeCount() {
		return this.trustedKeyList.size();
	}
	public Map<String, RSAPub> getTrustedKeyList() {
		return this.trustedKeyList;
	}
	public void setTrustedKeyList(Map<String, RSAPub> trustedKeyList) {
		this.trustedKeyList = trustedKeyList;
	}
	
	public DatagramRoute calculateRoute(String destNodeId) {
		//TODO Phase 2
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
	@Override
	public Packet createLinkStatePacket() {
		LinkState ls = new LinkState(this.myNeighbors);
		ls.addSignature(this.key);
		return this.createPacket(ls);
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