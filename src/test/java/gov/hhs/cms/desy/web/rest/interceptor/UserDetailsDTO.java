package gov.hhs.cms.desy.web.rest.interceptor;

import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDetailsDTO {

	private String firstName;
	private String lastName;
	private String email;
	private Set<String> authorities;

	public UserDetailsDTO(String firstName, String lastName, String email, Set<String> authorities) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.authorities = authorities;
	}

	public UserDetailsDTO() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

}
