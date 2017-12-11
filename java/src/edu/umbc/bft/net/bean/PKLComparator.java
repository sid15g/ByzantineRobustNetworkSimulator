package edu.umbc.bft.net.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class PKLComparator<K, V>	{

	private String nodeName;
	private Map<K, V> map;
	private Random rand;
	
	public PKLComparator(String name) {
		this.map = null;
		this.nodeName = name;
		this.rand = new Random();
	}//end of constructor
	
	private final String sublog()	{
		return "["+ this.nodeName +"] ";
	}
	
	public void setMap(Map<K, V> map) {
		this.map = map;
	}
	public Map<K, V> getMap() {
		return this.map;
	}
	
	public boolean compare(Map<K, V> m2) {
		if( m2!=null )	{
			return this.map.equals(m2);
		}else if( this.map==null && m2==null )
			return true;
		else
			return false;
	}

	public Map<K, V> tieBreaker(Map<K, V> m2) {
		if( this.map==null && m2!=null )	{
			this.map = m2;
		}else if( this.compare(m2) == false )	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), this.sublog() +" TieBreaker - Public Key List " );
			double val = this.rand.nextDouble();
			this.map = val>=0.5000000?this.map:m2;
		}
		return this.map;
	}
	
	
	/** Add elements in m2 (not in m1) to m1, but does not override any value */
	public Map<K, V> safeUnion(Map<K, V> m2) {
		
		if( this.map == null )	{
			this.map = m2;
		}else if( m2!=null )	{
			
			Iterator<K> iter = m2.keySet().iterator();
			
			while( iter.hasNext() )	{
				
				K key = iter.next();
				V val = m2.get(key);
				
				if( this.map.containsKey(key) == false )
					this.map.put(key, val);
				
			}//end of loop	
			
		}else	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Safe Union of maps failed " );
		}
		
		return this.map;
		
	}//end of method
	
	
	public boolean verify(K key, V value) {
		
		if( this.map!=null && this.map.containsKey(key) )	{
			V val = this.map.get(key);
			return val.equals(value);
		}
		
		return false;
		
	}//end of method
	
}
