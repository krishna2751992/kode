package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlUserProfileDTO {

	private String pn;
	private String userId;
	private String roleId;	
	
	
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
	 * @return the roleId
	 */
	@XmlElement(name = "PRSN_ROLE_NUM")
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}	
	
}
