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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.ResendEncryptEmailService;
import gov.hhs.cms.desy.service.dto.ResendEncryptEmailDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class ResendEncryptEmailResource {

	private final Logger log = LoggerFactory.getLogger(ResendEncryptEmailResource.class);
	
	@Inject
	private ResendEncryptEmailService resendEncryptEmail;	
	
	@GetMapping(value = "/resend-encrypt-email/{requestId}",  produces = "application/json")
	public ResponseEntity<ResendEncryptEmailDTO> getResendEncryptEmail(@PathVariable String requestId, HttpServletRequest request){
		log.info("ResendEncryptEmailResource :: getResendEncryptEmail:");
		ResendEncryptEmailDTO resendEncryptEmailDTO = new ResendEncryptEmailDTO();
		
		resendEncryptEmailDTO = resendEncryptEmail.getResendEncryptEmail(Integer.parseInt(requestId), request.getSession().getAttribute("userId").toString());
		log.info("getResendEncryptEmail: resendEncryptEmailDTO :" + resendEncryptEmailDTO);
		
		return new ResponseEntity<>(resendEncryptEmailDTO, HttpStatus.OK);
	}
}
