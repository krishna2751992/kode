/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ManageApprovalDTO {
	
	private String superId; 
	private String requestId; 
	private String duaNumber; 
	private String submittedBy;
	private String submittedDate;
	private String studyName;
	
	/**
	 * @return the superId
	 */
	public String getSuperId() {
		return superId;
	}
	/**
	 * @param superId the superId to set
	 */
	public void setSuperId(String superId) {
		this.superId = superId;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the duaNumber
	 */
	public String getDuaNumber() {
		return duaNumber;
	}
	/**
	 * @param duaNumber the duaNumber to set
	 */
	public void setDuaNumber(String duaNumber) {
		this.duaNumber = duaNumber;
	}
	/**
	 * @return the submittedBy
	 */
	public String getSubmittedBy() {
		return submittedBy;
	}
	/**
	 * @param submittedBy the submittedBy to set
	 */
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
	/**
	 * @return the submittedDate
	 */
	public String getSubmittedDate() {
		return submittedDate;
	}
	/**
	 * @param submittedDate the submittedDate to set
	 */
	public void setSubmittedDate(String submittedDate) {
		this.submittedDate = submittedDate;
	}
	/**
	 * @return the studyName
	 */
	public String getStudyName() {
		return studyName;
	}
	/**
	 * @param studyName the studyName to set
	 */
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	
}
