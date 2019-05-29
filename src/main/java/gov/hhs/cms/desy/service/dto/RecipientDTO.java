package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

public class RecipientDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5574546779338805872L;
	private int recipID;
	@SafeHtml
	private String name;
	@SafeHtml
	private String email;
	
	/**
	 * @return the recipID
	 */
	public int getRecipID() {
		return recipID;
	}
	/**
	 * @param recipID the recipID to set
	 */
	public void setRecipID(int recipID) {
		this.recipID = recipID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}	
	
}
