package edu.umbc.bft.net.bean;

import java.util.List;

import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.impl.PayloadWithKey;
import edu.umbc.bft.net.packet.payload.LinkState;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class NodeBuffer	{

	private int lastSeqNo;
	private Payload lastMessage;
	private List<NeighborDetail> neighbors;
	
	NodeBuffer(Packet p)	{
		this.neighbors = null;
		this.lastMessage = p.getPayload();
		this.lastSeqNo = p.getHeader().getSequenceNumber();
	}//end of constructor
	
	boolean updateLastMessage(Packet p)		{
		
		if( this.isValid(p) == false )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Old Sequence Number.... Packet Dropped " );
		}else if ( this.checkPayload(p) == false )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Invalid Signature... Packet Dropped " );
		}else if( p!=null )	{
			
			this.lastSeqNo = p.getHeader().getSequenceNumber();
			this.lastMessage = p.getPayload();
			
			if( this.lastMessage instanceof LinkState )
				this.neighbors = ((LinkState) this.lastMessage).getNeighborsList();
			
			return true;
		}
		return false;
	}//end of method
	
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
