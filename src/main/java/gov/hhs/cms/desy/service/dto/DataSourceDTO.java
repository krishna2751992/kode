/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5564959303266068479L;

	private int dataSourceId;
	@SafeHtml
	private String name;
	@SafeHtml
	private List<String> states;
	@SafeHtml
	private List<String> dataTypes;
	/**
	 * @return the dataSourceID
	 */
	public int getDataSourceId() {
		return dataSourceId;
	}
	/**
	 * @param dataSourceID the dataSourceID to set
	 */
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
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
	 * @return the states
	 */
	public List<String> getStates() {
		return states;
	}
	/**
	 * @param states the states to set
	 */
	public void setStates(List<String> states) {
		this.states = states;
	}
	/**
	 * @return the dataTypes
	 */
	public List<String> getDataTypes() {
		return dataTypes;
	}
	/**
	 * @param dataTypes the dataTypes to set
	 */
	public void setDataTypes(List<String> dataTypes) {
		this.dataTypes = dataTypes;
	}	
	
}
