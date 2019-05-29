/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.SelectableFieldsDTO;
import gov.hhs.cms.desy.service.dto.ViewFieldsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface SelectableFieldsService {
 
	public List<SelectableFieldsDTO> getSelectableFields(int dataSourceID,int dataTypeID, String userID);
	
	public List<ViewFieldsDTO> getFieldsForSelectedView(int viewID, String userID);
}
