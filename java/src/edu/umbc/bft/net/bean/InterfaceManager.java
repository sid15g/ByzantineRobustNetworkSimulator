package edu.umbc.bft.net.bean;

import java.util.List;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.LinkHandler;
import edu.umbc.bft.net.nodes.Node;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class InterfaceManager	{
	
	private Node node;
	private List<LinkHandler> handlers;
	
	public InterfaceManager(Node n, List<LinkHandler> handlers)	{
		this.node = n;
		this.handlers = handlers;
	}//end of constructor
	
	private final String sublog()	{
		return "["+ this.node.getName() +"]  ";
	}
	
	public void flood(Packet p) {
		this.flood(null, p);
	}
	public void flood(Interface src, Packet p) {
		
		if( p == null )
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" No Packet to flood ");
		
		final int size = this.handlers.size();
		
		for( int i=0; i<size; i++ )		{
			LinkHandler handler = this.handlers.get(i);
			
			if( src==null || handler.isSource(src) == false )	{
				handler.send(p);
			}
		}//End of loop
		
	}//End of Method
	
	
	public boolean replyFrom(Interface src, Packet p)	{
		
		LinkHandler handler = src.getLink().getHandler(this.node);
		
		if( handler != null )	{
			handler.send(p);
			return true;
		}else
			Logger.sysLog(LogValues.warn, this.getClass().getName(), this.sublog() +" Link Handler not found to reply ");
		
		return false;
	}//End of Method
	
	
	public boolean sendTo(Packet p, Interface dest) {
		
		final int size = this.handlers.size();
		
		for( int i=0; i<size; i++ )		{
			LinkHandler handler = this.handlers.get(i);
			
			if( handler.isDestination(dest) )	{
				handler.send(p);
				return true;
			}
		}//End of loop
		
		return false;
	}//End of Method
	
	
	public boolean sendTo(Packet p, String destNodeId)	{
		
		final int size = this.handlers.size();
		
		for( int i=0; i<size; i++ )		{
			LinkHandler handler = this.handlers.get(i);
			
			if( handler.isDestNodeID(destNodeId) )	{
				handler.send(p);
				return true;
			}
		}//End of loop
		
		return false;
	}//End of Method

}
