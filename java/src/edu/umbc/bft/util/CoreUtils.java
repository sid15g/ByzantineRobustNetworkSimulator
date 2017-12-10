package edu.umbc.bft.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.gson.Gson;

public class CoreUtils	{
	
	private static Properties prop = null;
	public static Gson GSON;
	
	
	static{
		CoreUtils.GSON = new Gson();
	}
	
	public static boolean loadProperties() {	
		CoreUtils.prop = new Properties();
		
		try( InputStream stream = CoreUtils.class.getClassLoader().getResourceAsStream("config.properties") ) {
			CoreUtils.prop.load(stream);
			return true;
		}catch(IOException e)	{
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " Error loading properties: \n"+ Logger.getStack(e) );
		}//end of try catch
		
		return false;
	}//End of method
	
	
	public static String getProperties(String key) {
		if( key!=null && key.length()>0 )	{
			return CoreUtils.prop.getProperty(key.trim());
		}else
			return null;
	}//End of method
	
	public static Integer getPropertiesAsInteger(String key) {
		
		if( key!=null && key.length()>0 )	{
			String value = CoreUtils.prop.getProperty(key.trim());
			try {
				return Integer.parseInt(value);	
			}catch(Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " Unable to parse property as interger : "+ key );
				return 0;
			}
		}else
			return 0;
		
	}//End of method

}
