package edu.umbc.bft.net.conn;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import edu.umbc.bft.net.packet.Packet;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class MessageStream extends LinkedBlockingQueue<Packet> {

	private static final long serialVersionUID = 8105349172051746667L;

	private double dropRate;
	private Random rand;
	
	public MessageStream() {
		this(128);
	}//End of constructor
	
	public MessageStream(int capacity) {
		this(capacity, 0.03);				// 3%
	}//End of constructor
	
	public MessageStream(int capacity, double dropRate) {
		super(capacity);
		this.rand = new Random();
		this.dropRate = dropRate;
	}//End of constructor
	
	
	@Override
	public final boolean add(Packet e) {
		if( rand.nextDouble() >= this.dropRate )	{
			return super.add(e);
		}else	{
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Network error: Packet dropped ");
			return false;
		}
	}	
	@Override
	public final Packet poll() {
		return super.poll();
	}
	public final double getDropRate() {
		return this.dropRate;
	}
	
}