package gov.hhs.cms.desy.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

	private static final String DESY_D_USER = "DESY_D_USER";

	private static final String DESY_D_ADMIN = "DESY_D_ADMIN";

	private static final String DESY_V_USER = "DESY_V_USER";

	private static final String DESY_V_ADMIN = "DESY_V_ADMIN";

	public static final String ADMIN = "ROLE_ADMIN";

	public static final String USER = "ROLE_USER";

	public static final String ANONYMOUS = "ROLE_ANONYMOUS";

	private AuthoritiesConstants() {
	}

	public static Map<String, String> mapRole() {
		Map<String, String> role = new HashMap<>();
		role.put(DESY_D_ADMIN, ADMIN);
		role.put(DESY_D_USER, USER);
		role.put(DESY_V_ADMIN, ADMIN);
		role.put(DESY_V_USER, USER);
		return role;
	}

}
