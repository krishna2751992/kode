/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.RequestSearchDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface RequestSearchService {
	public List<RequestDTO> getRequestsLst(RequestSearchDTO requestSearch,String userID);
}
