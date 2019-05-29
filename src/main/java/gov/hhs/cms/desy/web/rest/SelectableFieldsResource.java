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

import com.codahale.metrics.annotation.Timed;

import gov.hhs.cms.desy.service.SelectableFieldsService;
import gov.hhs.cms.desy.service.dto.SelectableFieldsDTO;
import gov.hhs.cms.desy.service.dto.ViewFieldsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class SelectableFieldsResource {

	private final Logger log = LoggerFactory.getLogger(SelectableFieldsResource.class);
	
	@Inject
	private SelectableFieldsService selectableFields;
	
	/**
	 * Retrieves the list available elements to be displayed on out put screen 
     * when user selects "SELECT AVAILABLE FIELDS" option on output screen.
     * This list is based on data source and data type selected by the user.
     * 
	 * @param dataSourceID
	 * @param dataTypeID
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/get-selectable-fields/{dataSourceId}/{dataTypeId}",  produces = "application/json")
	public ResponseEntity<List<SelectableFieldsDTO>> getSelectableFields(@PathVariable int dataSourceId, @PathVariable int dataTypeId, HttpServletRequest request) {
		log.info("SelectableFieldsResource :: getSelectableFields #");
		
		List<SelectableFieldsDTO> selectableFieldsDTOLst = new ArrayList<SelectableFieldsDTO>();
					
		log.info("getSelectableFields - dataSourceId :" + dataSourceId);
		log.info("getSelectableFields - dataTypeId :" + dataTypeId);		
		
		selectableFieldsDTOLst = selectableFields.getSelectableFields(dataSourceId, dataTypeId, request.getSession().getAttribute("userId").toString());
		log.info("getSelectableFields : selectableFieldsDTOLst :" + selectableFieldsDTOLst);


		return new ResponseEntity<>(selectableFieldsDTOLst , HttpStatus.OK);
	}
	
	/**
	 * Retrieves the list of elements based on predefined or user defined view, selected
     * by the user on output screen and is being used to display the list when user select 
     * Bef-Puf view or user defined view on output screen.
     * 
	 * @param viewID
	 * @return
	 * @throws Exception
	 */

	@GetMapping(value = "/get-view-fields/{viewId}",  produces = "application/json")
	public ResponseEntity<List<ViewFieldsDTO>> getViewFields(@PathVariable int viewId, HttpServletRequest request) {
		log.info("SelectableFieldsResource :: getViewFields #");
		
		log.info("getViewFields :" + viewId);
		List<ViewFieldsDTO> viewFieldsDTOLst = new ArrayList<ViewFieldsDTO>();
		
		viewFieldsDTOLst = selectableFields.getFieldsForSelectedView(viewId, request.getSession().getAttribute("userId").toString());

		return new ResponseEntity<>(viewFieldsDTOLst, HttpStatus.OK);
	}
}
