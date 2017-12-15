package edu.umbc.bft.net.bean;

import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Manager;
import edu.umbc.bft.net.nodes.bzt.ByzantineSwitch;
import edu.umbc.bft.net.nodes.bzt.FaultyBehavior;
import edu.umbc.bft.net.packet.Header;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.FloodHeader;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.LinkState;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

/** Add faulty behavior to your non-faulty manager */
public class FaultySwitchManager implements Manager	{
	
	private Manager manager;
	private FaultyBehavior behavior;
	
	public FaultySwitchManager(Manager manager, ByzantineSwitch fnode) {
		this.manager = manager;
		this.behavior = fnode.getFaultyBehavior();
	}//end of constructor

	public FaultyBehavior getBehavior() {
		return this.behavior;
	}
	
	@Override
	public String getSourceNodeId() {
		return this.manager.getSourceNodeId();
	}

	@Override
	public Header createDefaultHeader(String dest) {
		return this.manager.createDefaultHeader(dest);
	}

	@Override
	public Header createFloodHeader() {
		return this.manager.createFloodHeader();
	}

	@Override
	public Packet createLinkStatePacket() {
		Packet p = this.manager.createLinkStatePacket();
		LinkState ls = (LinkState)p.getPayload();
		List<NeighborDetail> list = ls.getNeighborsList();
		Random rand =  this.behavior.getRandBehavior();
		
		for(int i=0; i<list.size(); i++)	{
			NeighborDetail nd = list.get(i);
			if( rand.nextBoolean() )	{
				float cost = nd.getCost();
				double val = rand.nextDouble();
				nd.setCost(cost*val);
				Logger.sysLog(LogValues.info, this.getClass().getName(), this.behavior.sublog() +" ["+ nd.getName() +"] Link Cost changed from "+ cost +" to "+ nd.getCost() );
			}
		}//end of loop
		
		SwitchManager man = (SwitchManager)this.manager;
		RSAPriv key = man.getKey();
		ls.addSignature(key);
		return p;
	}

	@Override
	public Packet createIdentificationMessage() {
		return this.manager.createIdentificationMessage();
	}

	@Override
	public Packet createDatagram(String data, DatagramRoute route) {
		return this.manager.createDatagram(data, route);
	}

	@Override
	public Packet createPacket(Payload p) {
		return this.manager.createPacket(p);
	}

	@Override
	public Packet createPacket(Header h, Payload p) {
		return this.manager.createPacket(h, p);
	}

	private Packet createFakePacket(Payload id, String dest) {
		
		Packet np = this.createPacket(id);
		FloodHeader h = (FloodHeader)np.getHeader();
		String src = this.behavior.getNodeToNodeSpoof(dest);
		long seqNo = ((SwitchManager)this.manager).getLastSequenceNo(src);
		h.setFakeSource(src);
		h.setSequenceNo(seqNo+1);
		return np;
	}
	
	@Override
	public int getBufferSize() {
		return this.manager.getBufferSize();
	}

	@Override
	public boolean isLSPState(LSPState state) {
		return this.manager.isLSPState(state);
	}	
	@Override
	public void setLSPState(LSPState state)	{
		this.manager.setLSPState(state);
	}
	
	@Override
	public void setName(String name) {
		this.manager.setName(name);
	}

	@Override
	public long getSequenceNo() {
		return this.manager.getSequenceNo();
	}

	@Override
	public long getNextSequenceNo() {
		return this.manager.getNextSequenceNo();
	}

	@Override
	public String[] getMyNeighbors() {
		return this.manager.getMyNeighbors();
	}

	@Override
	public void setTrustedKeyList(Map<String, RSAPub> trustedKeyList) {
		this.manager.setTrustedKeyList(trustedKeyList);
	}
	
	@Override
	public boolean isPKLsReceived()	{
		return this.manager.isPKLsReceived();
	}

	@Override
	public void increaseKeyListCount() {
		this.manager.increaseKeyListCount();
	}

	@Override
	public void resetKeyListCount() {
		this.manager.resetKeyListCount();
	}

	@Override
	public void increaseLinkCost(String nodeId, Interface i) {
		this.manager.increaseLinkCost(nodeId, i);
	}

	@Override
	public DatagramRoute calculateRoute(String destNodeId) {
		return this.manager.calculateRoute(destNodeId);
	}

	@Override
	public boolean addNeighborDetail(Identification i) {
		return this.manager.addNeighborDetail(i);
	}

	@Override
	public boolean checkFromBuffer(Packet p) {
		return this.manager.checkFromBuffer(p);
	}

	@Override
	public boolean verifyPayloadSignature(Packet p) {
		return this.manager.verifyPayloadSignature(p);
	}

	@Override
	public boolean verifyPayloadSignature(Packet p, Map<String, RSAPub> map) {
		return this.manager.verifyPayloadSignature(p, map);
	}

	@Override
	public boolean checkAndFloodIdentificationMessage(Packet p, InterfaceManager manager) {
				
		Map<String, Payload> map = this.behavior.getIdSpoofing();
		
		if( this.behavior.isFaulty()==false && this.isPKLsReceived() )	{
			this.manager.checkAndFloodIdentificationMessage(p, manager);
			return true;
		}else if( map!=null && map.size()>1 && this.isPKLsReceived() && this.behavior.isSpoofed()==false )			{
			
			String[] neighbors = this.manager.getMyNeighbors();
			
			for( int i=0; i<neighbors.length; i++ )		{

				String name = neighbors[i];
	
				if( map.containsKey(name) )		{
					Logger.sysLog(LogValues.imp, this.getClass().getName(), this.behavior.sublog() +" Sending Fake ID to "+ name );
					Packet np = this.createFakePacket(map.get(name), name);
					manager.sendTo(np, name);
				}else	{
					Logger.sysLog(LogValues.imp, this.getClass().getName(), this.behavior.sublog() +" Sending ID("+ name +") " );
					Packet normalP = this.createIdentificationMessage();
					manager.sendTo(normalP, name);
				}
				
			}//end of loop
			
			this.behavior.doneSpoofing();
			return true;
			
		}else if( this.isPKLsReceived() )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.behavior.sublog() +" Sending ID("+ p.getSource() +") message " );
			Packet normalP = this.createIdentificationMessage();
			manager.sendTo(normalP, p.getSource());
			return true;
		}else	{
			//TODO faulty might not wait for all PKL and broadcast before
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.behavior.sublog() +" Waiting for PKL, then broadcast IDP " );
			return false;
		}
		
	}

}
