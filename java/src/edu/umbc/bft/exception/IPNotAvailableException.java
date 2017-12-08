package edu.umbc.bft.exception;


public class IPNotAvailableException extends Exception {
	
	private static final long serialVersionUID = 2143570533296735581L;

	protected IPNotAvailableException(String msg) {
		super(msg);
	}//End Of Constructor
	
	
	public static final IPNotAvailableException exhausted()	{
		return new IPNotAvailableException(" All the IPAddress of the subnet has been already used | No IP available. " );
	}//End Of Method
	
}
