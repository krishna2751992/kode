/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jagannathan.Narashim
 *
 */
@XmlRootElement(name = "DESY-REQ")
public class XmlUserProfileReqDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	
	private XmlUserProfileDTO xmlUserProfileDTO;

	/**
	 * @return the xmlHeaderDTO
	 */
	@XmlElement(name = "HEADER")
	public XmlHeaderDTO getXmlHeaderDTO() {
		return xmlHeaderDTO;
	}

	/**
	 * @param xmlHeaderDTO the xmlHeaderDTO to set
	 */
	public void setXmlHeaderDTO(XmlHeaderDTO xmlHeaderDTO) {
		this.xmlHeaderDTO = xmlHeaderDTO;
	}

	/**
	 * @return the xmlUserProfileDTO
	 */
	@XmlElement(name = "BODY")
	public XmlUserProfileDTO getXmlUserProfileDTO() {
		return xmlUserProfileDTO;
	}

	/**
	 * @param xmlUserProfileDTO the xmlUserProfileDTO to set
	 */
	public void setXmlUserProfileDTO(XmlUserProfileDTO xmlUserProfileDTO) {
		this.xmlUserProfileDTO = xmlUserProfileDTO;
	}	
	
}
