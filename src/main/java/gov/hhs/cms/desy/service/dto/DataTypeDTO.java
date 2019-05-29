/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTypeDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7427559583496482493L;
	@SafeHtml
	private int dataTypeID;
	@SafeHtml
	private String name;
	@SafeHtml
	private List<String> dataSourceYears;
	
	/**
	 * @return the dataTypeID
	 */
	public int getDataTypeID() {
		return dataTypeID;
	}
	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(int dataTypeID) {
		this.dataTypeID = dataTypeID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the dataSourceYears
	 */
	public List<String> getDataSourceYears() {
		return dataSourceYears;
	}
	/**
	 * @param dataSourceYears the dataSourceYears to set
	 */
	public void setDataSourceYears(List<String> dataSourceYears) {
		this.dataSourceYears = dataSourceYears;
	}	
	
}
