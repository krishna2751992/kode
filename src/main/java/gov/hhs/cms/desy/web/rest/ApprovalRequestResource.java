/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.constraints.SafeHtml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.ApprovalRequestSearchService;
import gov.hhs.cms.desy.service.dto.ApprovalRequestSearchDTO;
import gov.hhs.cms.desy.service.dto.ManageApprovalDTO;

@RestController
@RequestMapping("/api")
public class ApprovalRequestResource {

	private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);
	
	@Inject
	private ApprovalRequestSearchService approvalRequestSearch;
	
	@GetMapping(value = "/get-approval-requests", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ManageApprovalDTO>> getApprovalRequest(@RequestBody ApprovalRequestSearchDTO approvalRequestSearchDTO,  HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: getApprovalRequest #");
		List<ManageApprovalDTO> approvalRequestLst = new ArrayList<ManageApprovalDTO>();
		approvalRequestSearchDTO.setApprovalStatusCode(300);
		approvalRequestLst = approvalRequestSearch.getApprovalRequest(approvalRequestSearchDTO,request.getSession().getAttribute("userId").toString());
		log.info("ApprovalRequestSearchDTO List :" + approvalRequestLst);
	
		return new ResponseEntity<>(approvalRequestLst, HttpStatus.OK);
	}
	
	
	/**
	 * retrieves list of pending requests(calls searchApprovalRequests method with 
	 * status as 400) and is called when user clicks View pending option under approvals.
	 * 
	 * 
	 * @return
	 */	
	@GetMapping(value = "/get-pending-requests", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ManageApprovalDTO>> getPendingRequest(HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: getPendingRequest #");
		List<ManageApprovalDTO> approvalRequestLst = new ArrayList<ManageApprovalDTO>();
			
		ApprovalRequestSearchDTO approvalRequestSearchDTO = new ApprovalRequestSearchDTO();
		//setting 400(Pending requests) as Approval Status
		approvalRequestSearchDTO.setApprovalStatusCode(400);
		approvalRequestLst = approvalRequestSearch.getApprovalRequest(approvalRequestSearchDTO,request.getSession().getAttribute("userId").toString());
		
		log.info("ApprovalRequestSearchDTO List :" + approvalRequestLst);
		
		return new ResponseEntity<>(approvalRequestLst, HttpStatus.OK);
	}
	
	/**
	 * retrieves list of signed out requests(calls searchApprovalRequests method with status as 410) and is called when user clicks View Signout option under approvals.
	 * 
	 * @return
	 */	
	@GetMapping(value = "/get-singout-requests", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ManageApprovalDTO>> getSignoutRequest(HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: getApprovalRequest #");
		List<ManageApprovalDTO> approvalRequestLst = new ArrayList<ManageApprovalDTO>();

		ApprovalRequestSearchDTO approvalRequestSearchDTO = new ApprovalRequestSearchDTO();
		//setting 400(Pending requests) as Approval Status
		approvalRequestSearchDTO.setApprovalStatusCode(410);
		approvalRequestLst = approvalRequestSearch.getApprovalRequest(approvalRequestSearchDTO,request.getSession().getAttribute("userId").toString());
		
		log.info("ApprovalRequestSearchDTO List :" + approvalRequestLst);
		
		return new ResponseEntity<>(approvalRequestLst, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/update-approval-requests/{requestId}/{approvalStatusCode}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> updateApprovalRequest(@PathVariable @SafeHtml List<String> requestId, @PathVariable @SafeHtml String approvalStatusCode, HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: updateApprovalRequest #");
		String errorCode = "";
		
		try {
			log.info("Request Id list:" + requestId);
		 
			errorCode = approvalRequestSearch.updateApprovalStatus(request.getSession().getAttribute("userId").toString(), requestId, Integer.parseInt(approvalStatusCode));
			
			log.info("ApprovalRequestSearchDTO errorCode :" + errorCode);
		}catch(Exception e) {
			log.error("Error occurred on ApprovalRequestSearchResource :: updateApprovalRequest :", e);
		}
		
		return new ResponseEntity<>(errorCode, HttpStatus.OK);
	}
	
	@GetMapping(value = "/get-approved-requests", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ManageApprovalDTO>> getApprovedRequest(HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: getApprovedRequest #");
		List<ManageApprovalDTO> approvalRequestLst = new ArrayList<ManageApprovalDTO>();

		ApprovalRequestSearchDTO approvalRequestSearchDTO = new ApprovalRequestSearchDTO();
		//setting 300(Approved requests) as Approval Status
		approvalRequestSearchDTO.setApprovalStatusCode(300);
		approvalRequestLst = approvalRequestSearch.getApprovalRequest(approvalRequestSearchDTO,request.getSession().getAttribute("userId").toString());
		
		log.info("ApprovalRequestSearchDTO List :" + approvalRequestLst);
		
		return new ResponseEntity<>(approvalRequestLst, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/get-denied-requests", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<ManageApprovalDTO>> getDeniedRequest(HttpServletRequest request){
		log.info("ApprovalRequestSearchResource :: getDeniedRequest #");
		List<ManageApprovalDTO> allDenialRequestsLst = new ArrayList<ManageApprovalDTO>();
		
		List<ManageApprovalDTO> deniedRequestLst = new ArrayList<ManageApprovalDTO>();

		ApprovalRequestSearchDTO approvalRequestSearchDTO = new ApprovalRequestSearchDTO();
		//setting 300(Approved requests) as Approval Status
		String denialReasonCodes = "200,210,215,220,225,230,235,240,245,250";
		String[] denialReasonCd = denialReasonCodes.split(",");
		
		for(int i =0; i < denialReasonCd.length; i++) {
			deniedRequestLst = null;
			approvalRequestSearchDTO.setApprovalStatusCode(Integer.parseInt(denialReasonCd[i]));
			deniedRequestLst = approvalRequestSearch.getApprovalRequest(approvalRequestSearchDTO,request.getSession().getAttribute("userId").toString());
			deniedRequestLst.stream().forEach(deniedRequest -> allDenialRequestsLst.add(deniedRequest));
			log.info("ApprovalRequestSearchDTO List :" + deniedRequestLst);
		}
		
		log.info("ApprovalRequestSearchDTO List :" + allDenialRequestsLst);
		
		return new ResponseEntity<>(allDenialRequestsLst, HttpStatus.OK);
	}
}
