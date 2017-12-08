package edu.umbc.bft.net.nodes.abs;

import java.util.Set;

import edu.umbc.bft.secure.RSAPub;

public abstract class AbstractTrustedNodeImpl extends AbstractSwitchImpl	{

	private Set<RSAPub> publicKeyList;
	
	public AbstractTrustedNodeImpl(Set<RSAPub> pkl) {
		this.publicKeyList = pkl;
	}//End of constructor
	
	protected Set<RSAPub> getPublicKeyList() {
		return this.publicKeyList;
	}
	
	protected boolean addPublicKey(RSAPub key)	{
		if( key!=null )
			return this.publicKeyList.add(key);
		else
			return false;
	}
	
}
