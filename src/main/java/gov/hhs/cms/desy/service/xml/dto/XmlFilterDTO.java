/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlFilterDTO {
	private int columnID;
	private String operator;
	private String value;	
	//private String columnName;
	private int groupID;
	private int squenceID=0;
	private String lookupText;
	private String whereClause;
	private String eleIcdIndCd;
	

/**
 * @return
 */
@XmlElement(name = "ELE_ID")
public int getColumnID() {
	return columnID;
}

/**
 * @return
 */

/*public String getColumnName() {
	return columnName;
}*/

/**
 * @return
 */
@XmlElement(name = "GROUP_ID")
public int getGroupID() {
	return groupID;
}

/**
 * @return
 */
@XmlElement(name = "OPPR_LST")
public String getOperator() {
	return operator;
}

/**
 * @return
 */
@XmlElement(name = "ELE_NB")
public int getSquenceID() {
	return squenceID;
}

/**
 * @return
 */
@XmlElement(name = "DATA_VALUE")
public String getValue() {
	return value;
}

/**
 * @return
 */

@XmlElement(name = "ELE_ICD_IND_CD")
public String getEleIcdIndCd() {
	return eleIcdIndCd;
}

/**
 * @param string
 */
public void setEleIcdIndCd(String string) {
	eleIcdIndCd = string;
}

/**
 * @param i
 */
public void setColumnID(int i) {
	columnID = i;
}

/**
 * @param string
 */
/*public void setColumnName(String string) {
	columnName = string;
}*/

/**
 * @param i
 */
public void setGroupID(int i) {
	groupID = i;
}

/**
 * @param string
 */
public void setOperator(String string) {
	operator = string;
}

/**
 * @param i
 */
public void setSquenceID(int i) {
	squenceID = i;
}

/**
 * @param string
 */
public void setValue(String string) {
	if(string!=null && string.length()>0)
	{
		if(string.equalsIgnoreCase("USER INPUT FILE"))
		{
			value = string;
		}else
		{
			value = string.toUpperCase();
		}
		
		
	} else {
		// not setting an empty string here causes errors in Search.removeFilter
		value = "";	
	}
}

/**
 * @return
 */

@XmlElement(name = "DATA_CD_TXT")
public String getLookupText() {
	return lookupText;
}

/**
 * @return
 */
public String getLookupTextOnly() {
	String lookupTextOnly=null;
	if(lookupText!=null && value != null )
	{
		int length=value.length();
		lookupTextOnly=lookupText.substring(length+3).trim();
	}
	return lookupTextOnly;
}

/**
 * @param string
 */
public void setLookupText(String string) {
	lookupText = string;
}

/**
 * @return
 */
@XmlElement(name = "WHR_CLS")
public String getWhereClause() {
	return whereClause;
}

/**
 * @param string
 */
public void setWhereClause(String string) {
	whereClause = string;
}

}
