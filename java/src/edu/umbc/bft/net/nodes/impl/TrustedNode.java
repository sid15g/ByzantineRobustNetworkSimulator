package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.abs.AbstractTrustedNodeImpl;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class TrustedNode extends AbstractTrustedNodeImpl	{

	public TrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl) {
		super(pkl);
		super.comparator.setMap(pkl);
		super.manager.setTrustedKeyList(tkl);
	}//End of Constructor
	
	public TrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl, int id)	{
		this(pkl, tkl);
		this.setName("TN"+ id);
	}//End of Constructor
	
	@Override
	protected void execute(Interface i, Packet p) {
		
		if( super.manager.checkFromBuffer(p) == false )	{
			return;
		}else
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		
		boolean verify = false;
		
		/** ------------------------------ TRUSTED NODE ----------------------------- **/
		switch( p.getPayload().getClass().getSimpleName() )		{

			case "PublicKeyList":
				
				if( this.getName().equals(p.getHeader().getSource()) )
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Self Public Key List ignored... " );
				else	{
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Other Public Key List ignored... " );
					this.manager.increaseKeyListCount();
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
		//TODO periodically broadcasts
		Packet p = super.createPublicKeyListMessage();
		this.manager.increaseKeyListCount();
		super.flood(p);
	}
	
}
