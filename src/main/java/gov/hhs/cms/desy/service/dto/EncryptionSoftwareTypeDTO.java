/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class EncryptionSoftwareTypeDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6845392169719734311L;
	
	private int encryptSoftwareType;
	private String description;
	
	/**
	 * @return the encryptSoftwareType
	 */
	public int getEncryptSoftwareType() {
		return encryptSoftwareType;
	}
	/**
	 * @param encryptSoftwareType the encryptSoftwareType to set
	 */
	public void setEncryptSoftwareType(int encryptSoftwareType) {
		this.encryptSoftwareType = encryptSoftwareType;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}	
	
}
