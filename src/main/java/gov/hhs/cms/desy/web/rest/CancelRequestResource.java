/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;

import org.hibernate.validator.constraints.SafeHtml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.CancelRequestService;
import gov.hhs.cms.desy.service.ErrorMsgService;
import gov.hhs.cms.desy.web.rest.errors.ErrorConstants;

@RestController
@RequestMapping("/api")
public class CancelRequestResource {

	private final Logger log = LoggerFactory.getLogger(CancelRequestResource.class);

	@Inject
	private CancelRequestService cancelRequest;

	@Inject
	ErrorMsgService errorMsgService;

	/**
	 * @param requestId
	 * @return
	 */

	@PostMapping(value = "/cancel-request/{requestId}", produces = "application/json")
	public ResponseEntity<String> cancelRequest(@PathVariable @SafeHtml String requestId) {
		log.info("CancelRequestResource :: cancelRequest :");
		String errorCode = cancelRequest.cancelRequestUpdate(requestId);
		if (Integer.parseInt(errorCode) > ErrorConstants.ERROR_CODE_0) {
			String errorMsg = errorMsgService.getErrorMessage(errorCode);
			log.info("errorMsg, {}:", errorMsg);
		}
		return new ResponseEntity<>(errorCode, HttpStatus.OK);
	}
}
