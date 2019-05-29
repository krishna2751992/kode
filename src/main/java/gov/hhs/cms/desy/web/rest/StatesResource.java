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

import gov.hhs.cms.desy.service.StatesService;
import gov.hhs.cms.desy.service.dto.StatesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class StatesResource {
	private final Logger log = LoggerFactory.getLogger(StatesResource.class);
	
	@Inject
	private StatesService states;
	
	/**
	 * Retrieves list of states based on DUA and Data source selected by the users.
	 * 
	 * @param duaNum
	 * @param dataSourceID
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/get-states/{duaNum}/{dataSourceId}",  produces = "application/json")	
	public ResponseEntity<List<StatesDTO>> getStates(@PathVariable int duaNum, @PathVariable int dataSourceId, HttpServletRequest request) {	
		log.info("StatesResource : getStates #");
		List<StatesDTO> statesDTOLst = new ArrayList<StatesDTO>();

		log.info("getStates - duaNum :" + duaNum);			
		log.info("getStates - dataSourceId :" + dataSourceId);
		
		statesDTOLst = states.getStates(duaNum, dataSourceId, request.getSession().getAttribute("userId").toString());
		log.info("getStates : statesDTOLst :" + statesDTOLst);

		return new ResponseEntity<>(statesDTOLst, HttpStatus.OK);
	}
	
}
