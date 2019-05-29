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
@XmlRootElement(name = "DESY_REQ")
public class XmlSubmitRequestDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	
	private XmlSubmitRequestBodyDTO xmlSubmitRequestBodyDTO;
	
	
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
	 * @return the xmlSubmitRequestBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlSubmitRequestBodyDTO getXmlSubmitRequestBodyDTO() {
		return xmlSubmitRequestBodyDTO;
	}

	/**
	 * @param xmlSubmitRequestBodyDTO the xmlSubmitRequestBodyDTO to set
	 */
	public void setXmlSubmitRequestBodyDTO(XmlSubmitRequestBodyDTO xmlSubmitRequestBodyDTO) {
		this.xmlSubmitRequestBodyDTO = xmlSubmitRequestBodyDTO;
	}	

}
