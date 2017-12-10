package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.abs.AbstractSwitchImpl;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.PublickKeyList;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class GeneralSwitch extends AbstractSwitchImpl	{

	public GeneralSwitch() {
		super();
	}//End of Constructor
	
	public GeneralSwitch(int id) {
		this();
		this.setName("S"+ id);
	}//End of Constructor
	
	@Override
	protected void execute(Interface i, Packet p) {
		
		if( super.manager.checkFromBuffer(p) == false )	{
			return;
		}else
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		
		switch( p.getPayload().getClass().getSimpleName() )	{
		
			case "Identification":
				Identification id = (Identification)p.getPayload();
				if( super.manager.addNeighborDetail(id) == false )
					Logger.sysLog(LogValues.warn, this.getClass().getName(), this.subLog() +" Neighbour not added " );
				//TODO ELSE start a timer
				break;
				
			case "LinkState":
				break;
				
			case "PublickKeyList":
				
				PublickKeyList payload = (PublickKeyList)p.getPayload();
				Map<String, RSAPub> pkl = payload.getPublicKeyList();
				Map<String, RSAPub> mp = this.manager.getPublicKeyList();
				
				if( mp==null )	{
					this.manager.setPublicKeyList(pkl);
					
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Flooding ID(name, key) message " );
					super.flood(super.manager.createIdentificationMessage());
				}else	{
					mp = this.comparator.tieBreaker(mp, pkl);
					this.manager.setPublicKeyList(mp);
				}

				//TODO create new packet
				//super.flood(i, newP);
				break;
				
			case "Datagram":
				break;
				
			default:
				break;
		
		}//end of switch
		
	}//End Of Method

	
	@Override
	public void init() {
		// Nothing to do --- unless receive a PKL
	}
	
}
