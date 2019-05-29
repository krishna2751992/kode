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

import gov.hhs.cms.desy.service.SavedViewsService;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class SavedViewsResource {

	private final Logger log = LoggerFactory.getLogger(SavedViewsResource.class);
	
	@Inject
	private SavedViewsService savedViews;
	
	/**
	 * Retrieves the list of available views(predefined as well as user defined views)
     * based on data source and data type selected by the user and is being used to display 
     * the list of views on Output screen.
     * 
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param userNumber
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-saved-views/{dataSourceId}/{dataTypeId}/{userNumber}",  produces = "application/json")
	public ResponseEntity<List<OutputTypeDTO>> getSavedViews(@PathVariable int dataSourceId, @PathVariable int dataTypeId, @PathVariable String userNumber, HttpServletRequest request){
		log.info("SavedViewsResource :: getSavedViews #");
		List<OutputTypeDTO> outputTypeDTOLst = new ArrayList<OutputTypeDTO>();

		log.info("getSavedViews - dataSourceId :" + dataSourceId);
		log.info("getSavedViews - dataTypeId :" + dataTypeId);
		log.info("getSavedViews - userNumber :" + userNumber);
		
		outputTypeDTOLst = savedViews.getSavedViews(dataSourceId, dataTypeId, request.getSession().getAttribute("userId").toString(), userNumber);
		log.info("getSavedViews : outputTypeDTOLst :" + outputTypeDTOLst);
		
		return new ResponseEntity<>(outputTypeDTOLst, HttpStatus.OK);
	}
}
