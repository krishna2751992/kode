/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlRequestDTO {

	private String dataYear;

	/**
	 * @return the dataYear
	 */
	@XmlElement(name = "DATA_YEAR")
	public String getDataYear() {
		return dataYear;
	}

	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(String dataYear) {
		this.dataYear = dataYear;
	}
	
	
}
