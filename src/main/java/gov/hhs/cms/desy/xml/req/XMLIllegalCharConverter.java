/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XMLIllegalCharConverter {


	private static Hashtable XMLEntityLookupTable;
	private static Hashtable XMLEntityLookupRevTable;
	private static Hashtable nonXMLEntityLookupTable;
	private static String charToXMLtemp = null;
	private static String xmlToChartemp = null;

	/**
	 * Construtor with the following parametes.
	 */
	public XMLIllegalCharConverter()
	{
		super();
	}

	static
	{
		XMLEntityLookupTable = new Hashtable();

		XMLEntityLookupTable.put("&", "~AMP");
		XMLEntityLookupTable.put("<", "~LTH");
		XMLEntityLookupTable.put(">", "~GTH");
		XMLEntityLookupTable.put("'", "~APO");
		XMLEntityLookupTable.put("\"", "~QUO");

		nonXMLEntityLookupTable = new Hashtable();

		nonXMLEntityLookupTable.put("&amp;", "&");
		nonXMLEntityLookupTable.put("&lt;", "<");
		nonXMLEntityLookupTable.put("&gt;", ">");
		nonXMLEntityLookupTable.put("&apos;", "'");
		nonXMLEntityLookupTable.put("&quot;", "\"");
		
		XMLEntityLookupRevTable = new Hashtable();

		XMLEntityLookupRevTable.put("~AMP","&");
		XMLEntityLookupRevTable.put("~LTH","<");
		XMLEntityLookupRevTable.put("~GTH",">");
		XMLEntityLookupRevTable.put("~APO","'");
		XMLEntityLookupRevTable.put("~QUO","\"");
		
		
	}

	/**
	 * This replaces the XML illegal characters with backend defined delimiters
	 * in the input string object.
	 * 
	 * @param str - The string to be replaced.
	 * @return String - Updated input string with non XML illegal Characters
	 */
	public static String replaceIllegalCharacters(String str)
	{
		// get the keys:
		Enumeration illegalChars = XMLEntityLookupTable.keys();
		charToXMLtemp = str;

		// check for null
		if (str != null && str.length() != 0)
		{
			while (illegalChars.hasMoreElements())
			{
				String illegalChar = (String) illegalChars.nextElement();
				String entityReplacement = (String) XMLEntityLookupTable
						.get(illegalChar);
				charToXMLtemp = replace(charToXMLtemp, illegalChar,
						entityReplacement);

			} // end of while (illegalChars.hasMoreElements()) ...
		}

		return charToXMLtemp;
	}



	public static String replaceIllegalCharactersRev(String str)
		{
			// get the keys:
			Enumeration xmlChars = XMLEntityLookupRevTable.keys();
			xmlToChartemp = str;

			// check for null
			if (str != null && str.length() != 0)
			{
				while (xmlChars.hasMoreElements())
				{
					String xmlChar = (String) xmlChars.nextElement();
					String entityReplacement = (String) XMLEntityLookupRevTable
							.get(xmlChar);
					xmlToChartemp = replace(xmlToChartemp, xmlChar,
							entityReplacement);

				} // end of while (xmlChars.hasMoreElements()) ...
			}

			return xmlToChartemp;
		}
		
		
	/**
	 * This replaces the XML illegal characters representation strings to XML
	 * illegal characters in the input string object.
	 * 
	 * @param str - The string to be replaced.
	 * @return String - Updated input string with non XML illegal Characters
	 */
	public static String replaceXMLToCharacters(String str)
	{
		// get the keys:
		Enumeration xmlChars = nonXMLEntityLookupTable.keys();
		xmlToChartemp = str;

		// check for null
		if (str != null && str.length() != 0)
		{
			while (xmlChars.hasMoreElements())
			{
				String xmlChar = (String) xmlChars.nextElement();
				String entityReplacement = (String) nonXMLEntityLookupTable
						.get(xmlChar);
				xmlToChartemp = replace(xmlToChartemp, xmlChar,
						entityReplacement);

			} // end of while (xmlChars.hasMoreElements()) ...
		}

		return xmlToChartemp;
	}

	/**
	 * This method replace all occurances of a pattern of characters with the
	 * replaced string in the input string.
	 * 
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return String
	 */
	private static String replace(String str, String pattern, String replace)
	{
		int s = 0;
		int e = 0;

		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0)
		{
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}


}
