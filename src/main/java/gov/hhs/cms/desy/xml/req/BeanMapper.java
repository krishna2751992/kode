package gov.hhs.cms.desy.xml.req;

import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * This class represents all the mappings specified in db-mappings.xml file.
 * This is a singleton and will be created only once per JVM when accessed the
 * first time.
 * 
 * @author Jagannathan.Narashim
 * @version 1.0
 */
public class BeanMapper {



	public static final String BEAN_TAG = "BEAN";
	public static final String CLASS_NAME_TAG = "CLASS-NAME";
	public static final String DB_TABLE_TAG = "DB-TABLE";
	public static final String DB_TS_TAG = "DB-TS";
	public static final String TABLE_NAME_TAG = "TABLE-NAME";

	public static final String MEMBER_TAG = "MEMBER";
	public static final String COLUMN_TAG = "DB-COLUMN";
	public static final String ACCESSOR_TAG = "ACCESSOR";
	public static final String FORMAT_TAG = "FORMAT";
	public static final String TYPE_TAG = "TYPE";

	public static final String TYPE_CHAR = "CHAR";
	public static final String TYPE_INT = "INTEGER";
	public static final String TYPE_DATE = "DATE";
	public static final String TYPE_TEXT = "TEXT";

	private static BeanMapper instance = null;

	private Document doc = null;

	private BeanMapper() throws Exception
	{

		try
		{
			doc = new SAXBuilder().build(getClass().getClassLoader()
				.getResourceAsStream("db-mappings.xml"));
		}
		catch (Exception e)
		{
			throw new Exception(e);
		}

	}

	public String getXML()
	{
		return new XMLOutputter(Format.getPrettyFormat()).outputString(doc
			.getRootElement());
	}

	public static BeanMapper getInstance() throws Exception
	{
		if (instance != null)
			return instance;
		else
			return (instance = new BeanMapper());
	}

	/**
	 * This method gets the mapping for a particular file. A domain object
	 * implemeting XMLBean calls this method to get the mapping between the
	 * class that it's calling this method from and the database table. This
	 * mapping basically drives how the EVENT element will generated to
	 * represent a particular bean. The return value is an JDOM Element
	 */
	public Element getMapping(String className)
	{
		Element ele = null;
		Element retEle = null;
		Iterator iter = doc.getRootElement().getChildren(BeanMapper.BEAN_TAG)
			.iterator();
		if (iter != null)
		{
			while (iter.hasNext())
			{
				ele = (Element) iter.next();
				if (ele.getChildTextTrim(BeanMapper.CLASS_NAME_TAG) != null
					&& ele.getChildTextTrim(BeanMapper.CLASS_NAME_TAG).equals(
						className))
				{
					retEle = ele;
					break;
				}
			}
		}
		return retEle;
	}

	/**
	 * This method gets list of members for given class.
	 */
	public List getMembers(String className)
	{
		Element ele = getMapping(className);
		if (ele != null)
		{
			return ele.getChildren(BeanMapper.MEMBER_TAG);
		}
		else
		{
			return null;
		}
	}

	public String getTableName(String className)
	{
		Element ele = getMapping(className);
		if (ele != null)
		{
			return ele.getChildText(BeanMapper.DB_TABLE_TAG);
		}
		else
		{
			return null;
		}
	}


}
