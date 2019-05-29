/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import gov.hhs.cms.desy.service.util.DateFunctions;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDTO implements Serializable {
	private static final long serialVersionUID = 8875112568401203603L;

	private int superID;

	private int requestID;

	@JsonProperty("userDetails")
	private UserDTO userDTO;

	@JsonProperty("duaDetails")
	private DuaDTO duaDTO;

	@JsonProperty("dataSource")
	private DataSourceDTO dataSourceDTO;

	@JsonProperty("dataType")
	private DataTypeDTO dataTypeDTO;

	@JsonProperty("state")
	private StatesDTO statesDTO;

	@JsonProperty("outputType")
	private OutputTypeDTO outputTypeDTO;


	private SearchDTO searchCriteria;

	@JsonProperty("outputColumns")
	private List<ColumnDTO> outputColumns;

	@JsonProperty("filter")
	private List<FilterDTO> filterDTOLst;

	@JsonProperty("finderFile")
	private List<FinderFileDTO> finderFileDTOLst;

	private boolean commaDelimited;

	@SafeHtml
	private String requestDesc;

	@JsonIgnore
	@SafeHtml
	private String commaDelimitedDesc;

	@JsonIgnore
	private RecipientDTO recipientDTO;

	@JsonIgnore
	private Date dateCreated;

	@SafeHtml
	@JsonIgnore
	private String status;

	@JsonIgnore
	private FormatDTO formatDTO;

	@SafeHtml
	private String dataDescriptionCode;

	@JsonIgnore
	private MediaTypeDTO mediaTypeDTO;

	@JsonIgnore
	private int prevRequestID;

	@SafeHtml
	private String dropRecordRequired;

	@JsonIgnore
	private int customViewID;

	@JsonIgnore
	private Date dateModified;

	private boolean isSuperRequest;

	@SafeHtml
	private String outputFileIdentifier;

	@JsonIgnore
	private String dataYear;

	private int dropRecordRequiredInt;

	@JsonIgnore
	private boolean befPufView;

	@JsonIgnore
	private boolean isSavedRequest;

	@JsonIgnore
	private int pagesVisited;

	@JsonIgnore
	private List<SubRequestDTO> subRequests;

	@JsonIgnore
	private ApprovalDTO approval;

	@JsonIgnore
	private List<String> errorKeys;
	@JsonIgnore
	private List<String> errorMsgs;

	// below properties are created for JSON conversion
	@JsonIgnore
	private Date formattedTimestampDateCreated;
	@JsonIgnore
	private Date formattedDateModified;
	@JsonIgnore
	private Date formattedDateCreated;

	/**
	 * @return
	 */
	public ApprovalDTO getApproval() {
		return approval;
	}

	/**
	 * @return
	 */
	public boolean isCommaDelimited() {
		return commaDelimited;
	}

	/**
	 * @return
	 */
	public String getCommaDelimitedDesc() {
		return commaDelimitedDesc;
	}

	/**
	 * @return
	 */
	public int getCustomViewID() {
		return customViewID;
	}

	/**
	 * @return
	 */
	public String getDataDescriptionCode() {
		return dataDescriptionCode;
	}

	/**
	 * @return
	 */
	public DataSourceDTO getDataSource() {
		return dataSourceDTO;
	}

	/**
	 * @return
	 */
	public DataTypeDTO getDataType() {
		return dataTypeDTO;
	}

	/**
	 * @return
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @return
	 */
	public Date getDateModified() {
		return dateModified;
	}

	/**
	 * @return
	 */
	public String getRequestDesc() {
		return requestDesc;
	}

	/**
	 * @return
	 */
	public String getDropRecordRequired() {
		return dropRecordRequired;
	}

	/**
	 * @return
	 */
	public DuaDTO getDua() {
		return duaDTO;
	}

	/**
	 * @return
	 */
	public FormatDTO getFormat() {
		return formatDTO;
	}

	/**
	 * @return
	 */
	public MediaTypeDTO getMediaType() {
		return mediaTypeDTO;
	}

	/**
	 * @return
	 */
	public OutputTypeDTO getOutputType() {
		return outputTypeDTO;
	}

	/**
	 * @return
	 */
	public RecipientDTO getRecipient() {
		return recipientDTO;
	}

	/**
	 * @return
	 */
	public int getPrevRequestID() {
		return prevRequestID;
	}

	/**
	 * @return
	 */
	public int getRequestID() {
		return requestID;
	}

	/**
	 * @return
	 */
	public StatesDTO getState() {
		return statesDTO;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public int getSuperID() {
		return superID;
	}

	/**
	 * @return
	 */
	public UserDTO getUser() {
		return userDTO;
	}

	public String getFormattedDateModified() {
		return DateFunctions.formatDate(dateModified);
	}

	public String getFormattedDateCreated() {
		return DateFunctions.formatDate(dateCreated);
	}

	public String getFormattedTimestampDateCreated() {
		return DateFunctions.formatTimeStampDate(dateCreated);

	}

	/**
	 * @param approval
	 */
	public void setApproval(ApprovalDTO approval) {
		this.approval = approval;
	}

	/**
	 * @param b
	 */
	public void setCommaDelimited(boolean b) {
		commaDelimited = b;

		// set commaDelimitedDesc
		if (commaDelimited) {
			setCommaDelimitedDesc("Yes");
		} else {
			setCommaDelimitedDesc("No");
		}
	}

	/**
	 * @param s
	 */
	public void setCommaDelimitedDesc(String s) {
		commaDelimitedDesc = s;
	}

	/**
	 * @param i
	 */
	public void setCustomViewID(int i) {
		customViewID = i;
	}

	/**
	 * @param string
	 */
	public void setDataDescriptionCode(String string) {
		dataDescriptionCode = string;
	}

	/**
	 * @param source
	 */
	public void setDataSource(DataSourceDTO source) {
		DataSourceDTO dataSource = new DataSourceDTO();
		dataSource.setDataSourceId(source.getDataSourceId());
		dataSource.setName(source.getName());
		this.dataSourceDTO = dataSource;
	}

	/**
	 * @param type
	 */
	public void setDataType(DataTypeDTO type) {
		DataTypeDTO dataType = new DataTypeDTO();
		dataType.setDataTypeID(type.getDataTypeID());
		dataType.setName(type.getName());
		this.dataTypeDTO = dataType;

	}

	/**
	 * @param date
	 */
	public void setDateCreated(Date date) {
		dateCreated = date;
		setFormattedTimestampDateCreated(dateCreated);
		setFormattedDateCreated(dateCreated);
	}

	/**
	 * @param date
	 */
	public void setDateModified(Date date) {
		dateModified = date;
		setFormattedDateModified(dateModified);
	}

	/**
	 * @param formattedTimestampDateCreated the formattedTimestampDateCreated to set
	 */
	public void setFormattedTimestampDateCreated(Date formattedTimestampDateCreated) {
		this.formattedTimestampDateCreated = formattedTimestampDateCreated;
	}

	/**
	 * @param formattedDateModified the formattedDateModified to set
	 */
	public void setFormattedDateModified(Date formattedDateModified) {
		this.formattedDateModified = formattedDateModified;
	}

	/**
	 * @param formattedDateCreated the formattedDateCreated to set
	 */
	public void setFormattedDateCreated(Date formattedDateCreated) {
		this.formattedDateCreated = formattedDateCreated;
	}

	/**
	 * @param string
	 */
	public void setRequestDesc(String string) {
		requestDesc = string;
	}

	/**
	 * @param b
	 */
	public void setDropRecordRequired(String c) {
		dropRecordRequired = c;
	}

	/**
	 * @param header
	 */
	public void setDua(DuaDTO header) {
		duaDTO = new DuaDTO();
		duaDTO.setDuaNumber(header.getDuaNumber());
		duaDTO.setExpirationDate(header.getExpirationDate());
		duaDTO.setRequestor(header.getRequestor());
		duaDTO.setStudyName(header.getStudyName());
		duaDTO.setReturnRequired(header.getReturnRequired());
		duaDTO.setEncryptionSwitch(header.getEncryptionSwitch());
		duaDTO.setFtapeSwitch(header.getFtapeSwitch());
		duaDTO.setDesyExpirationDate(header.getDesyExpirationDate());
		duaDTO.setDuaClosed(header.isDuaClosed());
		duaDTO.setSupressFlag(header.isSupressFlag());

	}

	/**
	 * @param format
	 */
	public void setFormat(FormatDTO format) {
		this.formatDTO = format;
	}

	/**
	 * @param type
	 */
	public void setMediaType(MediaTypeDTO type) {
		mediaTypeDTO = type;
	}

	/**
	 * @param type
	 */
	public void setOutputType(OutputTypeDTO type) {
		outputTypeDTO = type;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(RecipientDTO recipient) {
		this.recipientDTO = recipient;
	}

	/**
	 * @param i
	 */
	public void setPrevRequestID(int i) {
		prevRequestID = i;
	}

	/**
	 * @param i
	 */
	public void setRequestID(int i) {
		requestID = i;
	}

	/**
	 * @param state
	 */
	public void setState(StatesDTO state) {
		this.statesDTO = state;
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		status = string;
	}

	/**
	 * @param i
	 */
	public void setSuperID(int i) {
		superID = i;
	}

	/**
	 * @param user
	 */
	public void setUser(UserDTO user) {
		this.userDTO = user;
	}

	/**
	 * @return
	 */
	public boolean isSuperRequest() {
		return isSuperRequest;
	}

	/**
	 * @param b
	 */
	public void setSuperRequest(boolean b) {
		isSuperRequest = b;
	}

	/**
	 * @return
	 */
	public String getOutputFileIdentifier() {
		return outputFileIdentifier;
	}

	/**
	 * @param string
	 */
	public void setOutputFileIdentifier(String iden) {
		if (iden != null && iden.length() > 0)
			outputFileIdentifier = iden.toUpperCase();
		else
			outputFileIdentifier = iden;
	}

	/**
	 * @return
	 */
	public String getDataYear() {
		return dataYear;
	}

	/**
	 * @param i
	 */
	public void setDataYear(String s) {
		dataYear = s;
	}

	/**
	 * @param i
	 */
	public void setView(OutputTypeDTO outputType) {
		if (outputType != null) {
			String id = outputType.getViewID();
			if (id.startsWith("V")) {
				outputType.setViewID(id.substring(1), "V");
				customViewID = 0;
			} else if (id.startsWith("C")) {
				if (Character.toString(id.charAt(id.length() - 1)).equalsIgnoreCase("S")) {
					befPufView = true;
					outputType.setViewID("2", "C", 0);
					customViewID = Integer.parseInt(id.substring(1, id.length() - 1));
				} else if (Character.toString(id.charAt(id.length() - 1)).equalsIgnoreCase("U")) {
					befPufView = false;
					outputType.setViewID("-1", "C", 1);
					customViewID = Integer.parseInt(id.substring(1, id.length() - 1));
				}

			}
			this.outputTypeDTO = outputType;
		}
	}

	/**
	 * @return
	 */
	public int getDropRecordRequiredInt() {
		return dropRecordRequiredInt;
	}

	/**
	 * @param i
	 */
	public void setDropRecordRequiredInt(int i) {
		dropRecordRequiredInt = i;
	}

	/**
	 * @return
	 */
	public boolean isBefPufView() {
		return befPufView;
	}

	/**
	 * @param b
	 */
	public void setBefPufView(boolean b) {
		befPufView = b;
	}

	/**
	 * @return
	 */
	public boolean isSavedRequest() {
		return isSavedRequest;
	}

	/**
	 * @param b
	 */
	public void setSavedRequest(boolean b) {
		isSavedRequest = b;
	}

	/**
	 * @return
	 */
	public int getPagesVisited() {
		return pagesVisited;
	}

	/**
	 * @param i
	 */
	public void setPagesVisited(int i) {
		pagesVisited = i;
	}

	/**
	 * @return the searchCriteria
	 */
	public SearchDTO getSearchCriteria() {
		return searchCriteria;
	}

	/**
	 * @param searchCriteria the searchCriteria to set
	 */
	public void setSearchCriteria(SearchDTO searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	/**
	 * @return the subRequests
	 */
	public List<SubRequestDTO> getSubRequests() {
		return subRequests;
	}

	/**
	 * @return
	 */
	/*
	 * public int getSubRequestsCount() { return this.subRequests.size(); }
	 */

	/**
	 * @param subRequests the subRequests to set
	 */
	public void setSubRequests(List<SubRequestDTO> subRequests) {
		this.subRequests = subRequests;
	}

	/**
	 * @return the outputColumns
	 */
	public List<ColumnDTO> getOutputColumns() {
		return outputColumns;
	}

	/**
	 * @param outputColumns the outputColumns to set
	 */
	public void setOutputColumns(List<ColumnDTO> outputColumns) {
		this.outputColumns = outputColumns;
	}

	/**
	 * @return the errorKeys
	 */
	public List<String> getErrorKeys() {
		return errorKeys;
	}

	/**
	 * @param errorKeys the errorKeys to set
	 */
	public void setErrorKeys(List<String> errorKeys) {
		this.errorKeys = errorKeys;
	}

	/**
	 * @return the errorMsgs
	 */
	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

	/**
	 * @param errorMsgs the errorMsgs to set
	 */
	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	/**
	 * Method to check whether Dropped Records Option should be enabled or disabled
	 * on Output screen.
	 * 
	 * @return
	 */
	public boolean checkDroppedRecords() {
		boolean droprecords = false;
		if (this.dataSourceDTO != null && this.duaDTO != null) {
			if (this.dataSourceDTO.getDataSourceId() == 1) {
				if (this.dataTypeDTO != null) {
					if (this.dataTypeDTO.getDataTypeID() == 0) {
						if (checkYears() && checkFilters() && checkHICAN()) {
							droprecords = true;
						} else {
							droprecords = false;
						}

					} else {
						droprecords = true;
					}
				}
			} else {
				droprecords = false;
			}
		}

		return droprecords;
	}

	private boolean checkYears() {
		boolean yearInRange = true;
		List<SubRequestDTO> subReq = this.getSubRequests();
		if (subReq != null) {
			for (int i = 0; i < subReq.size(); i++) {
				int year = subReq.get(i).getDataYear();
				if (year <= 1997) {
					yearInRange = false;
					;
					break;
				}
			}
		}
		return yearInRange;

	}

	public boolean checkHICAN() {
		boolean hicanSelected = false;
		int hicancount = 0;
		SearchDTO search = this.getSearchCriteria();
		if (search != null) {
			List<FinderFileDTO> finderFile = search.getFinderFiles();
			if (finderFile != null) {
				for (int i = 0; i < finderFile.size(); i++) {
					ColumnDTO col = finderFile.get(i).getFinderColumn();
					if (col != null) {
						int colID = col.getColumnID();
						if (colID == 84)
							hicancount++;
					}
				}
			}
		}

		if (hicancount == 1) {
			hicanSelected = true;
		}
		return hicanSelected;

	}

	private boolean checkFilters() {
		boolean checkFilter = false;
		SearchDTO search = this.getSearchCriteria();
		if (search != null) {
			List<FilterDTO> filter = search.getFilters();
			if (filter != null) {
				if (filter.size() == 1)
					checkFilter = true;
			}
		}
		return checkFilter;
	}

	public boolean checkFinderFileView() {
		boolean disableFinderFileView = false;
		SearchDTO search = this.getSearchCriteria();
		if (search != null) {
			List<FilterDTO> filter = search.getFilters();
			if (filter != null) {
				for (int i = 0; i < filter.size(); i++) {
					if (filter.get(i) != null && filter.get(i).getWhereClause() != null) {
						if (filter.get(i).getWhereClause().toUpperCase().startsWith("Y,H")
								|| filter.get(i).getWhereClause().toUpperCase().startsWith("H")) {
							disableFinderFileView = true;
							break;
						}
					}
				}

			}
		}
		return disableFinderFileView;
	}

	/**
	 * This method validates the request when user clicks the submit button on
	 * summary screen. Most of these validations are already being done on
	 * individual screens as well but there are cases when user can reach up to
	 * summary page with incomplete information using back button, as a result of
	 * that submit will fail.
	 * 
	 * @param sessionDSRules -- Requires DataSource and data type rules as in order
	 *                       to check for Reicipients, State , years and must search
	 *                       option, rules need to be checked before checking the
	 *                       actual values.
	 * @return
	 */

	public boolean validateRequest(DataSourceRulesDTO sessionDSRules) {
		DataSourceRulesDTO dataSourceRules = null;
		DataTypeRuleDTO dataTypeRules = null;
		// Data source rules and data type rules must be passed
		if (sessionDSRules != null && sessionDSRules.getDataTypeRuleDTO() != null) {
			dataSourceRules = sessionDSRules;
			dataTypeRules = sessionDSRules.getDataTypeRuleDTO();
			this.errorKeys = new ArrayList<String>();
			// checking Dua Number

			if (!(this.duaDTO != null && this.duaDTO.getDuaNumber() > 0)) {
				this.errorKeys.add(new String("submitrequest.invalid.dua"));
			}
			// Checking Data source
			if (!(this.dataSourceDTO != null && this.dataSourceDTO.getDataSourceId() > 0)) {
				this.errorKeys.add(new String("submitrequest.invalid.datasource"));
			}
			// Checking Data type
			if (!(this.dataTypeDTO != null && this.dataTypeDTO.getDataTypeID() >= 0)) {
				this.errorKeys.add(new String("submitrequest.invalid.datatype"));
			}
			// Checking Recipient
			if (this.duaDTO != null && (this.duaDTO.getFtapeSwitch() == 'Y' || this.duaDTO.getFtapeSwitch() == 'y')) {
				if (!(this.recipientDTO != null && this.recipientDTO.getRecipID() > 0)) {
					this.errorKeys.add(new String("submitrequest.invalid.recip"));
				}
			}
			// Checking State
			if (dataSourceRules.isSelectState()) {
				if (!(this.statesDTO != null
						&& (this.statesDTO.getStateCode() != null && this.statesDTO.getStateCode().length() > 0))) {
					this.errorKeys.add(new String("submitrequest.invalid.state"));
				}
			}

			// Checking Years
			if (dataTypeRules.isYearEnabled()) {
				if (!(this.subRequests != null && (this.subRequests.size() > 0))) {
					this.errorKeys.add(new String("submitrequest.invalid.years"));
				}
			}
			// Checking mustSearch
			boolean mustSearch = false;
			StatesDTO state = this.getState();
			String stateCode = null;
			if (state != null) {
				stateCode = state.getStateCode();
			}
			// calling method defined in dataSourceRule class to check for must search
			// option
			mustSearch = dataSourceRules.checkMustSearch(stateCode);
			boolean searchSelected = false;
			if (mustSearch) {
				if (this.searchCriteria != null && this.searchCriteria.getFilters() != null) {
					List<FilterDTO> filters = this.searchCriteria.getFilters();
					if (filters != null && filters.size() > 0) {
						searchSelected = true;
					}
				}

				if (!searchSelected) {
					this.errorKeys.add(new String("submitrequest.invalid.mustsearchcriteria"));
				}
			}
			// Checking output Type
			if (!(this.outputTypeDTO != null
					&& (this.outputTypeDTO.getViewID() != null && this.outputTypeDTO.getViewID().length() > 0))) {
				this.errorKeys.add(new String("submitrequest.invalid.outputtype"));
			}

			// checking dropped records

			if (!(this.dropRecordRequired != null && this.dropRecordRequired.length() > 0)) {
				this.errorKeys.add(new String("submitrequest.invalid.droppedrec"));
			}

			// checking selected fields if user selected Select Available Fields option
			if (this.outputTypeDTO != null && this.outputTypeDTO.getViewID() != null
					&& this.outputTypeDTO.getViewID().equalsIgnoreCase("V2")) {
				if (this.outputColumns != null && this.outputColumns.size() == 0) {
					this.errorKeys.add(new String("submitrequest.invalid.availablefields"));
				}
			}

			if (this.errorKeys.size() > 0) {
				return false;
			} else {
				return true;
			}

		} else {
			return false;
		}

	}

	/**
	 * @return the filterDTOLst
	 */
	public List<FilterDTO> getFilterDTOLst() {
		return filterDTOLst;
	}

	/**
	 * @param filterDTOLst the filterDTOLst to set
	 */
	public void setFilterDTOLst(List<FilterDTO> filterDTOLst) {
		this.filterDTOLst = filterDTOLst;
	}

	/**
	 * @return the finderFileDTOLst
	 */
	public List<FinderFileDTO> getFinderFileDTOLst() {
		return finderFileDTOLst;
	}

	/**
	 * @param finderFileDTOLst the finderFileDTOLst to set
	 */
	public void setFinderFileDTOLst(List<FinderFileDTO> finderFileDTOLst) {
		this.finderFileDTOLst = finderFileDTOLst;
	}

}
