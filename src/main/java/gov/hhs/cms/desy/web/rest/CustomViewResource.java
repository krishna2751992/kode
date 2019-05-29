/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.CustomViewService;
import gov.hhs.cms.desy.service.dto.ViewDTO;


@RestController
@RequestMapping("/api")
public class CustomViewResource {

	private final Logger log = LoggerFactory.getLogger(CustomViewResource.class);
	
	@Inject
	private CustomViewService customView;	
	
	
	@PostMapping(value = "/update-custom-view", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ViewDTO> updateCustomView(@RequestBody @Valid ViewDTO viewDTO, HttpServletRequest request) {
		log.info("CustomViewResource ::  updateCustomView #");		
		
		ViewDTO viewDTOUpdated = customView.updateCustomView(viewDTO);
		
		return new ResponseEntity<>(viewDTOUpdated, HttpStatus.OK);
	}

}
