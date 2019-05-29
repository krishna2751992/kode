/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlUpdateStatusBodyDTO {

	private XmlUpdateStatusEventDTO xmlUpdateStatusEventDTO;

	/**
	 * @return the xmlUpdateStatusEventDTO
	 */
	@XmlElement(name = "EVENT")
	public XmlUpdateStatusEventDTO getXmlUpdateStatusEventDTO() {
		return xmlUpdateStatusEventDTO;
	}

	/**
	 * @param xmlUpdateStatusEventDTO the xmlUpdateStatusEventDTO to set
	 */
	public void setXmlUpdateStatusEventDTO(XmlUpdateStatusEventDTO xmlUpdateStatusEventDTO) {
		this.xmlUpdateStatusEventDTO = xmlUpdateStatusEventDTO;
	}
	
	
}
