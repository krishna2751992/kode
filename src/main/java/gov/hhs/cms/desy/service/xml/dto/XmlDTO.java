/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jagannathan.Narashim
 *
 */
@XmlRootElement(name = "DESY_REQ")
public class XmlDTO {

	private XmlHeaderDTO xmlHeaderDTO;
	
	private XmlBody xmlBody;

	
	
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
	 * @return the xmlBody
	 */
	@XmlElement(name = "BODY")
	public XmlBody getXmlBody() {
		return xmlBody;
	}

	/**
	 * @param xmlBody the xmlBody to set
	 */
	public void setXmlBody(XmlBody xmlBody) {
		this.xmlBody = xmlBody;
	}

	
	
}
