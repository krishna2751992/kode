/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.SaveRequestService;
import gov.hhs.cms.desy.service.dto.RequestDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class SaveRequestResource {

	private final Logger log = LoggerFactory.getLogger(SaveRequestResource.class);
	
	@Inject
	private SaveRequestService saveRequest;	
	
	@PostMapping(value = "/save-request", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> saveRequest(@RequestBody @Valid RequestDTO requestDTO) {
		log.info("SaveRequestResource ::  saveRequest #");
		
		String requestId = saveRequest.saveUserRequest(requestDTO);
		log.info("saveRequest : requestId, {}:", requestId);

		return new ResponseEntity<>(requestId, HttpStatus.OK);
	}
}
