/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ResendEncryptEmailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3561596199020697711L;
	
	private int requestID;
	private UserDTO user;
	private RecipientDTO recipient;
	private EncryptionSoftwareTypeDTO encryptST;
	/**
	 * @return the requestID
	 */
	public int getRequestID() {
		return requestID;
	}
	/**
	 * @param requestID the requestID to set
	 */
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	/**
	 * @return the user
	 */
	public UserDTO getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserDTO user) {
		this.user = user;
	}
	/**
	 * @return the recipient
	 */
	public RecipientDTO getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(RecipientDTO recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the encryptST
	 */
	public EncryptionSoftwareTypeDTO getEncryptST() {
		return encryptST;
	}
	/**
	 * @param encryptST the encryptST to set
	 */
	public void setEncryptST(EncryptionSoftwareTypeDTO encryptST) {
		this.encryptST = encryptST;
	}	
	
}
