/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.RequestDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface RequestService {

	public RequestDTO getRequest(int superID,int requestID,String userID);
}
