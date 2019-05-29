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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.UserLoginInfoService;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class UserLoginInfoResource {
	private final Logger log = LoggerFactory.getLogger(UserLoginInfoResource.class);
	
	@Inject
	private UserLoginInfoService userLoginInfo;
	

	
	@PutMapping(value = "/user-login-info",  produces = "application/json")
	public ResponseEntity<String> getUserLoginInfo(HttpServletRequest request) {
		log.info("UserLoginInfoResource :: getUserLoginInfo #");
		String response = "";
		
		response = userLoginInfo.updateLogonInfo(request.getSession().getAttribute("userId").toString());
		log.info("getUserLoginInfo - response" + response);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
