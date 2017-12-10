package edu.umbc.bft.net.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class PKLComparator<K, V>	{

	private Random rand;
	
	public PKLComparator() {
		this.rand = new Random();
	}//end of constructor
	
	
	public boolean compare(Map<K, V> m1, Map<K, V> m2) {
		if( m1!=null && m2!=null )	{
			return m1.equals(m2);
		}else if( m1==null && m2==null )
			return true;
		else
			return false;
	}

	public Map<K, V> tieBreaker(Map<K, V> m1, Map<K, V> m2) {
		if( this.compare(m1, m2) == false )	{
			double val = this.rand.nextDouble();
			return val>=0.5000000?m1:m2;
		}else
			return m1;
	}
	
	
	/** Add elements in m2 (not in m1) to m1, but does not override any value */
	public Map<K, V> safeUnion(Map<K, V> m1, Map<K, V> m2) {
		
		if( m2!=null && m1!=null )	{
			
			Iterator<K> iter = m2.keySet().iterator();
			
			while( iter.hasNext() )	{
				
				K key = iter.next();
				V val = m2.get(key);
				
				if( m1.containsKey(key) == false )
					m1.put(key, val);
				
			}//end of loop
			
			return m1;
			
		}else	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Safe Union of maps failed " );
			return null;
		}
	}//end of method
	
}
