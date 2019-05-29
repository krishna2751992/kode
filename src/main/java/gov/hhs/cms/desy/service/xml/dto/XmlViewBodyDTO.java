/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlViewBodyDTO {

	private XmlViewEventDTO xmlViewEventDTO;

	/**
	 * @return the xmlViewEventDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlViewEventDTO getXmlViewEventDTO() {
		return xmlViewEventDTO;
	}

	/**
	 * @param xmlViewEventDTO the xmlViewEventDTO to set
	 */
	public void setXmlViewEventDTO(XmlViewEventDTO xmlViewEventDTO) {
		this.xmlViewEventDTO = xmlViewEventDTO;
	}
	
	
}
