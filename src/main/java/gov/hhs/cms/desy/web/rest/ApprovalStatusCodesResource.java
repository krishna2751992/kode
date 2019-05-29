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

import gov.hhs.cms.desy.service.ApprovalStatusCodesService;
import gov.hhs.cms.desy.service.dto.ApprovalDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class ApprovalStatusCodesResource {
	private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);
	
	@Inject
	private ApprovalStatusCodesService approvalStatusCodes;	
	
	@GetMapping(value = "/approval-status-codes",  produces = "application/json")
	public ResponseEntity<List<ApprovalDTO>> getApprovalStatusCodes(HttpServletRequest request) {
		log.info("ApprovalStatusCodesResource :: getApprovalStatusCodes #");
		
		List<ApprovalDTO> approvalDTOLst = new ArrayList<ApprovalDTO>();

		approvalDTOLst = approvalStatusCodes.getApprovalStatusCodes(request.getSession().getAttribute("userId").toString());
		log.info("ApprovalDTO List :" + approvalDTOLst);			
		
		return new ResponseEntity<>(approvalDTOLst, HttpStatus.OK);
	}
	
}
