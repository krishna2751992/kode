/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlRequestListDTO {

	private List<XmlRequestDTO> XmlRequestDTOLst;

	/**
	 * @return the xmlRequestDTOLst
	 */
	@XmlElement(name = "REQUEST")
	public List<XmlRequestDTO> getXmlRequestDTOLst() {
		return XmlRequestDTOLst;
	}

	/**
	 * @param xmlRequestDTOLst the xmlRequestDTOLst to set
	 */
	public void setXmlRequestDTOLst(List<XmlRequestDTO> xmlRequestDTOLst) {
		XmlRequestDTOLst = xmlRequestDTOLst;
	}
	
	
}
