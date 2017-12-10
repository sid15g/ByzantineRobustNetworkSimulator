package edu.umbc.bft.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;

import edu.umbc.bft.util.CoreUtils;
import edu.umbc.bft.util.LogValues;
import edu.umbc.bft.util.Logger;

public class Simulator {

	private static NetworkBuilder builder;
	private static NetworkCleaner cleaner;
	
	static	{
		Simulator.builder = new NetworkBuilder();
		Simulator.cleaner = new NetworkCleaner();
	}
	
	public static void main(String[] args) throws Exception		{
		Runtime.getRuntime().addShutdownHook(Simulator.cleaner);
		Simulator.configureLOG4J();
		CoreUtils.loadProperties();
		Simulator.load();
		//Simulator.simulate();
	}//End Of Main
	
	
	public static void load() {

		final int total = CoreUtils.getPropertiesAsInteger("total.switch.count");
		final int faulty = CoreUtils.getPropertiesAsInteger("switch.faulty.count");
		final int tnodes = CoreUtils.getPropertiesAsInteger("trusted.nodes.count");
		final int icount = CoreUtils.getPropertiesAsInteger("interface.count");
		final String comma = String.valueOf(',');
		
		boolean success = true;
		
		if( total<=0 || icount<=0 )	{
			Logger.sysLog(LogValues.info, Simulator.class.getName(), " No Network found... Check config.properties | Exiting application..." );
			return;
		}
		
		Logger.sysLog(LogValues.info, Simulator.class.getName(), " Building network... " );
		List<Thread> threads = Simulator.builder.createSwitches(total, tnodes, faulty);
		
		try {
			
			for( int i=1; i<=total; i++ )		{
				
				String nlinks = "links." + i;
				String temp = CoreUtils.getProperties(nlinks);
				String[] links = temp!=null?temp.trim().split(comma):null;
				
				if( links!=null && links.length==icount )	{
					if( Simulator.builder.connect(i, links) == false )	{
						Logger.sysLog(LogValues.fatal, Simulator.class.getName(), " Link Connection Error " );
						success = false;
						break;	
					}
				}else	{
					Logger.sysLog(LogValues.fatal, Simulator.class.getName(), " Error connecting Node"+ i +" | Links information not found | Exiting application... " );
					success = false;
					break;
				}
			}//end of loop

		}catch(Exception e) {
			Logger.sysLog(LogValues.fatal, Simulator.class.getName(), Logger.getStack(e) );
			Logger.sysLog(LogValues.info, Simulator.class.getName(), " Exiting application... " );
			success = false;
		}//end of try catch
		
		
		if( success )		{
			Logger.sysLog(LogValues.info, Simulator.class.getName(), " Network setup complete... " );
			Simulator.builder.dump();
			Simulator.cleaner.setSwitches(threads);
			Simulator.simulate(threads);
		}else	{
			Simulator.cleaner.start();
			System.gc();
		}
		
	}//End of method
	
	
	public static void simulate(List<Thread> threads) {
		
		Simulator.builder.initializeAndRunNodes(threads);
		//TODO
		
	}//End of method
	
	
	private static void configureLOG4J()		{

		File log4j = new File("./resource/log4j.xml");

		if( log4j.exists() )			{
			DOMConfigurator.configure("./resource/log4j.xml");
		}else			{
			try		{
				int bit=0;
				InputStream is = Simulator.class.getClassLoader().getResourceAsStream("log4j.xml");

				String path = System.getProperty("user.dir");
				File tempLog4j = new File(path, "resource");
				tempLog4j.mkdirs();
				tempLog4j.setReadable(true, false);
				tempLog4j.setWritable(true, false);

				path += (File.separator +"resource"+ File.separator);
				tempLog4j = new File(path, "log4j.xml");
				tempLog4j.createNewFile();

				FileOutputStream fos = new FileOutputStream(tempLog4j);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				do		{
					bit = is.read();
					if( bit == -1 )		{
						break;
					}
					bos.write(bit);
				}while( bit != -1 );

				is.close();
				bos.close();
				DOMConfigurator.configure("./resource/log4j.xml");

			}catch(Exception e)	{
				System.err.println(" ERROR - Initalizing LOG4J configuration!! ==> "+ e.getMessage());
			}//End Of Try Catch
		}//End Of IF Else		

	}//End Of Method
	
}
