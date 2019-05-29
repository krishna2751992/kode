/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterDTO implements Serializable{
	private static final long serialVersionUID = 4826932422282360259L;

	@SafeHtml
	private int columnID;
	@SafeHtml
	private String operator;
	@SafeHtml
	private String value;
	@SafeHtml
	private String columnName;
	@JsonIgnore
	private int groupID;
	@JsonIgnore
	private int squenceID=0;
	@SafeHtml
	private String lookupText;
	@SafeHtml
	private String whereClause;
	@SafeHtml
	private String eleIcdIndCd;

	//The below property added for JSON conversion
	@JsonIgnore
	private String lookupTextOnly;

/**
 * @return
 */
public int getColumnID() {
	return columnID;
}

/**
 * @return
 */
public String getColumnName() {
	return columnName;
}

/**
 * @return
 */
public int getGroupID() {
	return groupID;
}

/**
 * @return
 */
public String getOperator() {
	return operator;
}

/**
 * @return
 */
public int getSquenceID() {
	return squenceID;
}

/**
 * @return
 */
public String getValue() {
	return value;
}

/**
 * @return
 */
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
public void setColumnName(String string) {
	columnName = string;
}

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
	setLookupTextOnly(lookupText); //because JSON object has this property
}

/**
 * @param lookupTextOnly the lookupTextOnly to set
 */
public void setLookupTextOnly(String lookupTextOnly) {
	this.lookupTextOnly = lookupTextOnly;
}

/**
 * @return
 */
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
