package edu.umbc.bft.net.nodes.bzt;

import java.util.Map;
import java.util.Random;

import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.nodes.ByzantineNode;
import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.secure.KeyStore;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.util.CoreUtils;

public interface ByzantineSwitch extends ByzantineNode	{

	public void inject(Interface i, Packet p);
	
	public Map<String, RSAPriv> getFaultyKeyList();
	public Random getRandomBehavior();
	
	public default FaultyBehavior getFaultyBehavior()	{
		Switch s = (Switch)this;
		return new FaultyBehavior(s.getName(), this);
	}
	public default boolean doFaultyBehaviour()		{
		int limit = CoreUtils.getPropertiesAsInteger("faulty.behavior.limit");
		double per = (double)limit/100.000;
		
		return this.getRandomBehavior().nextDouble()<=per;
	}
	
	public default RSAPriv getNewKeyFor(String victimNodeId)	{
		if( victimNodeId != null )		{
			RSAPriv priv = KeyStore.getNewKey();
			this.getFaultyKeyList().put(victimNodeId, priv);
			return priv;
		}
		return null;
	}

}
