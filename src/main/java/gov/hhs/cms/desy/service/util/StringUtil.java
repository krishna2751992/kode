/**
 * 
 */
package gov.hhs.cms.desy.service.util;

/**
 * @author Jagannathan.Narashim
 *
 */
public class StringUtil {

	public static boolean isValidStr(String str) {
		boolean isValid = true;
		if(str==null || str.isEmpty() || "".equals(str.trim())) {
			
			isValid = false;
		}
		
		return isValid;
	}
}
