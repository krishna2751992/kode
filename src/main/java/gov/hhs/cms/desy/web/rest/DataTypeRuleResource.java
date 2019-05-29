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

import gov.hhs.cms.desy.service.DataTypeRuleService;
import gov.hhs.cms.desy.service.dto.DataTypeRuleDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DataTypeRuleResource {
	private final Logger log = LoggerFactory.getLogger(DataTypeRuleResource.class);

	@Inject
	DataTypeRuleService dataTypeRule;	
	
	@GetMapping(value = "/get-data-type-rule/{dataSourceId}/{dataTypeId}",  produces = "application/json")
	public ResponseEntity<DataTypeRuleDTO> getDataTypeRule(@PathVariable int dataSourceId, @PathVariable int dataTypeId, HttpServletRequest request )  {
		log.info("DataTypeRuleResource :: getDataTypeRule #");
		DataTypeRuleDTO dataTypeRuleDTO = new DataTypeRuleDTO();

		log.info("getDataTypeRule - dataSourceId :" + dataSourceId);
		log.info("getDataTypeRule - dataTypeId :" + dataTypeId);		
		
		dataTypeRuleDTO = dataTypeRule.getDataTypeRule(dataSourceId, dataTypeId, request.getSession().getAttribute("userId").toString());
		
		log.info("getDataTypeRule : dataTypeRuleDTO :" + dataTypeRuleDTO);

		return new ResponseEntity<>(dataTypeRuleDTO, HttpStatus.OK);
	}
}
