package edu.umbc.bft.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;


public class Logger 		{

	public static void sysLog(int severity, String applicationName, String message)    {

		try		{

			org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(applicationName);

			switch (severity) 			{       	    
				case LogValues.fatal:
					log.fatal(message); 
					break;
				case LogValues.hist:
					log.log(MyLevel.HIST, message);
					break;						
				case LogValues.error:
					log.error(message); 
					break;
				case LogValues.warn: 
					log.warn(message); 
					break;
				case LogValues.imp:
					log.log(MyLevel.IMP, message);
					break;	
				case LogValues.info:
					log.info(message); 
					break;
				case LogValues.debug:
					log.debug(message); 
					break; 
				case LogValues.trace:	
					log.trace(message);
					break;
				default:
					log.info(message);
			}

		}catch( Throwable e )		{
			System.err.println("jHub: Error doing log severity: "+severity+" Application: "+applicationName+" Message: "+message );
			e.printStackTrace();
		}//End Of TryCatch

	}//End Of Method


	public static String getStack( Throwable E )		{

		String result = "There was some error creating the Stack Frame";

		try        {

			ByteArrayOutputStream Stack = new ByteArrayOutputStream();
			PrintStream PrintStack = null;

			if( Stack != null )		{
				PrintStack = new PrintStream( Stack );

				if( PrintStack != null )				{
					E.printStackTrace( PrintStack );
					result = Stack.toString();
				}

			}

		}catch( Exception e )        {
			result += ( "\r\n\t"+ e.toString() );
		}

		return "\r\n\t"+result.trim();

	}//End Of Method


	public static void setLogLevel(int level)		{
		
		try		{
			switch(level)		{
				case LogValues.trace:
					setLogLevel(Level.TRACE);
					break;					
				case LogValues.debug:
					setLogLevel(Level.DEBUG);
					break;
				case LogValues.info:
					setLogLevel(Level.INFO);
					break;
				case LogValues.imp:
					setLogLevel(MyLevel.IMP);
					break;					
				case LogValues.warn:
					setLogLevel(Level.WARN);
					break;
				case LogValues.error:
					setLogLevel(Level.ERROR);
					break;
				case LogValues.hist:
					setLogLevel(MyLevel.HIST);
					break;						
				case LogValues.fatal:
					setLogLevel(Level.FATAL);
					break;	
			}			
		}catch (Exception e) 	{
			System.err.println("Error while change log level: "+level);
		}//End Of Try Catch		 
	}//End Of Method

	
	private static void setLogLevel(Level level)		{
		
		LogManager.getRootLogger().setLevel(level);
		
		@SuppressWarnings("unchecked")
		Enumeration<Category> allLoggers = LogManager.getRootLogger().getLoggerRepository().getCurrentCategories();
		
		while (allLoggers.hasMoreElements())		{
			Category category = (Category) allLoggers.nextElement();
			category.setLevel(level);			
		}//End Of Loop
		
	}//End Of Method

	
}//End Of Class


final class MyLevel extends Level 	{
    
	private static final long serialVersionUID = 2751778514843177729L;
	private static final int IMP_INT = (INFO_INT+WARN_INT)/2;				/** In between INFO and WARN */
	private static final int HIST_INT = (FATAL_INT+ERROR_INT)/2;			/** In between FATAL and ERROR */
	
	public static final Level IMP = new MyLevel(IMP_INT, "IMP", 5);
	public static final Level HIST = new MyLevel(HIST_INT, "HIST", 1);
	

	private MyLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}//End Of Constructor
	
}//End Of Class
