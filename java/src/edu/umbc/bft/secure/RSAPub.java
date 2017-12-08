package edu.umbc.bft.secure;

import java.math.BigInteger;

public final class RSAPub	{
	
	private RSA key;
	
	RSAPub(RSA r) {
		this.key = r;
	}//end of constructor
	
	public final String encrypt(String str)	{
		BigInteger inp = new BigInteger(str.getBytes());
		BigInteger cipher = this.key.encrypt(inp);
		return cipher.toString(24).toUpperCase().trim();
	}
	

	public boolean verify(String signature, String message) {
		BigInteger inp = new BigInteger(signature.toLowerCase(), 32);
		BigInteger temp = this.key.encrypt(inp);
		String plaintext = new String(temp.toByteArray());
		return plaintext.equals(message);
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
