package edu.umbc.bft.net.nodes.impl;

import java.util.Map;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.Manager.LSPState;
import edu.umbc.bft.net.nodes.abs.AbstractTrustedNodeImpl;
import edu.umbc.bft.net.nodes.bzt.ByzantineSwitch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.LinkState;
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
		}else if( (this instanceof ByzantineSwitch) == false )
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		
		boolean verify = false;
		
		/** ------------------------------ TRUSTED NODE ----------------------------- **/
		switch( p.getPayload().getClass().getSimpleName() )		{

			case "PublicKeyList":
				
				if( this.getName().equals(p.getSource()) )
					Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" Self Public Key List ignored! " );
				else	{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Public Key List received! " );
					this.manager.increaseKeyListCount();
					super.flood(i, p);
					
					if( this.manager.verifyPayloadSignature(p) == false )	{
						Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" PKL verification failed " );
						this.manager.increaseLinkCost(p.getSource(), i);
					}else{
						//TODO send PKL - ACK
					}
				}
				
				boolean flooded = this.manager.checkAndFloodIdentificationMessage(p, super.getInterfaceManager());
				
				if( flooded && super.startIdentificationTimer() )	{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Waiting for Neighboring IDs to flood LSP " );
				}
				
				break;				

			case "Identification":
				
				Identification id = (Identification)p.getPayload();				
				
				super.manager.addNeighborDetail(id);
				verify = this.manager.verifyPayloadSignature(p, this.comparator.getMap()); 
				Logger.sysLog(LogValues.debug, this.getClass().getName(), this.subLog() +" ID Verification -> ["+ id.getName() +"] : "+ verify );
				
				if( verify == false )		{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Neighbor verification failed | Cost increased " );
					/** Since ID was not verified, increase the cost of the link */
					this.manager.increaseLinkCost(id.getName(), i);
				}
				
				if( this.manager.isLSPState(LSPState.Sent) )	{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Waiting to flood new LSP " );
					this.manager.setLSPState(LSPState.ReSend);
					super.startIdentificationTimer();
				}
				
				break;
				
			case "LinkState":
				/** Link State Neighbor information already handled in 'manager.checkFromBuffer()->NodeBuffer:updateLastMessage()' */
				LinkState ls = (LinkState)p.getPayload();
				String pstr = "[ "+ p.getSource() +", "+ ls.toString() +" ]";
				Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" LSP received : "+ pstr );
				
				super.flood(i, p);
				break;
				
			case "Datagram":
				//TODO create route, forward and wait for ACK
				break;
				
			case "PKLAck":
				//TODO
				break;
				
			case "DataAck":
				//TODO
				break;
				
			default:
				Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" UnIdentified Packet Received " );
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
