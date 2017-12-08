package edu.umbc.bft.net.bean;

import java.util.HashMap;
import java.util.Map;

import edu.umbc.bft.net.packet.Packet;

public class NodeManager	{
	
	private Map<String, NodeBuffer> buffer;
	
	public NodeManager() {
		this.buffer = new HashMap<String, NodeBuffer>();
	}//end of constructor
	
	
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
		return null;
	}

}