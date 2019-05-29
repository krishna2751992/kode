/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

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

import gov.hhs.cms.desy.service.ColumnRulesService;
import gov.hhs.cms.desy.service.dto.ColumnRulesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class ColumnRulesResource {
	private final Logger log = LoggerFactory.getLogger(ColumnRulesResource.class);
	
	@Inject
	private ColumnRulesService columnRules;
	
	/**
	 * Retrieves element information based on field selected by user on search criteria screen.
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param columnId
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-column-rules/{dataSourceId}/{dataTypeId}/{columnId}",  produces = "application/json")
	public ResponseEntity<ColumnRulesDTO> getColumnRules(@PathVariable int dataSourceId, @PathVariable int dataTypeId, @PathVariable int columnId, HttpServletRequest request) {
		log.info("ColumnRulesResource :: getColumnRules #");

		ColumnRulesDTO columnRulesDTO = new ColumnRulesDTO();
		
		log.info("getColumnRules - dataSourceId :" + dataSourceId);
		log.info("getColumnRules - dataTypeId :" + dataTypeId);
		log.info("getColumnRules - columnId :" + columnId);
		
		columnRulesDTO = columnRules.getColumnRules(dataSourceId, dataTypeId, columnId, request.getSession().getAttribute("userId").toString());
		
		log.info("getColumnRules : columnRulesDTO :" + columnRulesDTO);

		return new ResponseEntity<>(columnRulesDTO, HttpStatus.OK);
	}
}
