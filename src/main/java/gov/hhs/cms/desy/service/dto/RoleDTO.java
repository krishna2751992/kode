/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jagannathan.Narashim
 *
 */
public class RoleDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5405705982065293300L;
	private int roleID;
	private String description;
	private List accList;
	private final static int USER = 1;
	private final static int APPROVER = 2;
	private final static int SYSTEM_ADMINISTRATOR = 3;
	private final static int DEVELOPER = 4;
	
	/**
	 * @return the roleID
	 */
	public int getRoleID() {
		return roleID;
	}
	/**
	 * @param roleID the roleID to set
	 */
	public void setRoleID(int roleID) {
		this.roleID = roleID;
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
	/**
	 * @return the accList
	 */
	public List getAccList() {
		return accList;
	}
	/**
	 * @param accList the accList to set
	 */
	public void setAccList(List accList) {
		this.accList = accList;
	}
	/**
	 * @return the user
	 */
	public static int getUser() {
		return USER;
	}
	/**
	 * @return the approver
	 */
	public static int getApprover() {
		return APPROVER;
	}
	/**
	 * @return the systemAdministrator
	 */
	public static int getSystemAdministrator() {
		return SYSTEM_ADMINISTRATOR;
	}
	/**
	 * @return the developer
	 */
	public static int getDeveloper() {
		return DEVELOPER;
	}
	
	
}
