/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlCancelRequestEventDTO {

	private String pn;
	private String userId;
	private String requestId;
	
	/**
	 * @return the pn
	 */
	@XmlElement(name = "PN")
	public String getPn() {
		return pn;
	}
	/**
	 * @param pn the pn to set
	 */
	public void setPn(String pn) {
		this.pn = pn;
	}
	/**
	 * @return the userId
	 */
	@XmlElement(name = "USER_ID")
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the requestId
	 */
	@XmlElement(name = "DATA_RQST_ID")
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}	
	
}
