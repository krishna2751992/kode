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

import gov.hhs.cms.desy.service.DenialReasonCodesService;
import gov.hhs.cms.desy.service.dto.ApprovalDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DenialReasonCodesResource {

	private final Logger log = LoggerFactory.getLogger(DenialReasonCodesResource.class);
	
	@Inject
	private DenialReasonCodesService denialReasonCodes;	
	
	@GetMapping(value = "/denial-status-codes",  produces = "application/json")
	public ResponseEntity<List<ApprovalDTO>> getDenialReasonCodes(HttpServletRequest request) {
		log.info("DenialReasonCodesResource :: getDenialReasonCodes #");
		List<ApprovalDTO> approvalDTOLst= new ArrayList<ApprovalDTO>();
		
		approvalDTOLst = denialReasonCodes.getDenialReasonCodes(request.getSession().getAttribute("userId").toString());
		log.info("getDenialReasonCodes : approvalDTOLst :" + approvalDTOLst);
		
		return new ResponseEntity<>(approvalDTOLst, HttpStatus.OK);
	}	

}
