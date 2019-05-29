/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.RequestDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface SubmitRequestService {
	
	public String submitUserRequest(RequestDTO requestDTO);
	public String getRequestId(RequestDTO requestDTO);
}
