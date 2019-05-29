/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ManageRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1793746678716863139L;
	private String duaNumber; //DUA-NB
	private String requestId; //DATA-RQST-ID
	private String requestedOn; //CRT-DT
	private String status;	//CPLN-STUS
	private String requestDesc; //RQST-NM
	private String superId; //SUPER-ID
	private String descCode; //DATA-DESC
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
	/**
	 * @return the requestDesc
	 */
	public String getRequestDesc() {
		return requestDesc;
	}
	/**
	 * @param requestDesc the requestDesc to set
	 */
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}
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
	 * @return the descCode
	 */
	public String getDescCode() {
		return descCode;
	}
	/**
	 * @param descCode the descCode to set
	 */
	public void setDescCode(String descCode) {
		this.descCode = descCode;
	}	

}
