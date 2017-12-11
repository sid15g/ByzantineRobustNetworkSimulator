package edu.umbc.bft.secure;

import java.io.Serializable;
import java.math.BigInteger;

import edu.umbc.bft.util.StringIterator;

public final class RSAPub implements Serializable	{
	
	private static final long serialVersionUID = -5268625144797772565L;
	
	private RSA key;
	
	RSAPub(RSA r) {
		this.key = new RSA(r);
	}//end of constructor
	
	public final String encrypt(String str)	{

		StringIterator iter = new StringIterator(str);
		StringBuilder buf = new StringBuilder();
		
		while( iter.hasNext() )	{
			BigInteger inp = new BigInteger(iter.next().getBytes());
			BigInteger cipher = this.key.encrypt(inp);
			String t = KeyStore.toString(cipher, 310);
			//System.out.println("e"+ cipher.toString().length() +"->"+ t.length() );
			buf.append( t );
		}
		return buf.toString();
	}
	

	public boolean verify(String signature, String message)		{
		StringIterator iter = new StringIterator(signature, 310);
		StringBuilder buf = new StringBuilder();
		
		while( iter.hasNext() )	{
			BigInteger inp = new BigInteger(iter.next().toLowerCase());
			BigInteger temp = this.key.encrypt(inp);
			String t = new String(temp.toByteArray());
			//System.out.println("v"+ temp.toString().length() +"---"+ t.length() +"--"+ t );
			buf.append( t );
		}
		return buf.toString().equals(message);
	}
	
	@Override
	public boolean equals(Object obj)	{
		if( obj instanceof RSAPub )	{
			RSAPub key = (RSAPub)obj;
			return key.toString().equals(this.toString());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.key.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}//end of class
