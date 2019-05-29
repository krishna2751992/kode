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
public class XmlCancelRequestDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	private XmlCancelRequestBodyDTO xmlCancelRequestBodyDTO;
	
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
	 * @return the xmlCancelRequestBodyDTO
	 */
	@XmlElement(name = "BODY")
	public XmlCancelRequestBodyDTO getXmlCancelRequestBodyDTO() {
		return xmlCancelRequestBodyDTO;
	}
	/**
	 * @param xmlCancelRequestBodyDTO the xmlCancelRequestBodyDTO to set
	 */
	public void setXmlCancelRequestBodyDTO(XmlCancelRequestBodyDTO xmlCancelRequestBodyDTO) {
		this.xmlCancelRequestBodyDTO = xmlCancelRequestBodyDTO;
	}	
	
}
