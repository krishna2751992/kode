package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.DsyRequestsStatusDTO;

public interface RequestStatusService {
	public String getRequestId();
	public List<DsyRequestsStatusDTO> manageRequests();
}
