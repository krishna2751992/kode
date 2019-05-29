package gov.hhs.cms.desy.web.rest.interceptors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.security.AuthoritiesConstants;
import gov.hhs.cms.desy.service.dto.UserDetailsDTO;


@Named
@Profile("prod")
public class UserInterceptor extends HandlerInterceptorAdapter {
	private final Logger log = LoggerFactory.getLogger(UserInterceptor.class);
	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String REGEX = "=|\\,";
	private static final String DESY = "DESY_";
	private static final String ISMEMBEROF = "ismemberof";
	private static final String USER_ID = "userId";
	private static final String UID = "uid";
	private static final String MAIL = "mail";
	private static final String LNAME = "lname";
	private static final String GIVENNAME = "givenname";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String fName = request.getHeader(GIVENNAME);
		String lName = request.getHeader(LNAME);
		String email = request.getHeader(MAIL);
		String uid = request.getHeader(UID);
		Set<String> authorities = new HashSet<>();

		// No EUA id;which technically shouldn't happen.But just in case, catch it here.
		if (StringUtils.isEmpty(uid)) {
			throw new BusinessException("User ID/EUA not found for the logged-in user");
		}

		// set uid from portal header to the current session
		request.getSession().setAttribute(USER_ID, uid);
		log.info("Current user, {}", uid);

		// get all roles associated for current user from header
		String userRoles = request.getHeader(ISMEMBEROF) == null || request.getHeader(ISMEMBEROF) == NOT_FOUND ? ""
				: request.getHeader(ISMEMBEROF);
		log.info("User role/ismemberof from header, {}", userRoles);

		this.parseRoles(userRoles, authorities);
		log.info("Mapped user role, {}", authorities);

		// If no role being passed from portal, set user role ANONYMOUS to the current
		// session
		if (CollectionUtils.isEmpty(authorities)) {
			log.error("current logged-in user does not have a role");
			authorities.add(AuthoritiesConstants.ANONYMOUS);
			request.getSession().setAttribute("user", this.setUserDetails(uid, fName, lName, email, authorities));
			log.info("Mapped user role, {}", authorities);
			return super.preHandle(request, response, handler);
		}

		this.setCurrentUserAuthorities(uid, authorities);
		// finally set logged-in user details to the current session
		request.getSession().setAttribute("user", this.setUserDetails(uid, fName, lName, email, authorities));
		return super.preHandle(request, response, handler);
	}

	private UserDetailsDTO setUserDetails(String uid, String fName, String lName, String email,
			Set<String> authorities) {
		return new UserDetailsDTO(uid, fName, lName, email, authorities);
	}

	/**
	 * Set CurrentUser Role in the SecurityContextHolder for all logged in user
	 * which set to authenticated. It is assumed to be safe since portal will handle
	 * and pass authenticated user.
	 *
	 */
	private void setCurrentUserAuthorities(String userId, Set<String> role) {
		Authentication auth = new UsernamePasswordAuthenticationToken(userId, "", this.setGrantedAuthorities(role));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private List<SimpleGrantedAuthority> setGrantedAuthorities(Set<String> grantedAuthority) {
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String role : grantedAuthority) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role));
		}
		return grantedAuthorities;
	}

	private void parseRoles(String roles, Set<String> authorities) {
		Set<String> roleSet = Stream.of(roles.split(REGEX))
				.map(elem -> new String(elem))
				.collect(Collectors.toList())
				.stream().filter(elem -> elem.contains(DESY))
				.collect(Collectors.toSet());

		roleSet.stream().forEach(elem -> {
			authorities.add(AuthoritiesConstants.mapRole().get(elem));
		});
	}

}
