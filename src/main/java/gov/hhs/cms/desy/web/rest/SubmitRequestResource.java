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

import gov.hhs.cms.desy.service.SubmitRequestService;
import gov.hhs.cms.desy.service.dto.RequestDTO;


@RestController
@RequestMapping("/api")
public class SubmitRequestResource {
	private final Logger log = LoggerFactory.getLogger(SubmitRequestResource.class);
	
	@Inject
	private SubmitRequestService submitRequest;	
	
	@PostMapping(value = "/submit-request")	
	public ResponseEntity<String> submitRequest(@RequestBody @Valid RequestDTO requestDTO) {
		log.info("SubmitRequestResource ::  submitRequest #"+ requestDTO);
		
		String requestId = "678977" ;//submitRequest.submitUserRequest(requestDTO);
		log.info("submitRequest : requestId, {}:", requestId);
		
		return new ResponseEntity<>(requestId, HttpStatus.OK);
	}	

}
