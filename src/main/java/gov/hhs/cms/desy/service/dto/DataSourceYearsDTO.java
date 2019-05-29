/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DataSourceYearsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 891414896670918920L;
	private int year;
	private String updateDate;
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
