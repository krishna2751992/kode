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
public class XmlFinderFileLstDTO {

	private List<XmlFinderFileDTO> xmlFinderFileDTOLst;

	/**
	 * @return the xmlFinderFileDTOLst
	 */
	@XmlElement(name = "INPUT_FILE")
	public List<XmlFinderFileDTO> getXmlFinderFileDTOLst() {
		return xmlFinderFileDTOLst;
	}

	/**
	 * @param xmlFinderFileDTOLst the xmlFinderFileDTOLst to set
	 */
	public void setXmlFinderFileDTOLst(List<XmlFinderFileDTO> xmlFinderFileDTOLst) {
		this.xmlFinderFileDTOLst = xmlFinderFileDTOLst;
	}
	
	
}
