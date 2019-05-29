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

import gov.hhs.cms.desy.service.DataSourceYearsService;
import gov.hhs.cms.desy.service.dto.DataSourceYearsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DataSourceYearsResource {
	private final Logger log = LoggerFactory.getLogger(DataSourceYearsResource.class);
	
	@Inject
	private DataSourceYearsService dataSourceYears;
	
	/**
	 * Retrieves list of years based on DUA, data source, data type and state selected by user and is being called by getDataType method.
	 * 
	 * @param duaNum
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param stateCode
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/get-years/{duaNum}/{dataSourceId}/{dataTypeId}/{stateCode}",  produces = "application/json")	
	public ResponseEntity<List<DataSourceYearsDTO>> getDataSourceYears(@PathVariable int duaNum, @PathVariable int dataSourceId, @PathVariable int dataTypeId, @PathVariable String stateCode, HttpServletRequest request) {
		log.info("DataSourceYearsResource :: getDataSourceYears #");
		
		List<DataSourceYearsDTO> dataSourceYearsDTOLst = new ArrayList<DataSourceYearsDTO>();

		log.info("getDataSourceYears - duaNum :" + duaNum);
		log.info("getDataSourceYears - dataSourceId :" + dataSourceId);
		log.info("getDataSourceYears - dataTypeId :" + dataTypeId);
		log.info("getDataSourceYears - stateCode :" + stateCode);
		
		dataSourceYearsDTOLst = dataSourceYears.getDataSourceYears(duaNum, dataSourceId, dataTypeId, stateCode, request.getSession().getAttribute("userId").toString());
		log.info("getDataSourceYears : dataSourceYearsDTOLst :" + dataSourceYearsDTOLst);

		return new ResponseEntity<>(dataSourceYearsDTOLst, HttpStatus.OK);
	}
	
	@GetMapping(value = "/get-years-no-statecode/{duaNum}/{dataSourceId}/{dataTypeId}",  produces = "application/json")	
	public ResponseEntity<List<DataSourceYearsDTO>> getDataSourceYearsWithNoStateCode(@PathVariable int duaNum, @PathVariable int dataSourceId, @PathVariable int dataTypeId,  HttpServletRequest request) {
		log.info("DataSourceYearsResource :: getDataSourceYears #");
		
		List<DataSourceYearsDTO> dataSourceYearsDTOLst = new ArrayList<DataSourceYearsDTO>();

		log.info("getDataSourceYears - duaNum :" + duaNum);
		log.info("getDataSourceYears - dataSourceId :" + dataSourceId);
		log.info("getDataSourceYears - dataTypeId :" + dataTypeId);		
		
		dataSourceYearsDTOLst = dataSourceYears.getDataSourceYearsWithNoStateCode(duaNum, dataSourceId, dataTypeId, request.getSession().getAttribute("userId").toString());
		log.info("getDataSourceYears : dataSourceYearsDTOLst :" + dataSourceYearsDTOLst);

		return new ResponseEntity<>(dataSourceYearsDTOLst, HttpStatus.OK);
	}
}
