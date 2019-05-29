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
public class MediaTypeDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3377343506394283008L;
	private int mediaTypeID;
	@SafeHtml
	private String description;
	/**
	 * @return the mediaTypeID
	 */
	public int getMediaTypeID() {
		return mediaTypeID;
	}
	/**
	 * @param mediaTypeID the mediaTypeID to set
	 */
	public void setMediaTypeID(int mediaTypeID) {
		this.mediaTypeID = mediaTypeID;
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
