package edu.umbc.bft.net.bean;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.umbc.bft.net.bean.ForwardingTable.TableEntry;
import edu.umbc.bft.net.conn.Interface;

public class ForwardingTable implements Iterable<TableEntry>	{

	public final class TableEntry	{
		
		private IPAddress src, dest;
		
		private TableEntry(IPAddress src, IPAddress dest) {
			this.src = src;
			this.dest = dest;
		}//End of constructor
		
		public IPAddress getSrc() {
			return this.src;
		}
		public IPAddress getDest() {
			return this.dest;
		}

		@Override
		public boolean equals(Object obj)	{
			if( obj instanceof TableEntry )	{
				TableEntry entry = (TableEntry)obj;
				boolean ret = entry.src.equals(this.src);
				ret &= entry.dest.equals(this.dest);
				return ret;
			}else
				return false;
		}//End of method
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((dest == null) ? 0 : dest.hashCode());
			result = prime * result + ((src == null) ? 0 : src.hashCode());
			return result;
		}

		private ForwardingTable getOuterType() {
			return ForwardingTable.this;
		}
		
	}//End of Class
	
	private Map<TableEntry, Interface> table;
	
	public ForwardingTable() {
		this(16);
	}//End of constructor
	
	public ForwardingTable(int capacity) {
		this.table = new HashMap<TableEntry, Interface>(capacity);
	}//End of constructor

	
	public final boolean addEntry(byte[] src, byte[] dest, Interface dinf)	{
		return this.addEntry(new IPAddress(src), new IPAddress(dest), dinf);
	}//End of method
	
	public final boolean addEntry(InetAddress src, InetAddress dest, Interface dinf)	{
		return this.addEntry(new IPAddress(src), new IPAddress(dest), dinf);
	}//End of method
	
	public boolean addEntry(IPAddress src, IPAddress dest, Interface dinf) {
		
		if( src!=null && dest!=null )	{
			TableEntry entry = new TableEntry(src, dest);
			this.table.put(entry, dinf);
			return true;
		}else
			return false;
		
	}//End of method
	
	public boolean contains(IPAddress src, IPAddress dest) {
		if( src!=null && dest!=null )	{
			TableEntry entry = new TableEntry(src, dest);
			return this.table.containsKey(entry);
		}
		return false;
	}
	
	public Interface get(IPAddress src, IPAddress dest) {
		if( src!=null && dest!=null )	{
			TableEntry entry = new TableEntry(src, dest);
			return this.table.get(entry);
		}
		return null;
	}
	
	public Interface remove(IPAddress src, IPAddress dest)	{
		if( src!=null && dest!=null )	{
			TableEntry entry = new TableEntry(src, dest);
			return this.table.remove(entry);
			
		}
		return null;
	}
	
	@Override
	public Iterator<TableEntry> iterator()	{
		return this.table.keySet().iterator();
	}

}