package edu.umbc.bft.net.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Manager;
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

public class SwitchManager implements Manager	{
	
	private Map<String, RSAPub> trustedKeyList;
	private List<NeighborDetail> myNeighbors;
	private Map<String, NodeBuffer> buffer;
	private long currentSequenceNo;
	private byte keyListCount;
	private int lspState;
	private String name;
	private RSAPriv key;
	
	public SwitchManager(String name, RSAPriv key) {
		this.key = key;
		this.name = name;
		this.keyListCount = 0;
		this.trustedKeyList = null;
		this.lspState  = LSPState.WaitingIDP.ordinal();
		this.buffer = new HashMap<String, NodeBuffer>();
		this.myNeighbors = new ArrayList<NeighborDetail>();
		this.currentSequenceNo = this.getInitialSequenceNo();
	}//end of constructor
	
	protected final String sublog()	{
		return "["+ this.name +"] ";
	}
	private long getInitialSequenceNo()	{
		long temp = new Random().nextLong() % 1000;
		return temp>0?temp:(temp*-1);
	}
	
	RSAPriv getKey() {
		return this.key;
	}
	
	@Override
	public String getSourceNodeId() {
		return this.name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public long getSequenceNo() {
		return this.currentSequenceNo;
	}
	@Override
	public long getNextSequenceNo() {
		return this.currentSequenceNo++;
	}
	long getLastSequenceNo(String nodeName)	{
		if( this.buffer.containsKey(nodeName) )	{
			return this.buffer.get(nodeName).getLastSequenceNo();
		}
		return -1;
	}
	@Override
	public void increaseKeyListCount() {
		this.keyListCount++;
		Logger.sysLog(LogValues.debug, this.getClass().getName(), this.sublog() +" COUNT "+ this.keyListCount );
	}
	@Override
	public void resetKeyListCount() {
		this.keyListCount = 0;
	}
	byte getKeyListCount() {
		return this.keyListCount;
	}
	
	@Override
	public boolean isLSPState(LSPState state) {
		return this.lspState == state.ordinal();
	}	
	@Override
	public void setLSPState(LSPState state)	{
		this.lspState = state.ordinal();
	}
	
	@Override
	public int getBufferSize() {
		return this.buffer.size();
	}
	
	@Override
	public boolean checkFromBuffer(Packet p)	{
		
		if( p==null )
			return false;
		
		String nodeId = p.getSource();
		
		if( nodeId!=null && this.buffer.containsKey(nodeId) )	{
			return this.buffer.get(nodeId).updateLastMessage(p);
		}else if( nodeId!=null && p!=null )	{
			NodeBuffer buf = new NodeBuffer(this.name, nodeId, p);
			this.buffer.put(nodeId, buf);
			return true;
		}
		return false;
		
	}//end of method
	
	@Override
	public boolean verifyPayloadSignature(Packet p)	{
		return this.verifyPayloadSignature(p, this.trustedKeyList);
	}
	
	@Override
	public boolean verifyPayloadSignature(Packet p, Map<String, RSAPub> map)	{
		if( p==null )	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Cannot verify payload - packet not found " );
			return false;
		}else if( map==null )	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Cannot verify payload - key list not found " );
			return false;
		}
			
		String src = p.getSource();
		RSAPub key = map.get(src);
		
		if( key!=null && p.getPayload() instanceof PayloadWithKey )		{
			PayloadWithKey payload = (PayloadWithKey)p.getPayload();
			return payload.verifySignature(key);
		}else
			return false;
	}
	
	@Override
	public boolean addNeighborDetail( Identification i )	{
		return this.addNeighborDetail(i.getName(), i.getKey());
	}
	boolean addNeighborDetail( String nodeId, RSAPub key )	{
		
		NeighborDetail n = new NeighborDetail(nodeId, key);
		
		if( nodeId != null && this.myNeighbors.contains(n)==false )	{
			this.myNeighbors.add(n);
			return true;
		}else if( nodeId != null )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +" Neighbor detail updated ->  ["+ nodeId +"]"  );
			int index = this.myNeighbors.indexOf(n);
			n = this.myNeighbors.get(index);
			n.setKey(key);
			return true;
		}else	{
			Logger.sysLog(LogValues.warn, this.getClass().getName(), this.sublog() +" Neighbor not added " );
			return false;
		}
	}
	boolean updateNeighborDetail( String nodeId, Double cost )	{
		
		if( this.myNeighbors!=null && nodeId !=null )			{
			for( int i=0; i<this.myNeighbors.size(); i++ )	{
				NeighborDetail n = this.myNeighbors.get(i);
				
				if( n.getName().equals(nodeId) )	{
					n.setCost(cost);
					return true;
				}
			}
		}
		return false;
		
	}

	@Override
	public boolean checkAndFloodIdentificationMessage(Packet p, InterfaceManager manager)		{
		
		if( this.isPKLsReceived() )		{
			
			Logger.sysLog(LogValues.debug, this.getClass().getName(), this.sublog() +" Flooding ID(name, key) message " );
			manager.flood(this.createIdentificationMessage());
			this.increaseKeyListCount();
			
			return true;
			
		}else	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +" Waiting for PKLs, then broadcast IDP " );
		}/** once all PKLs are received from all Trusted Nodes, flood ID */
		
		return false;
		
	}
	
	
	@Override
	public String[] getMyNeighbors()	{
		String []arr = new String[this.myNeighbors.size()];
		for( int i=0; i<arr.length; i++ )	{
			arr[i] = this.myNeighbors.get(i).getName();
		}//end of loop
		return arr;
	}
	
	Map<String, RSAPub> getTrustedKeyList() {
		return this.trustedKeyList;
	}

	int getTrustedNodeCount() {
		return this.trustedKeyList.size();
	}
	@Override
	public void setTrustedKeyList(Map<String, RSAPub> trustedKeyList) {
		this.trustedKeyList = trustedKeyList;
	}
	@Override
	public boolean isPKLsReceived()	{
		return this.getKeyListCount()==this.getTrustedNodeCount();
	}
	
	@Override
	public void increaseLinkCost(String nodeId, Interface i)	{
		double ucost = i.getLink().increaseCost();
		
		if( this.updateNeighborDetail(nodeId, ucost) == false )
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Neighbor link cost increase failed ");
		
	}
	
	@Override
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
		List<NeighborDetail> list = new ArrayList<NeighborDetail>();
		
		final NeighborDetail me = new NeighborDetail(this.name, this.key.getPublicKey());
		list.add(me);

		for( int i=0; i<this.myNeighbors.size(); i++ )	{
			list.add(this.myNeighbors.get(i));
		}
		
		LinkState ls = new LinkState(list);
		ls.addSignature(this.key);
		return this.createPacket(ls);
	}
	
	@Override
	public Packet createPacket(Payload p)	{
		return this.createPacket(this.createFloodHeader(), p);
	}
	@Override
	public Packet createPacket(Header h, Payload p)	{
		if( h!=null && p!=null )
			return new DefaultPacket(h, p);
		else 
			return null;
	}
	
}