/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.dto.UserProfileDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface UserProfileService {

	public UserProfileDTO getUserProfile(String loginUserId, String srchProflUserId, String srchProflUserName);
	public List<UserProfileDTO> getUserProfileDetails(String loginUserId);
}
