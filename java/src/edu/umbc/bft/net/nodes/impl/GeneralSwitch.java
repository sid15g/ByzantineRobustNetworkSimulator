package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.abs.AbstractSwitchImpl;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.PublicKeyList;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class GeneralSwitch extends AbstractSwitchImpl	{

	public GeneralSwitch(Map<String, RSAPub> tkl) {
		super();
		super.manager.setTrustedKeyList(tkl);
	}//End of Constructor
	
	public GeneralSwitch(Map<String, RSAPub> tkl, int id) {
		this(tkl);
		this.setName("S"+ id);
	}//End of Constructor
	
	@Override
	protected void execute(Interface i, Packet p)			{
		
		if( super.manager.checkFromBuffer(p) == false )	{
			return;
		}else
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		
		boolean verify = false;
		
		/** ------------------------------ NORMAL SWITCH ----------------------------- **/	
		switch( p.getPayload().getClass().getSimpleName() )		{

			case "PublicKeyList":
				
				PublicKeyList payload = (PublicKeyList)p.getPayload();
				verify = this.manager.verifyPayloadSignature(p); 
				this.manager.increaseKeyListCount();
				
				if( verify )	{
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Verifying PKL signature... " );
					Map<String, RSAPub> map = this.comparator.tieBreaker(payload.getPublicKeyList());
					payload.setKeys(map);
					super.flood(i, p);
					//TODO send PKL - ACK
				}else	{
					Logger.sysLog(LogValues.warn, this.getClass().getName(), this.subLog() +" PKL Verification failed " );
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Trusted Key : "+ payload.getKey().toString() );
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Received Key "+ payload.getKey().toString() );
				}
				
				
				if( this.manager.getKeyListCount() == this.manager.getTrustedNodeCount() )	{
					
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Flooding ID(name, key) message " );
					super.flood(super.manager.createIdentificationMessage());
					this.manager.increaseKeyListCount();
					
					if( super.startIdentificationTimer() )	{
						Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Waiting for Neighboring IDs to flood LSP " );
					}
					
				}/** once all PKLs are received from all Trusted Nodes, flood ID */
	
				break;

			case "Identification":
				
				Identification id = (Identification)p.getPayload();				
				
				verify = this.manager.verifyPayloadSignature(p, this.comparator.getMap()); 
				Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" ID Verification -> ["+ id.getName() +"] : "+ verify );
				
				if( verify == false )		{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Neighbour verification failed | Cost increased " );
					/** Since ID was not verified, increase the cost of the link */
					i.getLink().increaseCost();
				}
				
				if( super.manager.addNeighborDetail(id) == false )
					Logger.sysLog(LogValues.warn, this.getClass().getName(), this.subLog() +" Neighbour not added " );
				
				break;
				
			case "LinkState":
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
