/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class UserProfileDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167194567679815987L;
	private UserDTO userDTO;
	private RoleDTO roleDTO;
	/**
	 * @return the userDTO
	 */
	public UserDTO getUserDTO() {
		return userDTO;
	}
	/**
	 * @param userDTO the userDTO to set
	 */
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	/**
	 * @return the roleDTO
	 */
	public RoleDTO getRoleDTO() {
		return roleDTO;
	}
	/**
	 * @param roleDTO the roleDTO to set
	 */
	public void setRoleDTO(RoleDTO roleDTO) {
		this.roleDTO = roleDTO;
	}	
	
}
