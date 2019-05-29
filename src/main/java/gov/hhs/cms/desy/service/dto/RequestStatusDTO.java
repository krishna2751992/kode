package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

 
public class RequestStatusDTO implements Serializable {

	private static final long serialVersionUID = 5925489808581368716L;
	
	private String duaNumber; //DUA-NB
	private String superId; //SUPER-ID
	private String requestId; //DATA-RQST-ID
	private String requestedOn; //CRT-DT
	private String status;	//CPLN-STUS
	
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
	 * @return the requestedOn
	 */
	public String getRequestedOn() {
		return requestedOn;
	}
	/**
	 * @param requestedOn the requestedOn to set
	 */
	public void setRequestedOn(String requestedOn) {
		this.requestedOn = requestedOn;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSuperId() {
		return superId;
	}
	public void setSuperId(String superId) {
		this.superId = superId;
	} 
}
