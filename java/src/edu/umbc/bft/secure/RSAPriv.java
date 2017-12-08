package edu.umbc.bft.secure;

import java.math.BigInteger;

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
	
	public final String decrypt(String str)	{
		BigInteger inp = new BigInteger(str.toLowerCase(), 24);
		BigInteger message = this.key.decrypt(inp);
		return new String(message.toByteArray());
	}
	
	public final String encrypt(String str)	{
		return this.pubKey.encrypt(str);
	}
	
	public String sign(String msg)	{
		BigInteger inp = new BigInteger(msg.getBytes());
		BigInteger cipher = this.key.decrypt(inp);
		return cipher.toString(32).toUpperCase().trim();
	}
	
	public boolean verify(String signature, String message) {
		return this.pubKey.verify(signature, message);
	}

}
