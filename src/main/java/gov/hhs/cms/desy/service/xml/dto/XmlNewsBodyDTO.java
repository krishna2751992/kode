/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlNewsBodyDTO {

	private XmlNewsEventDTO xmlNewsEventDTO;

	/**
	 * @return the xmlNewsEventDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlNewsEventDTO getXmlNewsEventDTO() {
		return xmlNewsEventDTO;
	}

	/**
	 * @param xmlNewsEventDTO the xmlNewsEventDTO to set
	 */
	public void setXmlNewsEventDTO(XmlNewsEventDTO xmlNewsEventDTO) {
		this.xmlNewsEventDTO = xmlNewsEventDTO;
	}
	
	
}
