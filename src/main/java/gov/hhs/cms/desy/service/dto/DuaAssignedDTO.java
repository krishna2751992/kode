/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DuaAssignedDTO implements Serializable{
	private final Logger log = LoggerFactory.getLogger(DuaAssignedDTO.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7621114284868988794L;
	
	private int duaNumber;
	private Date expirationDate;
	private String requestor;
	private String studyName;
	private MoreInfo moreInfo;
	
	
	/**
	 * @return the duaNumber
	 */
	public int getDuaNumber() {
		return duaNumber;
	}
	/**
	 * @param duaNumber the duaNumber to set
	 */
	public void setDuaNumber(int duaNumber) {
		this.duaNumber = duaNumber;
	}
	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	/**
	 * @return the requestor
	 */
	public String getRequestor() {
		return requestor;
	}
	/**
	 * @param requestor the requestor to set
	 */
	public void setRequestor(String requestor) {
		this.requestor = requestor;
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
	
	
	/**
	 * @return the moreInfo
	 */
	public MoreInfo getMoreInfo() {
		return moreInfo;
	}
	/**
	 * @param moreInfo the moreInfo to set
	 */
	public void setMoreInfo(MoreInfo moreInfo) {
		this.moreInfo = moreInfo;
	}
	
}
