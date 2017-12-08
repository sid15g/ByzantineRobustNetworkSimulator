package edu.umbc.bft.secure;

public class KeyStore		{

	private RSAPriv priv;
	
	private KeyStore()	{
		RSA r = new RSA(1024);
		this.priv = new RSAPriv(r);
	}//end of constructor
	
	public static synchronized RSAPriv getNewKey() {
		return new KeyStore().priv;
	}//end of method
	
}
