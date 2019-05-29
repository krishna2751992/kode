/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ColumnRulesDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -24899479556053301L;

	private int columnId;
	private String columnName;
	private boolean enableFinder;
	private String operators;
	private boolean viewable;
	private String lookupString;
	private boolean searchable;
	private boolean enableHICN;
	private boolean selectFinder;
	private int length;
	private int valType;
	private String enableIcd;
	private List<LookupDTO> lookups;
	private String whereClause;
	private boolean headerStartPositionEnabled;
	/**
	 * @return the columnID
	 */
	public int getColumnId() {
		return columnId;
	}
	/**
	 * @param columnID the columnID to set
	 */
	public void setColumnId(int columnID) {
		this.columnId = columnId;
	}
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the enableFinder
	 */
	public boolean isEnableFinder() {
		return enableFinder;
	}
	/**
	 * @param enableFinder the enableFinder to set
	 */
	public void setEnableFinder(boolean enableFinder) {
		this.enableFinder = enableFinder;
	}
	/**
	 * @return the operators
	 */
	public String getOperators() {
		return operators;
	}
	/**
	 * @param operators the operators to set
	 */
	public void setOperators(String operators) {
		this.operators = operators;
	}
	/**
	 * @return the viewable
	 */
	public boolean isViewable() {
		return viewable;
	}
	/**
	 * @param viewable the viewable to set
	 */
	public void setViewable(boolean viewable) {
		this.viewable = viewable;
	}
	/**
	 * @return the lookupString
	 */
	public String getLookupString() {
		return lookupString;
	}
	/**
	 * @param lookupString the lookupString to set
	 */
	public void setLookupString(String lookupString) {
		this.lookupString = lookupString;
	}
	/**
	 * @return the searchable
	 */
	public boolean isSearchable() {
		return searchable;
	}
	/**
	 * @param searchable the searchable to set
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	/**
	 * @return the enableHICN
	 */
	public boolean isEnableHICN() {
		return enableHICN;
	}
	/**
	 * @param enableHICN the enableHICN to set
	 */
	public void setEnableHICN(boolean enableHICN) {
		this.enableHICN = enableHICN;
	}
	/**
	 * @return the selectFinder
	 */
	public boolean isSelectFinder() {
		return selectFinder;
	}
	/**
	 * @param selectFinder the selectFinder to set
	 */
	public void setSelectFinder(boolean selectFinder) {
		this.selectFinder = selectFinder;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the valType
	 */
	public int getValType() {
		return valType;
	}
	/**
	 * @param valType the valType to set
	 */
	public void setValType(int valType) {
		this.valType = valType;
	}
	/**
	 * @return the enableIcd
	 */
	public String getEnableIcd() {
		return enableIcd;
	}
	/**
	 * @param enableIcd the enableIcd to set
	 */
	public void setEnableIcd(String enableIcd) {
		this.enableIcd = enableIcd;
	}
	
	/**
	 * @return the lookups
	 */
	public List<LookupDTO> getLookups() {
		return lookups;
	}
	/**
	 * @param lookups the lookups to set
	 */
	public void setLookups(List<LookupDTO> lookups) {
		this.lookups = lookups;
	}
	
	/**
	 * @return the whereClause
	 */
	public String getWhereClause() {
		return whereClause;
	}
	/**
	 * @param whereClause the whereClause to set
	 */
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
	/**
	 * @return the headerStartPositionEnabled
	 */
	public boolean isHeaderStartPositionEnabled() {
		return headerStartPositionEnabled;
	}
	/**
	 * @param headerStartPositionEnabled the headerStartPositionEnabled to set
	 */
	public void setHeaderStartPositionEnabled(boolean headerStartPositionEnabled) {
		this.headerStartPositionEnabled = headerStartPositionEnabled;
	}
	
}
