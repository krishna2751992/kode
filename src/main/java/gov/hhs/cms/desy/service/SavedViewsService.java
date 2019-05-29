/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.OutputTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface SavedViewsService {

	public List<OutputTypeDTO> getSavedViews(int dataSourceID, int dataTypeID, String userId,String userNumber);
}
