package edu.umbc.bft.net.packet.payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import edu.umbc.bft.net.packet.impl.PayloadWithKey;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class PublicKeyList extends PayloadWithKey {

	private Map<String, RSAPub> keys;
	
	public PublicKeyList(Map<String, RSAPub> pkl) {
		this.keys = pkl;
	}//end of constructor
	
	public void setKeys(Map<String, RSAPub> keys) {
		this.keys = keys;
	}
	public Map<String, RSAPub> getPublicKeyList() {
		return this.keys;
	}
	
	@Override
	public byte[] toByteArray() {
		try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();)	{
			Iterator<String> iter = this.keys.keySet().iterator();
			while( iter.hasNext() )	{
				String key = iter.next();
				String value = this.keys.get(key).toString();
				byteOut.write(key.getBytes());
				byteOut.write(value.getBytes());
			}
			return byteOut.toByteArray();
		}catch(IOException e)	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Unable to convert PKL to byte array. \n"+ Logger.getStack(e) );
			return new byte[0];
		}
	}
	
	@Override
	public String toString() {
		return new String(this.toByteArray());
	}

}
