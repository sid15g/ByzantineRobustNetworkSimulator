package edu.umbc.bft.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.umbc.bft.exception.IPNotAvailableException;
import edu.umbc.bft.exception.InvalidLinkException;
import edu.umbc.bft.exception.InvalidNodeIDException;
import edu.umbc.bft.factory.IPFactory;
import edu.umbc.bft.factory.LinkFactory;
import edu.umbc.bft.factory.NodeFactory;
import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.conn.Eth;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.Link;
import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;


public class NetworkBuilder		{
	
	private Map<String, RSAPub> publicKeyList;
	private Map<Integer, Switch> switches;
	private Set<String> links;
	private IPFactory factory;
	private Random rand;
	
	NetworkBuilder()	{
		this.rand = new Random();
		this.factory = new IPFactory();
		this.links = new HashSet<String>();
		this.switches = new HashMap<Integer, Switch>();
		this.publicKeyList = new HashMap<String, RSAPub>();
	}//end of constructor
	
	public List<Thread> createSwitches(int total, int tnodes, int faulty)	{
		
		List<Thread> list = new ArrayList<Thread>();
		
		float tprob = (float)tnodes/(float)total;
		float fprob = (float)faulty/(float)total;
		boolean faultynode = false;
		int switches = total - tnodes;
		int id = 1;
		
		for( int i=0; i<total; i++ )	{
			
			faultynode = false;
			Switch s = null;
			
			if( rand.nextFloat()<fprob && faulty>0 )	{
				faultynode = true;
				faulty--;
			}
			
			if( rand.nextFloat()<tprob && tnodes>0 )	{
				s = NodeFactory.createTrustedNode(this.publicKeyList, faultynode);
				tnodes--;
			}else if( switches > 0 )	{
				s = NodeFactory.createSwitch(faultynode);
				switches--;
			}else
				s = NodeFactory.createTrustedNode(this.publicKeyList, faultynode);
			
			RSAPub key = s.getPublicKey();	
			this.switches.put(id++, s);
			this.publicKeyList.put(s.getName(), key);
			
			list.add(new Thread(s));
			
		}//end of loop
		
		return list;
		
	}//end of method

	
	public boolean connect(int nodeId, String links[]) throws InvalidNodeIDException, InvalidLinkException, IPNotAvailableException	{
		
		if( this.switches.containsKey(nodeId) == false  )
			throw InvalidNodeIDException.invalidNodeId(nodeId);
		
		boolean res = true;
		
		for( int j=0; links!=null && j<links.length; j++ )	{
			
			int nid = Integer.parseInt(links[j]);
			
			if( nid == -1 )
				continue;
			else if( this.switches.containsKey(nid) == false )
				throw InvalidLinkException.invalidLink(nodeId, nid);
			else
				res &= this.connect(nodeId, nid);
			
		}//end of loop
		
		return res;
		
	}//end of method
	
	
	private boolean connect(int nid1, int nid2) throws IPNotAvailableException	{
		
		Switch s1 = this.switches.get(nid1);
		Switch s2 = this.switches.get(nid2);
		
		if( s1!=null && s2!=null )	{
			
			String link = s2.getName() + s1.getName();
			
			if( this.links.contains(link)==false )
				return this.connect(s1, s2);
			else	{
				Logger.sysLog(LogValues.debug, this.getClass().getName(), " Already Exists: ["+ s1.getName() +"<-->"+ s2.getName() +"]" );
				return true;
			}
			
		}else
			return false;
		
	}//end of method
	
	
	public boolean connect(Switch s1, Switch s2) throws IPNotAvailableException		{
		
		IPAddress ip1 = this.factory.createIP();
		Interface i1 = new Eth(ip1);

		IPAddress ip2 = this.factory.createIP();
		Interface i2 = new Eth(ip2);
		
		Link l = LinkFactory.create(i1, i2, true);
		
		if( l != null )		{
			s1.addPhysicalPort(i1);
			boolean res = s1.attach(i1, l);
			
			s2.addPhysicalPort(i2);
			res &= s2.attach(i2, l);
			
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Link: ["+ s1.getName() +"<-->"+ s2.getName() +"]" );
			this.links.add(s1.getName() + s2.getName());
			this.links.add(s2.getName() + s1.getName());
			
			return res;
		}//end of null check
		
		return false;
		
	}//end of method
	

	public void initializeAndRunNodes(List<Thread> list)	{
		
		for( int i=0; list!=null && i<list.size(); i++ )
			list.get(i).start();
		
	}//end of method
	
	
	public void dump()	{

		Iterator<Integer> iter = this.switches.keySet().iterator();
		List<Switch> list = new ArrayList<Switch>();
		List<String> names = new ArrayList<String>();
		
		while( iter.hasNext() )	{
			int id = iter.next();
			Switch s = this.switches.get(id);
			list.add(s);
			names.add(s.getName());
		}//end of loop

		Logger.sysLog(LogValues.info, this.getClass().getName(), " Nodes: "+ names.toString() );
		
		
	}//end of method
	
}