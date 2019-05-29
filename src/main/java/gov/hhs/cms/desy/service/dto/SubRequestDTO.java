/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *description : this class is being used to store information about sub requests under one super Requests.
 */
public class SubRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5307042257573506775L;
	private int requestID;
	private int dataYear;
	private String outputFileName;
	private String outputCopybook;
	private String recordCount;
	private String droppedRecordCount;
	private String droppedFileName;
	private String requestStatus;
	private String updateMonthYear;
	private boolean isCompleted;
	
	/**
	 * @return the requestID
	 */
	public int getRequestID() {
		return requestID;
	}
	/**
	 * @param requestID the requestID to set
	 */
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	/**
	 * @return the dataYear
	 */
	public int getDataYear() {
		return dataYear;
	}
	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}
	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}
	/**
	 * @param outputFileName the outputFileName to set
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	/**
	 * @return the outputCopybook
	 */
	public String getOutputCopybook() {
		return outputCopybook;
	}
	/**
	 * @param outputCopybook the outputCopybook to set
	 */
	public void setOutputCopybook(String outputCopybook) {
		this.outputCopybook = outputCopybook;
	}
	/**
	 * @return the recordCount
	 */
	public String getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the droppedRecordCount
	 */
	public String getDroppedRecordCount() {
		return droppedRecordCount;
	}
	/**
	 * @param droppedRecordCount the droppedRecordCount to set
	 */
	public void setDroppedRecordCount(String droppedRecordCount) {
		this.droppedRecordCount = droppedRecordCount;
	}
	/**
	 * @return the droppedFileName
	 */
	public String getDroppedFileName() {
		return droppedFileName;
	}
	/**
	 * @param droppedFileName the droppedFileName to set
	 */
	public void setDroppedFileName(String droppedFileName) {
		this.droppedFileName = droppedFileName;
	}
	/**
	 * @return the requestStatus
	 */
	public String getRequestStatus() {
		return requestStatus;
	}
	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(String status) {
		if(status!=null)
		{
			requestStatus = status;
			if(status.trim().equalsIgnoreCase("COMPLETED/PAD") 
			   ||status.trim().equalsIgnoreCase("COMPLETED/NO OUTPUT")
			   ||status.trim().equalsIgnoreCase("COMPLETED") )
			   {
			   	isCompleted=true;
			   }
			   else
			   {
				isCompleted=false;
			   }
		}else
		{
			requestStatus = status;
		}
		
	}
	/**
	 * @return the updateMonthYear
	 */
	public String getUpdateMonthYear() {
		return updateMonthYear;
	}
	/**
	 * @param updateMonthYear the updateMonthYear to set
	 */
	public void setUpdateMonthYear(String updateMonthYear) {
		this.updateMonthYear = updateMonthYear;
	}
	/**
	 * @return the isCompleted
	 */
	public boolean isCompleted() {
		return isCompleted;
	}
	/**
	 * @param isCompleted the isCompleted to set
	 */
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}	
	
}
