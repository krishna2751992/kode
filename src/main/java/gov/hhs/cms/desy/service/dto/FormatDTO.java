/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Jagannathan.Narashim
 *
 */
public class FormatDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -421089805958018193L;
	
	private int formatId;
	@SafeHtml
	private String description;
	/**
	 * @return the formatId
	 */
	public int getFormatId() {
		return formatId;
	}
	/**
	 * @param formatId the formatId to set
	 */
	public void setFormatId(int formatId) {
		this.formatId = formatId;
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
