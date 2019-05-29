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

import gov.hhs.cms.desy.service.ColumnNamesService;
import gov.hhs.cms.desy.service.dto.ColumnDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class ColumnNamesResource {
	private final Logger log = LoggerFactory.getLogger(ColumnNamesResource.class);
	
	@Inject
	private ColumnNamesService columnNames;

	@GetMapping(value = "/get-all-column-names/{datasourceId}/{dataTypeId}", produces = "application/json")
	public ResponseEntity<List<ColumnDTO>> getColumnNames(@PathVariable String datasourceId, @PathVariable String dataTypeId, HttpServletRequest request) {
		log.info("ColumnNamesResource :  getColumnNames #");
		List<ColumnDTO> columnDTOLst = new ArrayList<ColumnDTO>();
		
		log.info("getColumnNames - datasourceId :" + datasourceId);
		log.info("getColumnNames - dataTypeId :" + dataTypeId);			
		
		columnDTOLst = columnNames.getColumnDTO(request.getSession().getAttribute("userId").toString(), datasourceId, dataTypeId);
		log.info("getColumnNames : columnDTOLst :" + columnDTOLst);
		
		return new ResponseEntity<>(columnDTOLst , HttpStatus.OK);
	}

}
