package edu.umbc.bft.net.nodes;

import edu.umbc.bft.net.bean.IPAddress;
import edu.umbc.bft.net.conn.Interface;
import edu.umbc.bft.net.packet.Packet;

public interface Switch	extends Node	{
	
	public void forward(Interface src, Packet e);
	public void addRoute(IPAddress src, IPAddress dest, Interface i);
	public void deleteRoute(IPAddress src, IPAddress dest, Interface i);
	
}