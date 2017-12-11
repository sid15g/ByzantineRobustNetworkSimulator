package edu.umbc.bft.secure;

import java.math.BigInteger;

import edu.umbc.bft.util.StringIterator;

public class RSAPriv		{
	
	private RSA key;
	private RSAPub pubKey;
	
	RSAPriv(RSA r) {
		this.key = r;
		this.pubKey = new RSAPub(r);
	}//end of constructor

	public RSAPub getPublicKey() {
		return this.pubKey;
	}
	
	public final String decrypt(String str)		{
		StringIterator iter = new StringIterator(str, 310);
		StringBuilder buf = new StringBuilder();
		
		while( iter.hasNext() )	{
			BigInteger inp = new BigInteger(iter.next().toLowerCase());
			BigInteger message = this.key.decrypt(inp);
			String t = new String(message.toByteArray());
			//System.out.println("d"+ t.length() );
			buf.append( t );			
		}
		return buf.toString();
	}
	
	public final String encrypt(String str)	{
		return this.pubKey.encrypt(str);
	}
	
	public String sign(String msg)	{
		StringIterator iter = new StringIterator(msg);
		StringBuilder buf = new StringBuilder();
		
		while( iter.hasNext() )	{
			BigInteger inp = new BigInteger(iter.next().getBytes());
			BigInteger cipher = this.key.decrypt(inp);
			String t = KeyStore.toString(cipher, 310);
			//System.out.println("s"+ cipher.toString().length() +"->"+ t.length() );
			buf.append( t );
		}
		return buf.toString();
	}
	
	public boolean verify(String signature, String message) {
		return this.pubKey.verify(signature, message);
	}
	
}
