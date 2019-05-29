/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class LookupDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2124440906589212288L;
	private int lookupID;
	private String lookupValue;
	private String lookupText;
	private String lookupIDValue;
	/**
	 * @return the lookupID
	 */
	public int getLookupID() {
		return lookupID;
	}
	/**
	 * @param lookupID the lookupID to set
	 */
	public void setLookupID(int lookupID) {
		this.lookupID = lookupID;
	}
	/**
	 * @return the lookupValue
	 */
	public String getLookupValue() {
		return lookupValue;
	}
	/**
	 * @param lookupValue the lookupValue to set
	 */
	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}
	/**
	 * @return the lookupText
	 */
	public String getLookupText() {
		return lookupText;
	}
	/**
	 * @param lookupText the lookupText to set
	 */
	public void setLookupText(String lookupText) {
		this.lookupText = lookupText;
	}
	/**
	 * @return the lookupIDValue
	 */
	public String getLookupIDValue() {
		return lookupIDValue;
	}
	/**
	 * @param lookupIDValue the lookupIDValue to set
	 */
	public void setLookupIDValue(String lookupIDValue) {
		this.lookupIDValue = lookupIDValue;
	}	
	
}
