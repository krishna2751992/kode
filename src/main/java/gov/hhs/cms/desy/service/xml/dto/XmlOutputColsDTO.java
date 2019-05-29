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
public class XmlOutputColsDTO {
	
	private List<String> elmIdLst;
	
	/**
	 * @return the elmIdLst
	 */
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
