package edu.umbc.bft.main;

import java.util.List;

import edu.umbc.bft.net.nodes.Switch;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class NetworkCleaner extends Thread	{

	private List<Thread> switches;
	
	public void setSwitches(List<Thread> switches) {
		this.switches = switches;
	}
	
	@Override
	public void run() {	

		Logger.sysLog(LogValues.info, this.getClass().getName(), " Cleaning Network setup... " );

		for( int i=0; this.switches!=null && i<this.switches.size(); i++)	{
			Switch s = (Switch)this.switches.get(i);
			s.shutdown();
		}

	}//end of thread

}
