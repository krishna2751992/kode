/**
 * 
 */
package gov.hhs.cms.desy.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DBStringConverter {
	private final Logger log = LoggerFactory.getLogger(DBStringConverter.class);

	public DBStringConverter()
	{
		
	}

	/**
	 * 
	 * @param dbValue
	 * @return
	 */
	public static String checkDBIntegerNull(String dbValue)
	{
		String defaultNonDB	= null;
		if(dbValue != null && dbValue.trim().length() > 0 && dbValue.trim().equals("-1"))
		{
			defaultNonDB	= null;
		}
		else
		{
			defaultNonDB	= dbValue;
		}
		
		return defaultNonDB;
	}
	
	
	/**
	 * 
	 * @param dbValue
	 * @return
	 */
	public static String checkDBCharNull(String dbValue)
	{
		String defaultNonDB	= null;
		if(dbValue != null && dbValue.trim().length() > 0 && dbValue.trim().equals("NUL"))
		{
			defaultNonDB	= null;
		}
		else
		{
			defaultNonDB	= dbValue;
		}
		
		return defaultNonDB;
	}
	
	public static String checkDBDateNull(String dbValue)
		{
			String defaultNonDB	= null;
			if(dbValue != null && dbValue.trim().length() > 0 && dbValue.trim().equals("1900-01-01-00.00.00.000000"))
			{
				defaultNonDB	= null;
			}
			else
			{
				defaultNonDB	= dbValue;
			}
		
			return defaultNonDB;
		}

}
