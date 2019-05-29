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

import gov.hhs.cms.desy.service.DataSourceRulesService;
import gov.hhs.cms.desy.service.dto.DataSourceRulesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DataSourceRulesResource {
	private final Logger log = LoggerFactory.getLogger(DataSourceRulesResource.class);
	
	@Inject
	private DataSourceRulesService dataSourceRules;
	
	/**
	 * Retrieves data source rules based on data source selected by user.
	 * 
	 * @param dataSourceId
	 * @param userId
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-datasource-rule/{dataSourceId}",  produces = "application/json")
	public ResponseEntity<DataSourceRulesDTO> getDataSourceRules(@PathVariable int dataSourceId, HttpServletRequest request) {
		log.info("DataSourceRulesResource :: getDataSourceRules #");
		DataSourceRulesDTO dataSourceRulesDTO = new DataSourceRulesDTO();

		log.info("getDataSourceRules - dataSourceId :" + dataSourceId);
		
		dataSourceRulesDTO = dataSourceRules.getDataSourceRules(dataSourceId, request.getSession().getAttribute("userId").toString());
		log.info("getDataSourceRules : dataSourceRulesDTO :" + dataSourceRulesDTO);
		
		return new ResponseEntity<>(dataSourceRulesDTO, HttpStatus.OK);
	}

}
