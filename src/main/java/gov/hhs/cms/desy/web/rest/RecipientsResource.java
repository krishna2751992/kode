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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.RecipientsService;
import gov.hhs.cms.desy.service.dto.RecipientDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class RecipientsResource {

	private final Logger log = LoggerFactory.getLogger(RecipientsResource.class);

	@Inject
	private RecipientsService recipients;
	
	/**
	 * retrieves list of recipients based on dua number and user id and is 
     * is being called by getDua method.
     * 
	 * @param duaNum
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-recipients/{duaNum}",  produces = "application/json")
	public ResponseEntity<List<RecipientDTO>> getRecipients(@PathVariable int duaNum, HttpServletRequest request) {
		log.info("RecipientsResource :: getRecipients #");
		List<RecipientDTO> recipientDTOLst = new ArrayList<RecipientDTO>();

		log.info("getRecipients - duaNum :" + duaNum);
	
		recipientDTOLst = recipients.getRecipients(duaNum, request.getSession().getAttribute("userId").toString());
		log.info("getRecipients : recipientDTOLst :" + recipientDTOLst);

		return new ResponseEntity<>(recipientDTOLst, HttpStatus.OK);
	}
}
