/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlUpdateStatusEventDTO {

	private String pn;
	private String approvalSuperId;
	private String approvalStatus;
	private String approvalApprovarId;
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
	 * @return the approvalSuperId
	 */
	@XmlElement(name = "APRVL_SUPER_ID")
	public String getApprovalSuperId() {
		return approvalSuperId;
	}
	/**
	 * @param approvalSuperId the approvalSuperId to set
	 */
	public void setApprovalSuperId(String approvalSuperId) {
		this.approvalSuperId = approvalSuperId;
	}
	/**
	 * @return the approvalStatus
	 */
	@XmlElement(name = "APRVL_STUS")
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/**
	 * @return the approvalApprovarId
	 */
	@XmlElement(name = "APRVL_APRVR_ID")
	public String getApprovalApprovarId() {
		return approvalApprovarId;
	}
	/**
	 * @param approvalApprovarId the approvalApprovarId to set
	 */
	public void setApprovalApprovarId(String approvalApprovarId) {
		this.approvalApprovarId = approvalApprovarId;
	}
	
}
