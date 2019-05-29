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
public class XmlNewsDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	private XmlNewsBodyDTO xmlNewsBodyDTO;

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
	 * @return the xmlNewsBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlNewsBodyDTO getXmlNewsBodyDTO() {
		return xmlNewsBodyDTO;
	}

	/**
	 * @param xmlNewsBodyDTO the xmlNewsBodyDTO to set
	 */
	public void setXmlNewsBodyDTO(XmlNewsBodyDTO xmlNewsBodyDTO) {
		this.xmlNewsBodyDTO = xmlNewsBodyDTO;
	}	
	
}
