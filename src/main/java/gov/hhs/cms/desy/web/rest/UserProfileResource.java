/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.UserProfileService;
import gov.hhs.cms.desy.service.dto.UserDetailsDTO;
import gov.hhs.cms.desy.service.dto.UserProfileDTO;


@RestController
@RequestMapping("/api")
public class UserProfileResource {

	private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);
	
	@Inject
	private UserProfileService userProfile;
	


	/**
	 * This method retrieves list of user (user profiles) based on user id
	 * and user name selected by user in the front end which is
	 * being passed as searchUser object to this method
	 * It is being used on Manage user screens to search for a specific user.
	 * 
	 * @param srchProflUserId
	 * @param srchProflUserName
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/get-user-all-profiles/{loginUserId}",  produces = "application/json")
	public ResponseEntity<List<UserProfileDTO>> getUserProfileDetails(String loginUserId, HttpServletRequest request) {
		log.info("UserProfileResource :  getUserProfileDetails #");
		List<UserProfileDTO> userProfileDTOLst= new ArrayList<UserProfileDTO>();
		
		userProfileDTOLst = userProfile.getUserProfileDetails(request.getSession().getAttribute("userId").toString());
		log.info("getUserProfiles : userProfileDTOLst :" + userProfileDTOLst);

		return new ResponseEntity<>(userProfileDTOLst , HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/get-user-profiles/{srchProflUserId}/{srchProflUserName}",  produces = "application/json")	
	public ResponseEntity<UserProfileDTO> getUserProfiles(@PathVariable String srchProflUserId, @PathVariable String srchProflUserName, HttpServletRequest request) {
		log.info("UserProfileResource :  getUserProfiles #");
		UserProfileDTO userProfileDTO = new UserProfileDTO();
			
		log.info("getUserProfiles - srchProflUserId :" + srchProflUserId);
		log.info("getUserProfiles - srchProflUserName :" + srchProflUserName);		
		
		userProfileDTO = userProfile.getUserProfile(request.getSession().getAttribute("userId").toString(), srchProflUserId, srchProflUserName);
		log.info("getUserProfiles : userProfileDTOLst :" + userProfileDTO);

		return new ResponseEntity<>(userProfileDTO , HttpStatus.OK);
	}

	/**
	 * GET /account : get the current user.
	 *
	 * @return the current user
	 * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be
	 *                          returned
	 */
	@GetMapping("/account")
	public UserDetailsDTO getAccount(HttpServletRequest request) {
		return (UserDetailsDTO) request.getSession().getAttribute("user");
	}
}
