/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.RoleDTO;
import gov.hhs.cms.desy.service.dto.UserProfileDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface UserRoleService {

	public List<RoleDTO> getUserRoles(String userId);	
	public boolean insertUserRoleReq(String userId, UserProfileDTO userProfileDTO);
	public boolean updateUserRoleReq(String userId, UserProfileDTO userProfileDTO);
	public boolean deleteUserRoleReq(String userId, UserProfileDTO userProfileDTO);
}
