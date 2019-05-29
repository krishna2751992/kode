/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class RequestSearchResultsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2647829265399881610L;

	private int superCount;
	private RequestDTO requestDTO;
	/**
	 * @return the superCount
	 */
	public int getSuperCount() {
		return superCount;
	}
	/**
	 * @param superCount the superCount to set
	 */
	public void setSuperCount(int superCount) {
		this.superCount = superCount;
	}
	/**
	 * @return the requestDTO
	 */
	public RequestDTO getRequestDTO() {
		return requestDTO;
	}
	/**
	 * @param requestDTO the requestDTO to set
	 */
	public void setRequestDTO(RequestDTO requestDTO) {
		this.requestDTO = requestDTO;
	}
	
	public boolean isMaxSuperRequestCount()
	{
			if (superCount >= 300)
				return true;
			else
				return false;
	}
	
}
