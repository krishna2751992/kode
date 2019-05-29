/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ViewFieldsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6848482913535318425L;
	private int columnID;
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
