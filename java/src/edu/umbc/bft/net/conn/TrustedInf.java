package edu.umbc.bft.net.conn;

import edu.umbc.bft.net.bean.IPAddress;

public class TrustedInf extends Eth	{

	public TrustedInf(byte[] ip) {
		super(ip);
	}//end of constructor
	
	public TrustedInf(IPAddress ip) {
		super(ip);
	}//end of constructor

}