/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import gov.hhs.cms.desy.service.ValidateUserService;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ValidateUserResource {
	private final Logger log = LoggerFactory.getLogger(DuaInfoResource.class);

	
	@Inject
	private ValidateUserService validateUser;
	
	
	@GetMapping(value = "/validate-login-user",  produces = "application/json")
	public ResponseEntity<Boolean> validateUser(HttpServletRequest request){
		log.info("ValidateUserResource :: validateUser:");
		boolean isValidUser = true;

		isValidUser = validateUser.isValidUser(request.getSession().getAttribute("userId").toString());
		log.info("validateUser: isValidUser :" + isValidUser);
		
		return new ResponseEntity<>(isValidUser, HttpStatus.OK);
	}
}
