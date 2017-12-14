package edu.umbc.bft.net.nodes.bzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.net.bean.NeighborDetail;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.net.packet.payload.LinkState;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class FaultyBehavior {

	private String name;
	private Random randBehavior;
	private ByzantineSwitch fnode;
	private boolean spoofed, faulty;			/** Since faulty behavior is nor guaranteed, 'faulty' boolean determines the faulty behavior */

	private Map<String, Payload> idSpoofing;
	private Map<String, RSAPriv> keySpoofing;
	private List<NeighborDetail> costSpoofing;
	private Map<String, String> nodeToNodeSpoofDetail;
	private Map<String, List<NeighborDetail>> linksSpoofing;
	
	FaultyBehavior(String nodeName, ByzantineSwitch fnode)	{
		this.fnode = fnode;
		this.name = nodeName;
		this.randBehavior = new Random();
		this.faulty = this.spoofed = false;
		this.idSpoofing = new HashMap<String, Payload>(2);
		this.keySpoofing = new HashMap<String, RSAPriv>(2);
		this.costSpoofing = new ArrayList<NeighborDetail>();
		this.nodeToNodeSpoofDetail = new HashMap<String, String>(2);
		this.linksSpoofing = new HashMap<String, List<NeighborDetail>>(2);
	}//end of constructor
	
	public final String sublog()	{
		return "["+ this.name +"] ";
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFaulty() {
		return this.faulty;
	}
	public void makeFaulty() {
		this.faulty = true;
	}
	
	public Map<String, Payload> getIdSpoofing() {
		return this.idSpoofing;
	}
	public Map<String, RSAPriv> getFaultyKeyList() {
		return this.keySpoofing;
	}
	public String getNodeToNodeSpoof(String name)	{
		return this.nodeToNodeSpoofDetail.get(name);
	}
	public Random getRandBehavior() {
		return this.randBehavior;
	}
	
	public boolean isSpoofed() {
		return this.spoofed;
	}
	public void doneSpoofing() {
		this.spoofed = true;
	}
	public boolean checkIdSpoof()	{
		/** Only after having 2 ID packet from neighbors, I can spoof them by swapping IDs */
		return this.spoofed==false?this.idSpoofing.size()>=1:false;
	}
	public Identification getIdSpoof(String victimID)	{
		
		if( this.checkIdSpoof() )	{
			
			String otherId = null;
			
			if( this.nodeToNodeSpoofDetail.containsKey(victimID) == false )
				otherId = this.getRandomNodeId();
			else	{
				otherId = this.nodeToNodeSpoofDetail.get(victimID);
			}
			
			this.nodeToNodeSpoofDetail.put(otherId, victimID);
			RSAPriv key = this.fnode.getNewKeyFor(victimID);
			Identification id = new Identification(otherId);
			id.addSignature(key);
			this.setIdPacket(id);
			Logger.sysLog(LogValues.imp, this.getClass().getName(), this.sublog() +" Spoofing as "+ otherId +" for : "+ victimID);
			
			return id;
			
		}else
			return null;

	}
	
	public void setIdPacket(Payload p)	{
		if( p instanceof Identification )
			this.setIdPacket((Identification)p);
	}
	public void setIdPacket(Identification id)	{
		if( id != null )	{
			/** First time just add source name and save - before sending change the name, add signature, save and send */
			this.idSpoofing.put(id.getName(), id);
		}else
			Logger.sysLog(LogValues.error, this.getClass().getName(), this.sublog() +" Unable to set Fake ID(name, key) packet " );
	}
	
	/** return modified neighbor list */
	public List<NeighborDetail> spoofNeighborListFor(Packet p)		{
		
		String victim = this.nodeToNodeSpoofDetail.get(p.getSource());
		
		if( p.getPayload() instanceof LinkState && victim!=null )	{
			
			List<NeighborDetail> spoof = new ArrayList<NeighborDetail>();
			LinkState ls = (LinkState)p.getPayload();
			
			List<NeighborDetail> original = ls.getNeighborsList();
			
			for( int i=0; i<original.size(); i++ )		{
				
				NeighborDetail nd = original.get(i);
				
				if( nd.getName().equals(this.name) == false )
					spoof.add(nd);
				
			}//end of loop
		
			this.linksSpoofing.put(victim, spoof);
			return spoof;
			
		}//end of LinkState check
		
		return null;
		
	}
	
	public NeighborDetail createFakeCost(NeighborDetail original)	{
		NeighborDetail nd = new NeighborDetail(original);
		double factor = this.randBehavior.nextDouble();
		double nCost = nd.getCost()*factor;
		nd.setCost(nCost);
		this.costSpoofing.add(nd);
		Logger.sysLog(LogValues.imp, this.getClass().getName(), this.sublog() +" Fake Cost | Original reduced by : "+ Double.valueOf(factor*100) );
		return nd;
	}
	
	private String getRandomNodeId()	{
		Object []keys = this.idSpoofing.keySet().toArray();
		if( keys.length == 1 )	{
			return String.valueOf(keys[0]);
		}else {
			int index = this.randBehavior.nextInt(keys.length);
			return String.valueOf(keys[index]);
		}
	}
	
}
