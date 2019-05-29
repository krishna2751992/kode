/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.CriteriaFieldsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface CriteriaFieldsService {
	public List<CriteriaFieldsDTO> getCriteriaFields(int dataSourceID,int dataTypeID, String userID);
}

