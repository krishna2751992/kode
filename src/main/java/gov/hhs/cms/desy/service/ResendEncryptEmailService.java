/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.ResendEncryptEmailDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface ResendEncryptEmailService {

	public ResendEncryptEmailDTO getResendEncryptEmail(int requestId, String userId);
}
