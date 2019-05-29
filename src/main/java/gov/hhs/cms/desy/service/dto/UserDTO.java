/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import gov.hhs.cms.desy.web.rest.util.UniqueId;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDTO implements Serializable{
	private static final long serialVersionUID = 8553471827846776083L;
	@SafeHtml
	private String userId;
	@SafeHtml
	private String userName;
	@SafeHtml
	private String email;
	@SafeHtml
	private String uniqueId;
	private int userNum;
	@JsonIgnore
	private Date loginTS = null;


	public UserDTO()
	{
		super();
		this.uniqueId = UniqueId.getUniqueId();
		this.loginTS = new Date();
	}

	public UserDTO(String userId)
	{
		if (userId != null)
			this.userId = userId.toUpperCase();
		else
			this.userId = null;
		this.uniqueId = UniqueId.getUniqueId();
		this.loginTS = new Date();
	}

	public Date getLoginTS()
	{
		return this.loginTS;
	}

	public String getUniqueId()
	{
		return this.uniqueId;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	

	/**
	 * @return Returns the userId.
	 */
	public String getUserId()
	{
		if (this.userId != null)
			return userId.toUpperCase();
		else
			return null;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName()
	{
		if (this.userName != null)
			return this.userName.toUpperCase();
		else
			return null;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String fullName)
	{
		this.userName = fullName;
	}

	
	/**
	 * @return
	 */
	public int getUserNum() {
		return userNum;
	}

	/**
	 * @param string
	 */
	public void setUserNum(int userNumber) {
		userNum = userNumber;
	}

}
