package edu.umbc.bft.factory;

import java.util.Set;

import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.net.nodes.impl.FaultySwitch;
import edu.umbc.bft.net.nodes.impl.FaultyTrustedNode;
import edu.umbc.bft.net.nodes.impl.GeneralSwitch;
import edu.umbc.bft.net.nodes.impl.TrustedNode;
import edu.umbc.bft.secure.RSAPub;

public class NodeFactory {

	private static int nodeCount;
	
	static{
		NodeFactory.nodeCount = 1;
	}
	
	public static Switch createSwitch(boolean faulty)	{
		if( faulty )
			return new FaultySwitch(NodeFactory.nodeCount++);
		else
			return new GeneralSwitch(NodeFactory.nodeCount++);
	}//End Of Method
	
	
	public static Switch createTrustedNode(Set<RSAPub> pkl, boolean faulty)	{
		if( faulty )
			return new FaultyTrustedNode(pkl, NodeFactory.nodeCount++);
		else
			return new TrustedNode(pkl, NodeFactory.nodeCount++);
	}//End Of Method
	
}