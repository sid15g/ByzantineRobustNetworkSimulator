package edu.umbc.bft.net.packet.impl;

import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;

public abstract class PayloadWithKey implements Payload	{
	
	protected String signature;
	protected RSAPub key;
	
	protected PayloadWithKey() {
		this.signature = null;
		this.key = null;
	}//end of constructor
	

	public final void addSignature(RSAPriv key)	{
		this.key = key.getPublicKey();
		this.signature = key.sign(this.toString());
	}//end of method
	
	
	/** Verify that the content has not been changed during packet routing */
	public final boolean verifySignature() {
		return this.key.verify(this.signature, this.toString());
	}
	
	/** 
	 * Verify the source from the PKL provided by T.N.
	 * Verify T.N. PKL from the keys node already have in initial configuration
	 **/
	public final boolean verifySignature(RSAPub key) {
		return key.verify(this.signature, this.toString());
	}
	
	public String getSignature() {
		return this.signature;
	}
	public RSAPub getKey() {
		return this.key;
	}
	
	@Override
	public String toString() {
		return new String(this.toByteArray());
	}
	
}