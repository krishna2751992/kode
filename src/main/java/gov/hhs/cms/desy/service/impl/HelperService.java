package gov.hhs.cms.desy.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.security.SecurityUtils;

public class HelperService {

	protected static final String CREATE = "C";
	protected static final String UPDATE = "U";

	@Inject
	protected DsyHttpAsync dsyHttpAsync;

	/**
	 * Get the user id of the current user.
	 *
	 * @return the userId of the current user
	 */
	protected String getCurrentUserId() {
		Optional<String> uid = SecurityUtils.getCurrentUserLogin();
		if (uid.isPresent()) {
			return uid.get();
		} else {
			throw new BusinessException("User Id/EUA Id not found for the current user");
		}
	}

}
