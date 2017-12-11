package edu.umbc.bft.factory;

import java.util.Map;

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
	
	public static Switch createSwitch(Map<String, RSAPub> tkl, boolean faulty)	{
		if( faulty )
			return new FaultySwitch(tkl, NodeFactory.nodeCount++);
		else
			return new GeneralSwitch(tkl, NodeFactory.nodeCount++);
	}//End Of Method
	
	
	public static Switch createTrustedNode(Map<String, RSAPub> pkl, Map<String, RSAPub> tkl, boolean faulty)	{
		if( faulty )
			return new FaultyTrustedNode(pkl, tkl, NodeFactory.nodeCount++);
		else
			return new TrustedNode(pkl, tkl, NodeFactory.nodeCount++);
	}//End Of Method
	
}