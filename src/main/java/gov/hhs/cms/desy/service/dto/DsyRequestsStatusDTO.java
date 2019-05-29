/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DsyRequestsStatusDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8715199907698979179L;
	
	private String superId; //SUPER-ID
	private String userId; //USER-ID
	private String userName; //CMS-PRSN-NAME
	private String duaNumber; //DUA-NB
	private String desyExpirationDate; //DESY-EXPRTN-DT
	private boolean duaClosed; // DUA-STUS-CD based on first character set the value
	private boolean supressFlag; //SUPPRESS-COPY-FLG based on first character set the value
	private String requestDesc; //RQST-NM
	private String recipientName; //RECIP-NM
	private String createDate; //CRT-DT
	private String dataDescription; //DATA-DESC
	private String requestId; //DATA-RQST-ID
	private String requestYear; //DATA-YEAR
	private String requestStatus; //CPLN-STUS
	
	public String getSuperId() {
		return superId;
	}
	public void setSuperId(String superId) {
		this.superId = superId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDuaNumber() {
		return duaNumber;
	}
	public void setDuaNumber(String duaNumber) {
		this.duaNumber = duaNumber;
	}
	public String getDesyExpirationDate() {
		return desyExpirationDate;
	}
	public void setDesyExpirationDate(String desyExpirationDate) {
		this.desyExpirationDate = desyExpirationDate;
	}
	public boolean isDuaClosed() {
		return duaClosed;
	}
	public void setDuaClosed(boolean duaClosed) {
		this.duaClosed = duaClosed;
	}
	public boolean isSupressFlag() {
		return supressFlag;
	}
	public void setSupressFlag(boolean supressFlag) {
		this.supressFlag = supressFlag;
	}
	public String getRequestDesc() {
		return requestDesc;
	}
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDataDescription() {
		return dataDescription;
	}
	public void setDataDescription(String dataDescription) {
		this.dataDescription = dataDescription;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestYear() {
		return requestYear;
	}
	public void setRequestYear(String requestYear) {
		this.requestYear = requestYear;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	@Override
	public String toString() {
		return "DsyRequestsStatusDTO [superId=" + superId + ", userId=" + userId + ", userName=" + userName
				+ ", duaNumber=" + duaNumber + ", desyExpirationDate=" + desyExpirationDate + ", duaClosed=" + duaClosed
				+ ", supressFlag=" + supressFlag + ", requestDesc=" + requestDesc + ", recipientName=" + recipientName
				+ ", createDate=" + createDate + ", dataDescription=" + dataDescription + ", requestId=" + requestId
				+ ", requestYear=" + requestYear + ", requestStatus=" + requestStatus + "]";
	}
	
}
