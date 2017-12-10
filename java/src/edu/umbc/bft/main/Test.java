package edu.umbc.bft.main;

import java.util.HashSet;
import java.util.Set;

import edu.umbc.bft.exception.IPNotAvailableException;
import edu.umbc.bft.factory.IPFactory;
import edu.umbc.bft.factory.LinkFactory;
import edu.umbc.bft.factory.NameGenerator;
import edu.umbc.bft.net.bean.ForwardingTable;
import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.conn.Eth;
import edu.umbc.bft.net.conn.HalfDuplexLink;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.conn.Link;
import edu.umbc.bft.net.packet.Payload;
import edu.umbc.bft.net.packet.payload.Identification;
import edu.umbc.bft.secure.KeyStore;
import edu.umbc.bft.secure.RSAPriv;
import edu.umbc.bft.secure.RSAPub;

public class Test {

	public static void main(String[] args) throws Exception	{
		//Test.testNames();
		//Test.testLinkFactory();
		//Test.testForwardingTable();
		//Test.testIPFactory();
		Test.testKey();
		//Test.testPayloads();
	}//End of main
	
	public static void testNames() {
		
		Set<String> s= new HashSet<String>();
		
		for( int i=0; i<65536; i++ )	{
			String str = NameGenerator.assignMACAddress();
			s.add(str);
			//System.out.println(str);
		}
		
		System.out.println(s.size());
	}//End of method

	
	public static void testForwardingTable() throws Exception {
		
		ForwardingTable ft = new ForwardingTable();
		
		Interface i1 = new Eth(new byte[]{10,20,15,30});
		IPAddress src = new IPAddress(new byte[]{10,20,15,10});
		IPAddress dest1 = new IPAddress(new byte[]{10,20,15,50});
		ft.addEntry(src, dest1, i1);
		
		Interface i2 = new Eth(new byte[]{10,20,15,25});
		IPAddress dest2 = new IPAddress(new byte[]{10,20,15,60});
		ft.addEntry(src, dest2, i2);
				
		System.out.println( i1.equals(i2) );
		
		IPAddress dest3 = new IPAddress(new byte[]{10,20,15,60});
		System.out.println( ft.contains(src, dest3) );
		
	}//End of method
	
	public static void testLinkFactory() throws Exception {
		
		Link l1 = LinkFactory.create(new byte[]{10,10,10,10}, new byte[]{8,8,8,8}, true);
		Link l2 = LinkFactory.create(new byte[]{9,9,9,9}, l1.getInterfaces()[1], false);
		System.out.println(l1.tostring());
		System.out.println(l2.tostring());
		
		HalfDuplexLink l = ((HalfDuplexLink)l2);
		l.setBandwidth(80);
		l.setDropRate(0.05);
		
		//System.out.println(l1.compareState(l2));
		System.out.println( l1.equals(l2) );
		
		Link l3 = LinkFactory.create(new byte[]{10,10,10,10}, new byte[]{8,8,8,8}, false);
		System.out.println( l1.equals(l3) );
		
	}//end of method
	
	public static void testIPFactory() throws IPNotAvailableException	{
		
		IPFactory fact = new IPFactory();
		
		IPAddress ip1 = fact.createIP();
		System.out.println(ip1);
		
		IPAddress ip2 = fact.createIP();
		System.out.println(ip2);
		
		System.out.println(ip1.equals(ip2));
		
	}//end of method
	
	
	public static void testKey() {
		
		RSAPriv priv = KeyStore.getNewKey();
		RSAPub pub = priv.getPublicKey();
		
		Identification i = new Identification("S2");
		i.addSignature(priv);
		Payload p = i;
		String m = p.toString();
		System.out.println(m.length());
		System.out.println(m);
		String c = pub.encrypt(m);
		System.out.println(c);
		String dm = priv.decrypt(c);
		System.out.println(dm);
		System.out.println("===");
		c = priv.sign(m);
		System.out.println(c);
		System.out.println( pub.verify(c, m) );

		RSAPriv priv2 = KeyStore.getNewKey();
		System.out.println(pub.toString());
		System.out.println(priv2.getPublicKey().toString());
		
	}//end of method
	
	
	public static void testPayloads() throws Exception {
		
		Payload p = new Identification("S5");
		System.out.println(p.toString());
	}
}
