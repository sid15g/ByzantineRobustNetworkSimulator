package edu.umbc.bft.exception;

public class InvalidNodeIDException extends Exception {

	private static final long serialVersionUID = 6242248541255898691L;
	
	public InvalidNodeIDException(String msg) {
		super(msg);
	}//end of constructor
	
	public static InvalidNodeIDException invalidNodeId(int nid)	{
		return new InvalidNodeIDException(" Found Invalid Node to define Link : "+ nid);
	}

}
