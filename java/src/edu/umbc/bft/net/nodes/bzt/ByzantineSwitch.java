package edu.umbc.bft.net.nodes.bzt;

import edu.umbc.bft.net.nodes.ByzantineNode;
import edu.umbc.bft.net.packet.Packet;

public interface ByzantineSwitch extends ByzantineNode	{

	@Override
	public default void inject(Packet p)	{
		// TODO
	}//End Of Method

}
