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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.OutputTypesService;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class OutputTypesResource {

	private final Logger log = LoggerFactory.getLogger(OutputTypesResource.class);

	@Inject
	private OutputTypesService outputTypes;
	
	@GetMapping(value = "/get-output-types",  produces = "application/json")
	ResponseEntity<List<OutputTypeDTO>> getOutputTypes(HttpServletRequest request) {		
		log.info("OutputTypesResource :: getOutputTypes #");
		
		List<OutputTypeDTO> outputTypeDTOLst= new ArrayList<OutputTypeDTO>();
		
		outputTypeDTOLst = outputTypes.getOutputTypes(request.getSession().getAttribute("userId").toString());
		log.info("getOutputTypes : outputTypeDTOLst" + outputTypeDTOLst);

		return new ResponseEntity<>(outputTypeDTOLst, HttpStatus.OK);
	}
}
