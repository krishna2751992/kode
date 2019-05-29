/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.ApprovalDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface ApprovalStatusCodesService {

	public List<ApprovalDTO> getApprovalStatusCodes(String userId);
}
