package edu.umbc.bft.net.bean;

import java.util.List;

import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.PayloadWithKey;
import edu.umbc.bft.net.packet.payload.LinkState;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

/**
 *  Used to maintain details of a node in a network
 *  Details like last sequence number used, the nodes neighbor list
 *  
 *  One node might have to store multiple objects of this class to store details of multiple nodes in a network
 **/
public class NodeBuffer	{

	private long lastSeqNo;
	private Payload lastMessage;
	private String bufNode, srcNode;
	private List<NeighborDetail> neighbors;
	
	NodeBuffer(String srcNode, String bufNode, Packet p)	{
		this.neighbors = null;
		this.srcNode = srcNode;
		this.bufNode = bufNode;
		this.lastMessage = p.getPayload();
		this.lastSeqNo = p.getHeader().getSequenceNumber();
	}//end of constructor
	
	private final String sublog()	{
		return "["+ this.srcNode +","+ this.bufNode +"] ";
	}
	
	
	long getLastSequenceNo() {
		return this.lastSeqNo;
	}
	
	boolean updateLastMessage(Packet p)		{
		
		if( this.isValid(p) == false )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +"  Packet dropped(Old sequence number) --> "+ p.dscp() );
		}else if ( this.checkPayload(p) == false )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +" Packet dropped(Tampered packet suspected) --> "+ p.dscp() );
		}else if( p!=null )	{
			
			this.lastSeqNo = p.getHeader().getSequenceNumber();
			this.lastMessage = p.getPayload();
			
			if( this.lastMessage instanceof LinkState )
				this.neighbors = ((LinkState) this.lastMessage).getNeighborsList();
			
			return true;
		}
		return false;
	}
	
	boolean isValid(Packet p)	{
		return p!=null?this.lastSeqNo < p.getHeader().getSequenceNumber():false;
	}
	
	boolean checkPayload(Packet p)	{
		if( p!=null && p.hasSignature() )	{
			PayloadWithKey pwk = (PayloadWithKey)p.getPayload();
			return pwk.verifySignature();
		}else if( p!=null )
		/** Consider packet valid it if has no signature */
			return true;
		else
			return false;
	}
	
	List<NeighborDetail> getNeighbors() {
		return this.neighbors;
	}
	
}
