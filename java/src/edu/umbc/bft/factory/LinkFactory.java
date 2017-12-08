package edu.umbc.bft.factory;

import edu.umbc.bft.net.conn.Eth;
import edu.umbc.bft.net.conn.FullDuplexLink;
import edu.umbc.bft.net.conn.HalfDuplexLink;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.Link;

public class LinkFactory	{

	public static Link create(byte[] ip1, Interface ip2, boolean fullDuplex)	{
		Interface i = new Eth(ip1);
		return LinkFactory.create(i, ip2, fullDuplex);
	}
	
	public static Link create(Interface ip1, byte[] ip2, boolean fullDuplex)	{
		Interface i = new Eth(ip2);
		return LinkFactory.create(ip1, i, fullDuplex);
	}
	
	public static Link create(byte[] ip1, byte[] ip2, boolean fullDuplex)	{
		Interface i1 = new Eth(ip1);
		Interface i2 = new Eth(ip2);
		return LinkFactory.create(i1, i2, fullDuplex);
	}
	
	public static Link create(Interface i1, Interface i2, boolean fullDuplex)	{
		Link l = null;
		if( fullDuplex )	{
			l = new FullDuplexLink(i1, i2);
		}else	{
			l = new HalfDuplexLink(i1, i2);
		}
		i1.attachLink(l);
		i2.attachLink(l);
		return l;
	}//end of method
	
}