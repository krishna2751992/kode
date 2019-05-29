/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.ErrorMsgService;

@RestController
@RequestMapping("/api")
public class ErrorMsgResource {

	private final Logger log = LoggerFactory.getLogger(ErrorMsgResource.class);

	@Inject
	private ErrorMsgService errorMsg;

	@GetMapping(value = "/get-err-msg/{errcode}", produces = "application/json")
	public ResponseEntity<String> getErrorMessage(@PathVariable String errcode) {
		log.info("ErrorMsgResource :: getErrorMessage:");
		String errorMessage = errorMsg.getErrorMessage(errcode);
		log.info("getErrorMessage: errorMessage, {}:", errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.OK);
	}
}
