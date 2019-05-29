/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.StatesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface StatesService {
	public List<StatesDTO> getStates(int duaNum,int dataSourceID, String userID);
	
}
