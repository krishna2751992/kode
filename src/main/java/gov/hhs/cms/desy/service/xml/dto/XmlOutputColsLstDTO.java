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
public class XmlOutputColsLstDTO {
	
	private List<String> elmIdLst;
	
	/**
	 * @return the elmIdLst
	 */
	@XmlElement(name = "ELM_ID")
	public List<String> getElmIdLst() {
		return elmIdLst;
	}

	/**
	 * @param elmIdLst the elmIdLst to set
	 */
	public void setElmIdLst(List<String> elmIdLst) {
		this.elmIdLst = elmIdLst;
	}
}
