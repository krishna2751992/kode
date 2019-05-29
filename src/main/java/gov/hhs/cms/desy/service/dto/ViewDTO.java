/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import gov.hhs.cms.desy.web.rest.util.ValidInputString;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ViewDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7637982261111809471L;

	private int viewID;
	@NotNull
	@ValidInputString(message = "contains suspicious XSS string")
	private String viewName;
	private int dataSourceID;
	private int dataTypeID;
	private List<ColumnDTO> columnsDTO;
	private int userNum;
	/**
	 * @return the viewID
	 */
	public int getViewID() {
		return viewID;
	}
	/**
	 * @param viewID the viewID to set
	 */
	public void setViewID(int viewID) {
		this.viewID = viewID;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the dataSourceID
	 */
	public int getDataSourceID() {
		return dataSourceID;
	}
	/**
	 * @param dataSourceID the dataSourceID to set
	 */
	public void setDataSourceID(int dataSourceID) {
		this.dataSourceID = dataSourceID;
	}
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
	 * @return the columnsDTO
	 */
	public List<ColumnDTO> getColumnsDTO() {
		return columnsDTO;
	}
	/**
	 * @param columnsDTO the columnsDTO to set
	 */
	public void setColumnsDTO(List<ColumnDTO> columnsDTO) {
		this.columnsDTO = columnsDTO;
	}
	/**
	 * @return the userNum
	 */
	public int getUserNum() {
		return userNum;
	}
	/**
	 * @param userNum the userNum to set
	 */
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}	
	
}
