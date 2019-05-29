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
public class XmlUserDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	
	private XmlUserBodyDTO xmlUserBodyDTO;

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
	 * @return the xmlUserBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlUserBodyDTO getXmlUserBodyDTO() {
		return xmlUserBodyDTO;
	}

	/**
	 * @param xmlUserBodyDTO the xmlUserBodyDTO to set
	 */
	public void setXmlUserBodyDTO(XmlUserBodyDTO xmlUserBodyDTO) {
		this.xmlUserBodyDTO = xmlUserBodyDTO;
	}	
	
}
