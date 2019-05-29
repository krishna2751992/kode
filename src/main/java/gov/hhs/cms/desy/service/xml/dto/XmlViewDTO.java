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
public class XmlViewDTO {

	private XmlHeaderDTO xmlHeaderDTO;

	private XmlViewBodyDTO xmlViewBodyDTO;
	
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
	 * @return the xmlViewBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlViewBodyDTO getXmlViewBodyDTO() {
		return xmlViewBodyDTO;
	}

	/**
	 * @param xmlViewBodyDTO the xmlViewBodyDTO to set
	 */
	public void setXmlViewBodyDTO(XmlViewBodyDTO xmlViewBodyDTO) {
		this.xmlViewBodyDTO = xmlViewBodyDTO;
	}

	
}
