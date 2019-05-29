/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlFinderFileDTO {

	private String fileName;
	private int startPosition;
	private int headerStartPosition;
	private int columnID;
	private int groupID;
	private String filIcdIndCd;
	
	/**
	 * @return the fileName
	 */
	@XmlElement(name = "FIL_NM")
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the startPosition
	 */
	@XmlElement(name = "STTG_POS")
	public int getStartPosition() {
		return startPosition;
	}
	/**
	 * @param startPosition the startPosition to set
	 */
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	/**
	 * @return the headerStartPosition
	 */
	@XmlElement(name = "HDR_START_POS")
	public int getHeaderStartPosition() {
		return headerStartPosition;
	}
	/**
	 * @param headerStartPosition the headerStartPosition to set
	 */
	public void setHeaderStartPosition(int headerStartPosition) {
		this.headerStartPosition = headerStartPosition;
	}
	/**
	 * @return the columnID
	 */
	@XmlElement(name = "FINDER_ELE_NB")
	public int getColumnID() {
		return columnID;
	}
	/**
	 * @param columnID the columnID to set
	 */
	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}
	/**
	 * @return the groupID
	 */
	@XmlElement(name = "GRP_ID")
	public int getGroupID() {
		return groupID;
	}
	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	/**
	 * @return the filIcdIndCd
	 */
	@XmlElement(name = "FIL_ICD_IND_CD")
	public String getFilIcdIndCd() {
		return filIcdIndCd;
	}
	/**
	 * @param filIcdIndCd the filIcdIndCd to set
	 */
	public void setFilIcdIndCd(String filIcdIndCd) {
		this.filIcdIndCd = filIcdIndCd;
	}
	
	
}
