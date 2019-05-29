/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jagannathan.Narashim
 *
 */
public class FinderFileDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8193280670921429202L;
	@SafeHtml
	private String fileName;
	@SafeHtml
	private int startPosition;
	@SafeHtml
	private int headerStartPosition;
	@JsonProperty("finderColumn")
	private ColumnDTO finderColumn;
	@SafeHtml
	private int groupID;
	@SafeHtml
	private String filIcdIndCd;
	
	/**
	 * @return the fileName
	 */
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
	 * @return the finderColumn
	 */
	public ColumnDTO getFinderColumn() {
		return finderColumn;
	}
	/**
	 * @param finderColumn the finderColumn to set
	 */
	public void setFinderColumn(ColumnDTO finderColumn) {
		this.finderColumn = finderColumn;
	}
	/**
	 * @return the groupID
	 */
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
