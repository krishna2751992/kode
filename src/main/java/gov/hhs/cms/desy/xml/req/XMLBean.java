/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jdom2.CDATA;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jagannathan.Narashim
 *
 */
public class XMLBean {
	private final Logger log = LoggerFactory.getLogger(XMLBean.class);

	public static final String DB2_DEFAULT_FOR_BOOLEAN = "N";
	public static final String DB2_DEFAULT_FOR_INT = "-1";
	public static final String DB2_DEFAULT_FOR_CHAR = "NUL";
	public static final String DB2_DEFAULT_FOR_DATE = "1900-01-01";

	/**
	 * This method is used for all the CUD operation to generate XML message
	 * @return
	 * @throws DesyXMLException
	 */
	public ArrayList getXMLElements() throws Exception
	{
		BeanMapper mapper = null;
		ArrayList list = new ArrayList();

		mapper = BeanMapper.getInstance();
		Element mapping = mapper.getMapping(getClass().getName());
		if (mapping == null)
		{
			return null;
		}

		Iterator tableIter = mapping.getChildren(BeanMapper.DB_TABLE_TAG)
			.iterator();
		while (tableIter.hasNext())
		{
			Element currTab = (Element) tableIter.next();
			Element eventEle = new Element(XmlMsgConst.EVENT_TAG);
			eventEle.addContent(new Element(XmlMsgConst.PN_TAG).setText(currTab
				.getChildTextTrim(BeanMapper.DB_TS_TAG)));

			//adding columns
			Iterator iter = currTab.getChildren(BeanMapper.MEMBER_TAG)
				.iterator();
			Element field = null;
			while (iter.hasNext())
			{
				Element memberEle = (Element) iter.next();
				if (memberEle.getChildTextTrim(BeanMapper.TYPE_TAG).equals(
					BeanMapper.TYPE_TEXT))
				{
					field = new Element(memberEle
						.getChildTextTrim(BeanMapper.COLUMN_TAG));
					try
					{
						field.addContent(new CDATA(getMethodValue(memberEle
							.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
							memberEle.getChildTextTrim(BeanMapper.FORMAT_TAG),
							memberEle.getChildTextTrim(BeanMapper.TYPE_TAG))));
					}
					catch (Exception e)
					{
						throw new Exception(e);
					}					
				}
				else
				{
					try
					{
						log.debug("In  XMLBean.getMethodValue - getXMLElements : Before get the field tag");
						
						log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.ACCESSOR_TAG VALUE : "
								+ memberEle
									.getChildTextTrim(BeanMapper.ACCESSOR_TAG));
						log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.FORMAT_TAG VALUE : "
								+ memberEle
									.getChildTextTrim(BeanMapper.FORMAT_TAG));
						log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.FORMAT_TAG VALUE : "
								+ memberEle
									.getChildTextTrim(BeanMapper.TYPE_TAG));
						field = new Element(memberEle
							.getChildTextTrim(BeanMapper.COLUMN_TAG))
							.setText(getMethodValue(memberEle
								.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
								memberEle
									.getChildTextTrim(BeanMapper.FORMAT_TAG),
								memberEle.getChildTextTrim(BeanMapper.TYPE_TAG)));
						
						log.debug("In  XMLBean.getMethodValue - getXMLElements : After get the field tag");
					}
					catch (Exception e)
					{
						throw new Exception(e);
					}
				}
				eventEle.addContent(field);
			}
			list.add(eventEle);
		}
		return list;
	}

	

	public String getXML() throws Exception
	{
		return new XMLOutputter(Format.getPrettyFormat()).outputString(this
			.getXMLElements());
	}

	/**
	 * This get the value and converts Java default values to DB2 default values
	 * like INT Value from 0 to "-1" DATE Value from Null to "1900-01-01"
	 * CHAR(STRING) Value from Null to "NUL"
	 * 
	 * @param javaDefault
	 * @param db2Default
	 * @return
	 */
	private String getMethodValue(String methodName, String formatString,
		String typeString) throws SecurityException, NoSuchMethodException,
		IllegalArgumentException, IllegalAccessException,
		InvocationTargetException
	{

		log.debug("In  XMLBean.getMethodValue - Before checking methodName is null or not - methodName : "
				+ methodName);
		if (methodName == null || methodName.length() < 1)
			return null;
		log.debug("In  XMLBean.getMethodValue - After checking methodName and it is not null");

		StringTokenizer st = new StringTokenizer(methodName, ".");
		String currMethodName = null;
		Method currMethod = null;
		Object currObj = this;
		String retValue = null;

		while (st.hasMoreTokens())
		{
			currMethodName = st.nextToken();
			currMethod = currObj.getClass().getMethod(currMethodName, null);

			if (currMethod != null)
				log.debug("In  XMLBean.getMethodValue - currMethod is not null ");

			if (st.hasMoreTokens())
			{
				log.debug("In  XMLBean.getMethodValue - in second st.hasMoreTokens() ");
				if (currObj == null)
					log.debug("In  XMLBean.getMethodValue - 1 in if(currObj == null) ");

				currObj = currMethod.invoke(currObj, null);
				if (currObj == null)
				{
					log.debug("In  XMLBean.getMethodValue - 2 in if (currObj == null) ");
					if (typeString != null && typeString.equals("INTEGER"))
						retValue = DB2_DEFAULT_FOR_INT;
					else if (typeString != null && typeString.equals("BOOLEAN"))
						retValue = DB2_DEFAULT_FOR_BOOLEAN;
					else if (typeString != null && typeString.equals("CHAR"))
						retValue = DB2_DEFAULT_FOR_CHAR;
					else if (typeString != null && typeString.equals("DATE"))
						retValue = DB2_DEFAULT_FOR_DATE;

					//retValue = DB2_DEFAULT_FOR_CHAR;
					return retValue;
				}
			}
			else
			{
				String retTypeName = currMethod.getReturnType().getName();
				log.debug("In  XMLBean.getMethodValue - retTypeName : "
						+ retTypeName);
				Object retObj = currMethod.invoke(currObj, null);

				// this check is only system.out
				if (retObj != null)
					log.debug("In  XMLBean.getMethodValue - retObj is not null");
				else
					log.debug("In  XMLBean.getMethodValue - retObj is null");

				if (currMethod.getReturnType().isPrimitive())
				{
					if (retTypeName.equals("boolean"))
					{
						retValue = ((Boolean) retObj).booleanValue() ? "Y"
							: "N";
					}
					else if (retTypeName.equals("char"))
					{
						retValue = XMLIllegalCharConverter
							.replaceIllegalCharacters(retObj.toString());

						log.debug("In  XMLBean.getMethodValue - Before retValue converting null to NUL: "
								+ retValue);
						if (retValue == null)
							retValue = DB2_DEFAULT_FOR_CHAR;
						log.debug("In  XMLBean.getMethodValue - After retValue converting null to NUL: "
								+ retValue);
					}
					else
					{
						retValue = retObj.toString();

						log.debug("In  XMLBean.getMethodValue - before retValue converting 0 to -1: "
								+ retValue);
						log.debug("In  XMLBean.getMethodValue - After retValue converting 0 to -1: "
								+ retValue);
					}
				}
				else if (retTypeName.equals("java.lang.String"))
				{
					if (retObj != null)
					{
						retValue = XMLIllegalCharConverter
							.replaceIllegalCharacters(retObj.toString());
					}

					if (retValue == null)
						retValue = DB2_DEFAULT_FOR_CHAR;
				}
				else if (retTypeName.equals("java.util.Date"))
				{
					if (retObj != null)
						retValue = new SimpleDateFormat(formatString)
							.format((Date) retObj);

					if (retValue == null)
						retValue = DB2_DEFAULT_FOR_DATE;
				}
			}
		}
		return retValue;
	}
/* For submiting the request 
 * 		Converts Java default values to DB2 default values
 * 		like INT Value from 0 to "-1" DATE Value from Null to "1900-01-01"
 * 		CHAR(STRING) Value from Null to "NUL"
 *     boolean defaultConvert -- true 
 * For saving unsubmitted request there sholus not be any conversion
 *     boolean defaultConvert -- false
 *  */
	public Element getXMLElements(Element element, boolean defaultConvert) throws Exception
		{
			BeanMapper mapper = null;
			ArrayList list = new ArrayList();
			mapper = BeanMapper.getInstance();
			Element mapping = mapper.getMapping(getClass().getName());
			if (mapping == null)
			{
				return null;
			}
			Iterator tableIter = mapping.getChildren(BeanMapper.DB_TABLE_TAG)
				.iterator();
			while (tableIter.hasNext())
			{
				Element currTab = (Element) tableIter.next();
				
				//adding columns
				Iterator iter = currTab.getChildren(BeanMapper.MEMBER_TAG)
					.iterator();
				Element field = null;
				while (iter.hasNext())
				{
					Element memberEle = (Element) iter.next();
					if (memberEle.getChildTextTrim(BeanMapper.TYPE_TAG).equals(
						BeanMapper.TYPE_TEXT))
					{
											
						try
						{
							if(defaultConvert)
							{
								field = new Element(memberEle.getChildTextTrim(BeanMapper.COLUMN_TAG));
								field.addContent(new CDATA(getMethodValue(memberEle
										.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
										memberEle.getChildTextTrim(BeanMapper.FORMAT_TAG),
										memberEle.getChildTextTrim(BeanMapper.TYPE_TAG))));
							}
							else
							{
								String tagName=memberEle.getChildTextTrim(BeanMapper.COLUMN_TAG);
								tagName= tagName.replace('_','-');
								field.addContent(new CDATA(getMethodValueNoDefault(memberEle
									.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
									memberEle.getChildTextTrim(BeanMapper.FORMAT_TAG),
									memberEle.getChildTextTrim(BeanMapper.TYPE_TAG),tagName)));
							}
							
							
						}
						catch (Exception e)
						{
							throw new Exception(e);
						}
						
					}
					else
					{
						try
						{
							log.debug("In  XMLBean.getMethodValue - getXMLElements : Before get the field tag");
							
							log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.ACCESSOR_TAG VALUE : "
									+ memberEle
										.getChildTextTrim(BeanMapper.ACCESSOR_TAG));
							
							log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.FORMAT_TAG VALUE : "
									+ memberEle
										.getChildTextTrim(BeanMapper.FORMAT_TAG));
							log.debug("In  XMLBean.getMethodValue - getXMLElements : BeanMapper.FORMAT_TAG VALUE : "
									+ memberEle
										.getChildTextTrim(BeanMapper.TYPE_TAG));
							
							if(defaultConvert)
							{
								
								field = new Element(memberEle.getChildTextTrim(BeanMapper.COLUMN_TAG))
									.setText(getMethodValue(memberEle
									.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
											memberEle
									.getChildTextTrim(BeanMapper.FORMAT_TAG),
											memberEle.getChildTextTrim(BeanMapper.TYPE_TAG)));
							
							}
							else
							{
								String tagName=memberEle.getChildTextTrim(BeanMapper.COLUMN_TAG);
								tagName= tagName.replace('_','-');
										
								field = new Element(tagName)
										.setText(getMethodValueNoDefault(memberEle
										.getChildTextTrim(BeanMapper.ACCESSOR_TAG),
											memberEle
												.getChildTextTrim(BeanMapper.FORMAT_TAG),
										memberEle.getChildTextTrim(BeanMapper.TYPE_TAG),tagName));
							}
							
							log.debug("In  XMLBean.getMethodValue - getXMLElements : After get the field tag");
						}
						catch (Exception e)
						{
							throw new Exception(e);
						}						
					}
					element.addContent(field);
					
				}
				
			}
			return element;
		}

	// only user for saving unsubmitted request
	
	private String getMethodValueNoDefault(String methodName, String formatString,
			String typeString,String tagName) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
		{

			if (methodName == null || methodName.length() < 1)
				return null;
			
			StringTokenizer st = new StringTokenizer(methodName, ".");
			String currMethodName = null;
			Method currMethod = null;
			Object currObj = this;
			String retValue = null;

			while (st.hasMoreTokens())
			{
				currMethodName = st.nextToken();
				currMethod = currObj.getClass().getMethod(currMethodName, null);


				if (st.hasMoreTokens())
				{

					currObj = currMethod.invoke(currObj, null);
					if (currObj == null)
					{
						return retValue;
					}
				}
				else
				{
					String retTypeName = currMethod.getReturnType().getName();
					Object retObj = currMethod.invoke(currObj, null);
					
					

					if (currMethod.getReturnType().isPrimitive())
					{
						if (retTypeName.equals("boolean"))
						{
							
								retValue = ((Boolean) retObj).booleanValue() ? "Y"
								: "N";
							
						}
						else if (retTypeName.equals("char"))
						{
		//					retValue = XMLIllegalCharConverter
		//						.replaceIllegalCharacters(retObj.toString());

									if(tagName.equalsIgnoreCase("RQST-NM"))
									{
										retValue = XMLIllegalCharConverter
										.replaceIllegalCharacters(retObj.toString());
									}
									else
									{
										retValue=retObj.toString();
									}

						}
						else if (retTypeName.equals("int"))
						{
							retValue = retObj.toString();
							   
						}
						else
						{
							
								retValue = retObj.toString();
							
						
						}
					}
					else if (retTypeName.equals("java.lang.String"))
					{
						if (retObj != null)
						{
							if(tagName.equalsIgnoreCase("RQST-NM"))
							{
								retValue = XMLIllegalCharConverter
								.replaceIllegalCharacters(retObj.toString());
							}
							else
							{
								retValue=retObj.toString();
							}
						}

					}
					else if (retTypeName.equals("java.util.Date"))
					{
						if (retObj != null)
							retValue = new SimpleDateFormat(formatString)
								.format((Date) retObj);

					}
				}
			}
			return retValue;
		}

	public static void main(String[] args) throws Exception
	{
//		DuaDataFile dfile = new DuaDataFile();
//		DataFile file = dfile;
//		System.out.println(file.getXML());
	}


}
