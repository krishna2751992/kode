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

import gov.hhs.cms.desy.service.DataSourceService;
import gov.hhs.cms.desy.service.dto.DataSourceDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DataSourceResource {
	private final Logger log = LoggerFactory.getLogger(DataSourceResource.class);
	
	@Inject
	private DataSourceService dataSource;

	/**
	 * Retrieves list of data sources based on DUA number and user ID
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-datasource/{duaNum}",  produces = "application/json")
	public ResponseEntity<List<DataSourceDTO>> getDataSource(@PathVariable int duaNum, HttpServletRequest request) {
		log.info("DataSourceResource :: getDataSource #");	
		List<DataSourceDTO> dataSourceDTOLst= new ArrayList<DataSourceDTO>();

		log.info("getDataSource - duaNum :" + duaNum);
		
		dataSourceDTOLst = dataSource.getDataSources(duaNum, request.getSession().getAttribute("userId").toString());	
		log.info("getDataSource : dataSourceDTOLst :" + dataSourceDTOLst);
		
		return new ResponseEntity<>(dataSourceDTOLst, HttpStatus.OK);
	}
}
