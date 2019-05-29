package gov.hhs.cms.desy.web.rest.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import gov.hhs.cms.desy.service.dto.UserDetailsDTO;
import gov.hhs.cms.desy.web.rest.interceptors.UserInterceptor;


public class UserInterceptorTest {

	private static final String ROLE = "cn=DESY_D_ADMIN,ou=Groups,dc=cms,dc=hhs,dc=gov:cn=DESY_D_USER,ou=Groups,dc=cms,dc=hhs,dc=gov";
	private static final String MAIL = "mail";
	private static final String LNAME = "lname";
	private static final String GIVENNAME = "givenname";
	public static final String ADMIN_ROLE = "ROLE_ADMIN";
	public static final String USER_ROLE = "ROLE_USER";

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@InjectMocks
	private UserInterceptor interceptor = new UserInterceptor();

	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		request.setRequestURI("/");
		request.addHeader("uid", "KXXD");
		request.addHeader(MAIL, "wube@newwave.io");
		request.addHeader(GIVENNAME, "Wube");
		request.addHeader(LNAME, "Tam");
		response = new MockHttpServletResponse();
	}

	@Test
	public void testInterceptorWithRole() throws Exception {
		Set<String> authorities = Stream.of(ADMIN_ROLE, USER_ROLE).collect(Collectors.toSet());
		request.addHeader("ismemberof", ROLE);
		boolean result = interceptor.preHandle(request, response, null);
		UserDetailsDTO userDetails = (UserDetailsDTO) request.getSession().getAttribute("user");
		assertEquals("Wube", userDetails.getFirstName());
		assertEquals("Tam", userDetails.getLastName());
		assertEquals("wube@newwave.io", userDetails.getEmail());
		assertEquals(authorities, userDetails.getAuthorities());
		assertTrue(result);
	}

	@Test
	public void testInterceptorWithoutRole() throws Exception {
		Set<String> authorities = Stream.of("ROLE_ANONYMOUS").collect(Collectors.toSet());
		request.addHeader("ismemberof", "");
		boolean result = interceptor.preHandle(request, response, null);
		UserDetailsDTO userDetails = (UserDetailsDTO) request.getSession().getAttribute("user");
		assertEquals(authorities, userDetails.getAuthorities());
		assertTrue(result);
	}
}
