/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlUserBodyDTO {

	private XmlUserDataDTO xmlUserDataDTO;

	/**
	 * @return the xmlUserDataDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlUserDataDTO getXmlUserDataDTO() {
		return xmlUserDataDTO;
	}

	/**
	 * @param xmlUserDataDTO the xmlUserDataDTO to set
	 */
	public void setXmlUserDataDTO(XmlUserDataDTO xmlUserDataDTO) {
		this.xmlUserDataDTO = xmlUserDataDTO;
	}
	
	
}
