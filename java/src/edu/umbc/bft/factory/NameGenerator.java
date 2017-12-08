package edu.umbc.bft.factory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
	
	private static Random rand;
	private static Set<String> set;
	
	static {
		NameGenerator.set = new HashSet<String>(64);
		NameGenerator.rand = new Random();
		NameGenerator.set.add("a");
	}//End Of Block
	
	
	public static String generate()	{
		
		String str = "a";
		
		while( set.contains(str) != false )	{
			int temp = NameGenerator.rand.nextInt(99999) + 100000;
			str = Integer.toHexString(temp).toUpperCase().trim();
		}
		
		NameGenerator.set.add(str);
		return str;
		
	}//End Of Method
	
	public static String assignMACAddress() 		{
		
		String str = "a";
		
		while( set.contains(str) != false )	{
			str = NameGenerator.getMAC();
		}
		
		NameGenerator.set.add(str);
		return str;
		
	}//End of Method
	
	private static String getMAC() 		{
	    
	    byte[] macAddr = new byte[6];
	    rand.nextBytes(macAddr);

	    macAddr[0] = (byte)(macAddr[0] & (byte)254);

	    StringBuilder sb = new StringBuilder(18);
	    
	    for(byte b: macAddr)	{
	    	
	        if(sb.length() > 0)
	            sb.append(":");

	        sb.append(String.format("%02x", b));
	        
	    }//End of loop
	    
	    return sb.toString().trim();
	    
	}//End of Method
	
}
