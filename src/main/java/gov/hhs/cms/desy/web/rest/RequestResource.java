/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.RequestSearchService;
import gov.hhs.cms.desy.service.RequestService;
import gov.hhs.cms.desy.service.RequestStatusService;
import gov.hhs.cms.desy.service.dto.DsyRequestsStatusDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.RequestSearchDTO;

/**
 * RequestResource controller
 */
@RestController
@RequestMapping("/api")
public class RequestResource {
	private final Logger log = LoggerFactory.getLogger(RequestResource.class);
	
	@Inject
	private RequestService reqeust;
	
	@Inject
	private RequestSearchService requestSearch;

	@Inject
	private RequestStatusService requestStatus;

	/**
	 * Retrieve Request Summary for given Request Id or super Id.
	 * 
	 * @param superId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-request/{superId}/{requestId}",  produces = "application/json")
	public ResponseEntity<RequestDTO> getRequest(@PathVariable int superId, @PathVariable int requestId, HttpServletRequest request) {
		log.info("RequestResource : getRequest #");

		log.info("getRequest - superId, {}:", superId);

		log.info("getRequest - {}", requestId);

		RequestDTO requestDTO = reqeust.getRequest(superId, requestId,
				request.getSession().getAttribute("userId").toString());
		log.info("getRequest : requestDTO,{}:", requestDTO);
		
		return new ResponseEntity<>(requestDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin-request-search", produces = "application/json")
	public ResponseEntity<List<RequestDTO>> getAdminRequestSearchResults(@RequestBody RequestSearchDTO requestSearchDTO,
			HttpServletRequest request) {
		log.info("RequestSearchResource :: getAdminRequestSearchResults:");

		// superId is a mandatory Parameter in requestSearchDTO
		List<RequestDTO> requestDTOLst = requestSearch.getRequestsLst(requestSearchDTO,
				request.getSession().getAttribute("userId").toString());

		log.info("RequestSearchResource: requestDTOLst,{}:", requestDTOLst);

		return new ResponseEntity<>(requestDTOLst, HttpStatus.OK);
	}

	/**
	 * This method returns all request created by the login user
	 * 
	 * @return
	 */
	@GetMapping(value = "/user-dua-requests", produces = "application/json")
	public ResponseEntity<List<DsyRequestsStatusDTO>> manageRequests() {
		log.info("RequestStatusResource :: manageRequests#");
		List<DsyRequestsStatusDTO> userRequestStatusDTOLst = requestStatus.manageRequests();
		log.info("manageRequests : allRequestStatusDTOLst: {}", userRequestStatusDTOLst);
		return new ResponseEntity<>(userRequestStatusDTOLst, HttpStatus.OK);
	}

	/**
	 * Get a new SuperID from database based on sequence and is called by Save
	 * Request and Submit Request methods.
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/get-request-id", produces = "application/json")
	public ResponseEntity<String> getRequestId() {
		log.info("RequestStatusResource :: getRequestId #");
		String requestID = requestStatus.getRequestId();
		return new ResponseEntity<>(requestID, HttpStatus.OK);
	}
}
