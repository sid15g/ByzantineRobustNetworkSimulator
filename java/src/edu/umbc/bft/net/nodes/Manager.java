package edu.umbc.bft.net.nodes;

import java.util.Map;

import edu.umbc.bft.factory.PacketFactory;
import edu.umbc.bft.net.bean.DatagramRoute;
import edu.umbc.bft.net.bean.InterfaceManager;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.secure.RSAPub;

public interface Manager extends PacketFactory	{
	
	public enum LSPState	{
		WaitingIDP,
		Sent,
		ReSend;
	}
	
	public int getBufferSize();
	public void setName(String name);
	
	public long getSequenceNo();
	public long getNextSequenceNo();
	
	public String[] getMyNeighbors();
	
	public void setTrustedKeyList(Map<String, RSAPub> trustedKeyList);
	
	public boolean isPKLsReceived();	
	public void increaseKeyListCount();
	public void resetKeyListCount();
	
	public void setLSPState(LSPState state);
	public boolean isLSPState(LSPState state);
	
	public void increaseLinkCost(String nodeId, Interface i);
	public DatagramRoute calculateRoute(String destNodeId);
	
	public boolean checkFromBuffer(Packet p);
	public boolean addNeighborDetail( Identification i );
	public boolean checkAndFloodIdentificationMessage(Packet p, InterfaceManager manager);
	
	
	public boolean verifyPayloadSignature(Packet p);
	public boolean verifyPayloadSignature(Packet p, Map<String, RSAPub> map);
	
}
