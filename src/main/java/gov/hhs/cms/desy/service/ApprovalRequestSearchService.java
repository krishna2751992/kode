/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.ApprovalRequestSearchDTO;
import gov.hhs.cms.desy.service.dto.ManageApprovalDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface ApprovalRequestSearchService {
	
	public List<ManageApprovalDTO> getApprovalRequest(ApprovalRequestSearchDTO approvalRequestSearch, String userId);
	public String updateApprovalStatus(String userId, List<String> superIdLst, int approvalStatus);
}
