/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.provider.jaxb.XmlHeader;

/**
 * @author Jagannathan.Narashim
 *
 */
@XmlRootElement(name = "DESY-REQ")
public class XmlUpdateStatusDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	private XmlUpdateStatusBodyDTO xmlUpdateStatusBodyDTO;	

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
	 * @return the xmlUpdateStatusBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlUpdateStatusBodyDTO getXmlUpdateStatusBodyDTO() {
		return xmlUpdateStatusBodyDTO;
	}

	/**
	 * @param xmlUpdateStatusBodyDTO the xmlUpdateStatusBodyDTO to set
	 */
	public void setXmlUpdateStatusBodyDTO(XmlUpdateStatusBodyDTO xmlUpdateStatusBodyDTO) {
		this.xmlUpdateStatusBodyDTO = xmlUpdateStatusBodyDTO;
	}
	
	
}
