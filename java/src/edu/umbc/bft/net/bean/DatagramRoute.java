package edu.umbc.bft.net.bean;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/** Defines the route, decided by the source */
public class DatagramRoute implements Iterator<String>	{
	
	private Queue<String> route;
	
	DatagramRoute()	{
		this.route = new LinkedBlockingQueue<String>();
	}//end of constructor
	
	boolean add(String nodeId)	{
		if( nodeId != null )
			return this.route.add(nodeId.trim());
		else
			return false;
	}//end of method
	
	@Override
	public boolean hasNext() {
		return !this.route.isEmpty();
	}	
	@Override
	public String next() {
		return this.route.poll();
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.route.toArray());
	}
	
}
