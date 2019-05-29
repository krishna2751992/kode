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

import gov.hhs.cms.desy.service.CriteriaFieldsService;
import gov.hhs.cms.desy.service.dto.CriteriaFieldsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class CriteriaFieldsResource {

	private final Logger log = LoggerFactory.getLogger(CriteriaFieldsResource.class);
	
	@Inject
	CriteriaFieldsService criteriaFields;
	
	/**
	 * 	Retrieves list of elements based on data source
     *	and data type selected by the user and being used to 
     *  display the list of available search elements on search Criteria screen.
     *  
	 * @param dataSourceId
	 * @param dataTypeId
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/get-criteria-fields/{dataSourceId}/{dataTypeId}",  produces = "application/json")
	public ResponseEntity<List<CriteriaFieldsDTO>> getSearchCriteriaFields(@PathVariable int dataSourceId, @PathVariable int dataTypeId, HttpServletRequest request) {
		log.info("CriteriaFieldsResource :: getSearchCriteriaFields #");
		List<CriteriaFieldsDTO> criteriaFieldsDTOLst = new ArrayList<CriteriaFieldsDTO>();

		log.info("getSearchCriteriaFields - dataSourceId :" + dataSourceId);
		log.info("getSearchCriteriaFields - dataTypeId :" + dataTypeId);
		
		criteriaFieldsDTOLst = criteriaFields.getCriteriaFields(dataSourceId, dataTypeId, request.getSession().getAttribute("userId").toString());
		
		log.info("getSearchCriteriaFields : criteriaFieldsDTOLst :" + criteriaFieldsDTOLst);

		return new ResponseEntity<>(criteriaFieldsDTOLst, HttpStatus.OK);
	}
			
}
