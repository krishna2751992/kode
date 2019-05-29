/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ApprovalRequestSearchDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2334473954882230882L;
	private int duaNum;
	private String duaStudyName;
	private String userID;
	private String userName;
	private int requestID;
	private Date subFrom;
	private Date subTo;
	private Date approvedFrom;
	private Date approvedTo;
	private String approverID;
	private String approvalStatusDesc;
	private String requestAction;
	private int approvalStatusCode;
	/**
	 * @return the duaNum
	 */
	public int getDuaNum() {
		return duaNum;
	}
	/**
	 * @param duaNum the duaNum to set
	 */
	public void setDuaNum(int duaNum) {
		this.duaNum = duaNum;
	}
	/**
	 * @return the duaStudyName
	 */
	public String getDuaStudyName() {
		return duaStudyName;
	}
	/**
	 * @param duaStudyName the duaStudyName to set
	 */
	public void setDuaStudyName(String duaStudyName) {
		this.duaStudyName = duaStudyName;
	}
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the requestID
	 */
	public int getRequestID() {
		return requestID;
	}
	/**
	 * @param requestID the requestID to set
	 */
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	/**
	 * @return the subFrom
	 */
	public Date getSubFrom() {
		return subFrom;
	}
	/**
	 * @param subFrom the subFrom to set
	 */
	public void setSubFrom(Date subFrom) {
		this.subFrom = subFrom;
	}
	/**
	 * @return the subTo
	 */
	public Date getSubTo() {
		return subTo;
	}
	/**
	 * @param subTo the subTo to set
	 */
	public void setSubTo(Date subTo) {
		this.subTo = subTo;
	}
	/**
	 * @return the approvedFrom
	 */
	public Date getApprovedFrom() {
		return approvedFrom;
	}
	/**
	 * @param approvedFrom the approvedFrom to set
	 */
	public void setApprovedFrom(Date approvedFrom) {
		this.approvedFrom = approvedFrom;
	}
	/**
	 * @return the approvedTo
	 */
	public Date getApprovedTo() {
		return approvedTo;
	}
	/**
	 * @param approvedTo the approvedTo to set
	 */
	public void setApprovedTo(Date approvedTo) {
		this.approvedTo = approvedTo;
	}
	/**
	 * @return the approverID
	 */
	public String getApproverID() {
		return approverID;
	}
	/**
	 * @param approverID the approverID to set
	 */
	public void setApproverID(String approverID) {
		this.approverID = approverID;
	}
	/**
	 * @return the approvalStatusDesc
	 */
	public String getApprovalStatusDesc() {
		return approvalStatusDesc;
	}
	/**
	 * @param approvalStatusDesc the approvalStatusDesc to set
	 */
	public void setApprovalStatusDesc(String approvalStatusDesc) {
		this.approvalStatusDesc = approvalStatusDesc;
	}
	/**
	 * @return the requestAction
	 */
	public String getRequestAction() {
		return requestAction;
	}
	/**
	 * @param requestAction the requestAction to set
	 */
	public void setRequestAction(String requestAction) {
		this.requestAction = requestAction;
	}
	/**
	 * @return the approvalStatusCode
	 */
	public int getApprovalStatusCode() {
		return approvalStatusCode;
	}
	/**
	 * @param approvalStatusCode the approvalStatusCode to set
	 */
	public void setApprovalStatusCode(int approvalStatusCode) {
		this.approvalStatusCode = approvalStatusCode;
	}	
	
}
