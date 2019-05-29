package gov.hhs.cms.desy.config;

import java.util.Collections;

import javax.inject.Named;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom AuthenticationProvider, all logged in user are set to authenticated.
 * It is assumed to be safe since portal will handle and pass authenticated user
 * to DESY APP
 *
 */
@Named
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return new UsernamePasswordAuthenticationToken(null, Collections.emptyList());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
