/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

/**
 * @author Jagannathan.Narashim
 *
 */
public class Db2Crud {


	public static final char CREATE = 'C';
	public static final char READ = 'R';
	public static final char UPDATE = 'U';
	public static final char DELETE = 'D';
	public static final int MAX_SEARCH_RESULT = 300;

	public static String replaceSpaceWithTilda(String search)
	{
		if (search != null)
			return search.replace(' ', '~');
		else
			return null;
	}

}
