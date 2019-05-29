/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.RecipientDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface RecipientsService {
	public List<RecipientDTO> getRecipients(int duaNum,String userId);
}
