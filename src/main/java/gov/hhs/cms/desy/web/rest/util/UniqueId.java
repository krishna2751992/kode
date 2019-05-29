/**
 * 
 */
package gov.hhs.cms.desy.web.rest.util;

import java.rmi.server.UID;

/**
 * @author Jagannathan.Narashim
 *
 */
public class UniqueId {
	public static String getUniqueId()
	{
		return new UID().toString();
	}
}
