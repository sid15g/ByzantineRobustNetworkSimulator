package edu.umbc.bft.net.nodes.abs;

import java.util.Map;

import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.net.packet.payload.PublicKeyList;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.CoreUtils;

public abstract class AbstractTrustedNodeImpl extends AbstractSwitchImpl	{

	private Map<String, RSAPub> publicKeyList;
	
	public AbstractTrustedNodeImpl(Map<String, RSAPub> pkl) {
		this.publicKeyList = pkl;
	}//End of constructor
	
	protected Map<String, RSAPub> getPublicKeyList() {
		return this.publicKeyList;
	}
	protected String getPublicKeyListAsJson() {
		return CoreUtils.GSON.toJson(this.publicKeyList);
	}
	
	protected boolean addPublicKey(String nodeId, RSAPub key)	{
		if( key!=null && nodeId!=null )	{
			this.publicKeyList.put(nodeId, key);
			return true;
		}else
			return false;
	}
	
	protected Packet createPublicKeyListMessage()	{
		PublicKeyList pm = new PublicKeyList(this.publicKeyList);
		pm.addSignature(super.getPrivateKey());
		Packet p = super.manager.createPacket(pm);
		return p;
	}
	
}
