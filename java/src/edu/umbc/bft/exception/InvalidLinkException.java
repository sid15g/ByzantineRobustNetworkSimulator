package edu.umbc.bft.exception;

public class InvalidLinkException extends Exception {

	private static final long serialVersionUID = -5592518068166553709L;
	
	public InvalidLinkException(String msg) {
		super(msg);
	}//end of constructor
	
	public static InvalidLinkException invalidLink(int src, int dest)	{
		return new InvalidLinkException(" Found Invalid Link defination :: "+ src +"<--x-->"+ dest );
	}
}
