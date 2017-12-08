package edu.umbc.bft.exception;

import edu.umbc.bft.net.nodes.Switch;

public class LinkCreationException extends Exception {
	
	private static final long serialVersionUID = 4748283610459373970L;
	
	protected LinkCreationException(String msg) {
		super(msg);
	}//End Of Constructor
	
	
	public static final LinkCreationException alreadyBusy(Switch s)	{
		return new LinkCreationException(" Link already in use. | Dest: "+ s.getName() );
	}//End Of Method

	public static final LinkCreationException invalidBinding()	{
		return new LinkCreationException(" Link Binding Failed : Invalid link details ");
	}//End Of Method
	
}
