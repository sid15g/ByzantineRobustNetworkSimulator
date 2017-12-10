package edu.umbc.bft.net.packet.payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.secure.RSAPub;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class PublickKeyList implements Payload {

	private Map<String, RSAPub> keys;
	
	public PublickKeyList(Map<String, RSAPub> pkl) {
		this.keys = pkl;
	}
	
	public Map<String, RSAPub> getPublicKeyList() {
		return this.keys;
	}
	
	@Override
	public byte[] toByteArray() {
		try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();)	{
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this.keys);
			return byteOut.toByteArray();
		}catch(IOException e)	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Unable to convert PKL to byte array. \n"+ Logger.getStack(e) );
			return new byte[0];
		}
	}
	
	@Override
	public String toString() {
		return new String(this.toByteArray()).trim();
	}

}
