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

import gov.hhs.cms.desy.service.DataTypeService;
import gov.hhs.cms.desy.service.dto.DataTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DataTypeResource {
	private final Logger log = LoggerFactory.getLogger(DataTypeResource.class);
	
	@Inject
	private DataTypeService dataType;
	
	/**
	 * Retrieves list of data types based on DAU and data source selected by user and is being called by getDataSorce method.
	 * 
	 * @param duaNum
	 * @param encryptionSwitch
	 * @param dataSourceId
	 * @return
	 * @throws Exception
	 */	
	@GetMapping(value = "/get-datatypes/{duaNum}/{encryptionSwitch}/{dataSourceId}",  produces = "application/json")
	public ResponseEntity<List<DataTypeDTO>> getDataTypes(@PathVariable int duaNum, @PathVariable char encryptionSwitch, @PathVariable int dataSourceId, HttpServletRequest request) {	
		log.info("DataTypeResource :: getDataTypes #");
		List<DataTypeDTO> dataTypeDTOLst= new ArrayList<DataTypeDTO>();
		
		log.info("getDataTypes - duaNum :" + duaNum);
		log.info("getDataTypes - encryptionSwitch :" + encryptionSwitch);
		log.info("getDataTypes - dataSourceId :" + dataSourceId);
		
		dataTypeDTOLst = dataType.getDataTypes(duaNum, encryptionSwitch, dataSourceId, request.getSession().getAttribute("userId").toString());
		
		log.info("getDataTypes : dataTypeDTOLst :" + dataTypeDTOLst);

		return new ResponseEntity<>(dataTypeDTOLst, HttpStatus.OK);
	}
}
