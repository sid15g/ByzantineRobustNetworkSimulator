package edu.umbc.bft.secure;

import java.math.BigInteger;

public class KeyStore		{

	private RSAPriv priv;
	
	private KeyStore()	{
		RSA r = new RSA(1024);
		this.priv = new RSAPriv(r);
	}//end of constructor
	
	public static synchronized RSAPriv getNewKey() {
		return new KeyStore().priv;
	}//end of method
	
	public static String toString(BigInteger value, int size)	{
		String str = value.toString();
		
		while( str.length() < size )	{
			str = "0"+ str;
		}
		
		return str.toUpperCase();
	}
	
}
