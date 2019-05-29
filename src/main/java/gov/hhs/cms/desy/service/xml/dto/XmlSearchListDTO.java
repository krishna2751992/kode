/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import gov.hhs.cms.desy.service.dto.FilterDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlSearchListDTO {
//@XmlElement(name = "SEARCH_ELEMENT")
	private List<XmlFilterDTO> xmlFilterDTOLst;

	/**
	 * @return the xmlFilterDTOLst
	 */
	@XmlElement(name = "SEARCH_ELEMENT")
	public List<XmlFilterDTO> getXmlFilterDTOLst() {
		return xmlFilterDTOLst;
	}

	/**
	 * @param xmlFilterDTOLst the xmlFilterDTOLst to set
	 */
	public void setXmlFilterDTOLst(List<XmlFilterDTO> xmlFilterDTOLst) {
		this.xmlFilterDTOLst = xmlFilterDTOLst;
	}	
	
}
