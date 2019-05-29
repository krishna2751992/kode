/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 389207892279636407L;
	@SafeHtml
	private String approverUserID;
	private Date approvalDate;
	@SafeHtml
	private String approverName;
	private int statusCode;
	@SafeHtml
	private String description;
	
	/**
	 * @return the approverUserID
	 */
	public String getApproverUserID() {
		return approverUserID;
	}
	/**
	 * @param approverUserID the approverUserID to set
	 */
	public void setApproverUserID(String approverUserID) {
		this.approverUserID = approverUserID;
	}
	/**
	 * @return the approvalDate
	 */
	public Date getApprovalDate() {
		return approvalDate;
	}
	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	/**
	 * @return the approverName
	 */
	public String getApproverName() {
		return approverName;
	}
	/**
	 * @param approverName the approverName to set
	 */
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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
