/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlSubmitRequestBodyDTO {

	private XmlSubmitRequestEventDTO xmlSubmitRequestEventDTO;

	/**
	 * @return the xmlSubmitRequestEventDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlSubmitRequestEventDTO getXmlSubmitRequestEventDTO() {
		return xmlSubmitRequestEventDTO;
	}

	/**
	 * @param xmlSubmitRequestEventDTO the xmlSubmitRequestEventDTO to set
	 */
	public void setXmlSubmitRequestEventDTO(XmlSubmitRequestEventDTO xmlSubmitRequestEventDTO) {
		this.xmlSubmitRequestEventDTO = xmlSubmitRequestEventDTO;
	}
	
}
