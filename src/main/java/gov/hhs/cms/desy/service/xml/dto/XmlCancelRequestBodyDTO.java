/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlCancelRequestBodyDTO {

	private XmlCancelRequestEventDTO xmlCancelRequestEventDTO;

	/**
	 * @return the xmlCancelRequestEventDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlCancelRequestEventDTO getXmlCancelRequestEventDTO() {
		return xmlCancelRequestEventDTO;
	}

	/**
	 * @param xmlCancelRequestEventDTO the xmlCancelRequestEventDTO to set
	 */
	public void setXmlCancelRequestEventDTO(XmlCancelRequestEventDTO xmlCancelRequestEventDTO) {
		this.xmlCancelRequestEventDTO = xmlCancelRequestEventDTO;
	}
	
	
}
