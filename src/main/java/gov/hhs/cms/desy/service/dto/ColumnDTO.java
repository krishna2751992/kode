/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnDTO implements Serializable{
	private static final long serialVersionUID = -1829702874645041136L;
	@SafeHtml
	private int columnID;
	@SafeHtml
	private String name;
	/**
	 * @return the columnID
	 */
	public int getColumnID() {
		return columnID;
	}
	/**
	 * @param columnID the columnID to set
	 */
	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
