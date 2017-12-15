package edu.umbc.bft.net.nodes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.net.bean.FaultySwitchManager;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.bzt.ByzantineSwitch;
import edu.umbc.bft.net.nodes.bzt.FaultyBehavior;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.PublicKeyList;
import edu.umbc.bft.secure.KeyStore;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class FaultyTrustedNode extends TrustedNode implements ByzantineSwitch	{
	
	private List<RSAPriv> fKeys;
	private FaultyBehavior behavior;
	private Map<String, RSAPub> faultyPkl;
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl)	{
		super(pkl, tkl);
		FaultySwitchManager fsm = new FaultySwitchManager(super.manager, (ByzantineSwitch)this);
		this.faultyPkl  = new HashMap<String, RSAPub>();
		this.fKeys = new ArrayList<RSAPriv>();
		this.behavior = fsm.getBehavior();
		super.manager = fsm;
	}//End of Constructor
	
	public FaultyTrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl, int id) {
		this(pkl, tkl);
		this.setName("FTN"+ id);
		this.behavior.setName(super.getName());
	}//End of Constructor
	
	@Override
	public Random getRandomBehavior()	{
		return this.behavior.getRandBehavior();
	}
	@Override
	public Map<String, RSAPriv> getFaultyKeyList()	{
		return this.behavior.getFaultyKeyList();
	}
	
	@Override
	protected final void execute(Interface i, Packet p) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" Received: "+ p.dscp() );
		
		if( this.doFaultyBehaviour() || this.behavior.isFaulty() )	{
			this.inject(i, p);
			this.behavior.makeFaulty();
		}else
			super.execute(i, p);
		
	}//end of method
	
	
	@Override
	public void inject(Interface i, Packet p) {

		/** ------------------------------ FAULTY TRUSTED NODE ----------------------------- **/	
		switch( p.getPayload().getClass().getSimpleName() )		{

			case "PublicKeyList":
				super.execute(i, p);
				break;

			case "Identification":
				
				Identification idp = (Identification)p.getPayload();
				super.execute(i, p);
				
				if( this.behavior.checkIdSpoof() )		{
					
					Logger.sysLog(LogValues.imp, this.getClass().getName(), this.subLog() +" Faulty Behavior " );
					Identification fakeIdp = this.behavior.getIdSpoof(idp.getName());
					this.behavior.getIdSpoof(fakeIdp.getName());
					this.behavior.setIdPacket(p.getPayload());
					this.manager.checkAndFloodIdentificationMessage(p, super.getInterfaceManager());

				}else if( this.behavior.isSpoofed()==false )	{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" ID("+ p.getSource() +") SET " );
					this.behavior.setIdPacket(p.getPayload());
				}else	{
					Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" ID("+ p.getSource() +") sent " );
					Packet pid = this.manager.createIdentificationMessage();
					super.replyFrom(i, pid);
				}
				
				break;
				
			case "LinkState":
				/** Link cost changes handled in manager and broadcast */
				super.execute(i, p);
				break;
				
			case "Datagram":
				break;
				
			case "PKLAck":
				break;
				
			case "DataAck":
				break;
				
			default:
				Logger.sysLog(LogValues.info, this.getClass().getName(), this.subLog() +" UnIdentified Packet Received " );
				break;
		
		}//end of switch

	}//End Of Method
	
	
	@Override
	protected Packet createPublicKeyListMessage()	{
		Map<String, RSAPub> pkl = super.getPublicKeyList();
		Iterator<String> iter = pkl.keySet().iterator();
		
		while( iter.hasNext() )		{
			
			String name = iter.next();
			
			if( this.behavior.getRandBehavior().nextBoolean() )	{
				RSAPub key = pkl.get(name);
				this.faultyPkl.put(name, key);
			}else	{
				Logger.sysLog(LogValues.imp, this.getClass().getName(), this.subLog() +" Key changed for : "+ name );
				RSAPriv pKey = KeyStore.getNewKey();
				RSAPub key = pKey.getPublicKey();
				this.faultyPkl.put(name, key);
				this.fKeys.add(pKey);
			}
			
		}//end of loop
		
		PublicKeyList pm = new PublicKeyList(this.faultyPkl);
		pm.addSignature(super.getPrivateKey());
		Packet p = super.manager.createPacket(pm);
		return p;
	}
	
	@Override
	public void init() {
		//TODO periodically broadcasts
		Packet p = this.createPublicKeyListMessage();
		this.manager.increaseKeyListCount();
		super.flood(p);
	}
	
}
