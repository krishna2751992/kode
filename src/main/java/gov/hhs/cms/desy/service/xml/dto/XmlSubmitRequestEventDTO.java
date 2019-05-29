/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlSubmitRequestEventDTO {

	private String pn;
	private int superID;
	//private int requestID;
	private String superRequest;
	private int duaNumber;
	private String expirationDate;
	private String studyName;
	private String returnRequired;
	private String userNum;
	private String userId;
	private String userName;;
	private String passwordUpper;
	private String requestDesc;
	private String prevRequestID;
	private String dataTypeID;
	private String dataTypeName;
	private String dataSourceID;
	private String dataSourceName;
	private String stateCode;
	private String stateDescription;
	private String formatID;
	private int mediaTypeID;
	private String mediaTypeDescription;
	private String outputFileIdentifier;
	private String dataDescriptionCode;
	private String userEmail;
	private String commaDelimited;
	private String viewIdInt;
	private String outputTypeDescription;
	private String dropRecordRequired;
	private String customViewID;
	private String recipID;
	private String recipName;
	private String recipEmail;
	private String befPufView;
	private String savedRequest;
	private String pagesVisited;	
	private XmlSearchListDTO xMLSearchList;
	private XmlOutputColsLstDTO xmlOutputColsLst;
	private XmlRequestListDTO xmlRequestListDTO;
	private XmlFinderFileLstDTO xmlFinderFileLstDTO;
	
	/**
	 * @return the pn
	 */
	@XmlElement(name = "PN")
	public String getPn() {
		return pn;
	}

	/**
	 * @param pn the pn to set
	 */
	public void setPn(String pn) {
		this.pn = pn;
	}
	
	
	/**
	 * @return the xMLSearchList
	 */
	@XmlElement(name = "SEARCH_LIST")
	public XmlSearchListDTO getxMLSearchList() {
		return xMLSearchList;
	}

	/**
	 * @param xMLSearchList the xMLSearchList to set
	 */
	public void setxMLSearchList(XmlSearchListDTO xMLSearchList) {
		this.xMLSearchList = xMLSearchList;
	}

	
	/**
	 * @return the xmlOutputColsLst
	 */
	@XmlElement(name = "OUTPUT_COL_LIST")
	public XmlOutputColsLstDTO getXmlOutputColsLst() {
		return xmlOutputColsLst;
	}

	/**
	 * @param xmlOutputColsLst the xmlOutputColsLst to set
	 */
	public void setXmlOutputColsLst(XmlOutputColsLstDTO xmlOutputColsLst) {
		this.xmlOutputColsLst = xmlOutputColsLst;
	}

	
	/**
	 * @return the xmlRequestListDTO
	 */
	@XmlElement(name = "REQUEST_LIST")
	public XmlRequestListDTO getXmlRequestListDTO() {
		return xmlRequestListDTO;
	}

	/**
	 * @param xmlRequestListDTO the xmlRequestListDTO to set
	 */
	public void setXmlRequestListDTO(XmlRequestListDTO xmlRequestListDTO) {
		this.xmlRequestListDTO = xmlRequestListDTO;
	}

	/**
	 * @return the xmlFinderFileLstDTO
	 */
	@XmlElement(name = "INPUT_FILE_LIST")
	public XmlFinderFileLstDTO getXmlFinderFileLstDTO() {
		return xmlFinderFileLstDTO;
	}

	/**
	 * @param xmlFinderFileLstDTO the xmlFinderFileLstDTO to set
	 */
	public void setXmlFinderFileLstDTO(XmlFinderFileLstDTO xmlFinderFileLstDTO) {
		this.xmlFinderFileLstDTO = xmlFinderFileLstDTO;
	}

	/**
	 * @return the superID
	 */
	@XmlElement(name = "SUPER_ID")
	public int getSuperID() {
		return superID;
	}

	/**
	 * @param superID the superID to set
	 */
	public void setSuperID(int superID) {
		this.superID = superID;
	}

	/**
	 * @return the superRequest
	 */
	@XmlElement(name = "IS_SUPER_REQ")
	public String getSuperRequest() {
		return superRequest;
	}

	/**
	 * @param superRequest the superRequest to set
	 */
	public void setSuperRequest(String superRequest) {
		this.superRequest = superRequest;
	}

	/**
	 * @return the duaNumber
	 */
	@XmlElement(name = "DUA_NB")
	public int getDuaNumber() {
		return duaNumber;
	}

	/**
	 * @param duaNumber the duaNumber to set
	 */
	public void setDuaNumber(int duaNumber) {
		this.duaNumber = duaNumber;
	}

	/**
	 * @return the expirationDate
	 */
	@XmlElement(name = "DESY_EXPRTN_DT")	
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the studyName
	 */
	@XmlElement(name = "STDY_NAME")
	public String getStudyName() {
		return studyName;
	}

	/**
	 * @param studyName the studyName to set
	 */
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	/**
	 * @return the returnRequired
	 */
	@XmlElement(name = "RETURN_REQ")
	public String getReturnRequired() {
		return returnRequired;
	}

	/**
	 * @param returnRequired the returnRequired to set
	 */
	public void setReturnRequired(String returnRequired) {
		this.returnRequired = returnRequired;
	}

	/**
	 * @return the userNum
	 */
	@XmlElement(name = "USER_NB")
	public String getUserNum() {
		return userNum;
	}

	/**
	 * @param userNum the userNum to set
	 */
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	/**
	 * @return the userId
	 */
	@XmlElement(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	@XmlElement(name = "USER_ID")
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the passwordUpper
	 */
	@XmlElement(name = "USER_PASS")
	public String getPasswordUpper() {
		return passwordUpper;
	}

	/**
	 * @param passwordUpper the passwordUpper to set
	 */
	public void setPasswordUpper(String passwordUpper) {
		this.passwordUpper = passwordUpper;
	}

	/**
	 * @return the requestDesc
	 */
	@XmlElement(name = "RQST_NM")
	public String getRequestDesc() {
		return requestDesc;
	}

	/**
	 * @param requestDesc the requestDesc to set
	 */
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}

	/**
	 * @return the prevRequestID
	 */
	@XmlElement(name = "RQST_ACTY_IND")
	public String getPrevRequestID() {
		return prevRequestID;
	}

	/**
	 * @param prevRequestID the prevRequestID to set
	 */
	public void setPrevRequestID(String prevRequestID) {
		this.prevRequestID = prevRequestID;
	}

	/**
	 * @return the dataTypeID
	 */
	@XmlElement(name = "OBJ_ID")
	public String getDataTypeID() {
		return dataTypeID;
	}

	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(String dataTypeID) {
		this.dataTypeID = dataTypeID;
	}

	/**
	 * @return the dataTypeName
	 */
	@XmlElement(name = "OBJ_ID")
	public String getDataTypeName() {
		return dataTypeName;
	}

	/**
	 * @param dataTypeName the dataTypeName to set
	 */
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	/**
	 * @return the dataSourceID
	 */
	@XmlElement(name = "DATASTORE_ID")
	public String getDataSourceID() {
		return dataSourceID;
	}

	/**
	 * @param dataSourceID the dataSourceID to set
	 */
	public void setDataSourceID(String dataSourceID) {
		this.dataSourceID = dataSourceID;
	}

	/**
	 * @return the dataSourceName
	 */
	@XmlElement(name = "DATASTORE_NM")
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * @param dataSourceName the dataSourceName to set
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	/**
	 * @return the stateCode
	 */
	@XmlElement(name = "STATE_CD")
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the stateDescription
	 */
	@XmlElement(name = "STATE_NAME")
	public String getStateDescription() {
		return stateDescription;
	}

	/**
	 * @param stateDescription the stateDescription to set
	 */
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	/**
	 * @return the formatID
	 */
	@XmlElement(name = "STATE_NAME")
	public String getFormatID() {
		return formatID;
	}

	/**
	 * @param formatID the formatID to set
	 */
	public void setFormatID(String formatID) {
		this.formatID = formatID;
	}

	/**
	 * @return the mediaTypeID
	 */
	@XmlElement(name = "MDA_ID")
	public int getMediaTypeID() {
		return mediaTypeID;
	}

	/**
	 * @param mediaTypeID the mediaTypeID to set
	 */
	public void setMediaTypeID(int mediaTypeID) {
		this.mediaTypeID = mediaTypeID;
	}

	/**
	 * @return the mediaTypeDescription
	 */
	@XmlElement(name = "MDA_DCRN")
	public String getMediaTypeDescription() {
		return mediaTypeDescription;
	}

	/**
	 * @param mediaTypeDescription the mediaTypeDescription to set
	 */
	public void setMediaTypeDescription(String mediaTypeDescription) {
		this.mediaTypeDescription = mediaTypeDescription;
	}

	/**
	 * @return the outputFileIdentifier
	 */
	@XmlElement(name = "DATASET_NM")
	public String getOutputFileIdentifier() {
		return outputFileIdentifier;
	}

	/**
	 * @param outputFileIdentifier the outputFileIdentifier to set
	 */
	public void setOutputFileIdentifier(String outputFileIdentifier) {
		this.outputFileIdentifier = outputFileIdentifier;
	}

	/**
	 * @return the dataDescriptionCode
	 */
	@XmlElement(name = "RQST_OUT_LBL")
	public String getDataDescriptionCode() {
		return dataDescriptionCode;
	}

	/**
	 * @param dataDescriptionCode the dataDescriptionCode to set
	 */
	public void setDataDescriptionCode(String dataDescriptionCode) {
		this.dataDescriptionCode = dataDescriptionCode;
	}

	/**
	 * @return the userEmail
	 */
	@XmlElement(name = "EMAL_ADDRS")
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the commaDelimited
	 */
	@XmlElement(name = "COMMA_DELIM_FMT")
	public String getCommaDelimited() {
		return commaDelimited;
	}

	/**
	 * @param commaDelimited the commaDelimited to set
	 */
	public void setCommaDelimited(String commaDelimited) {
		this.commaDelimited = commaDelimited;
	}

	/**
	 * @return the viewIdInt
	 */
	@XmlElement(name = "VW_ID")
	public String getViewIdInt() {
		return viewIdInt;
	}

	/**
	 * @param viewIdInt the viewIdInt to set
	 */
	public void setViewIdInt(String viewIdInt) {
		this.viewIdInt = viewIdInt;
	}

	/**
	 * @return the outputTypeDescription
	 */
	@XmlElement(name = "RQST_OTPT_DESC")
	public String getOutputTypeDescription() {
		return outputTypeDescription;
	}

	/**
	 * @param outputTypeDescription the outputTypeDescription to set
	 */
	public void setOutputTypeDescription(String outputTypeDescription) {
		this.outputTypeDescription = outputTypeDescription;
	}

	/**
	 * @return the dropRecordRequired
	 */
	@XmlElement(name = "DROP_REC_CD")
	public String getDropRecordRequired() {
		return dropRecordRequired;
	}

	/**
	 * @param dropRecordRequired the dropRecordRequired to set
	 */
	public void setDropRecordRequired(String dropRecordRequired) {
		this.dropRecordRequired = dropRecordRequired;
	}

	/**
	 * @return the customViewID
	 */
	@XmlElement(name = "RQST_CSTM_VW_ID")
	public String getCustomViewID() {
		return customViewID;
	}

	/**
	 * @param customViewID the customViewID to set
	 */
	public void setCustomViewID(String customViewID) {
		this.customViewID = customViewID;
	}

	/**
	 * @return the recipID
	 */
	@XmlElement(name = "RECIP_ID")
	public String getRecipID() {
		return recipID;
	}

	/**
	 * @param recipID the recipID to set
	 */
	public void setRecipID(String recipID) {
		this.recipID = recipID;
	}

	/**
	 * @return the recipName
	 */
	@XmlElement(name = "RECIP_NM")
	public String getRecipName() {
		return recipName;
	}

	/**
	 * @param recipName the recipName to set
	 */
	public void setRecipName(String recipName) {
		this.recipName = recipName;
	}

	/**
	 * @return the recipEmail
	 */
	@XmlElement(name = "ADR_EMAIL_NAME")
	public String getRecipEmail() {
		return recipEmail;
	}

	/**
	 * @param recipEmail the recipEmail to set
	 */
	public void setRecipEmail(String recipEmail) {
		this.recipEmail = recipEmail;
	}

	/**
	 * @return the befPufView
	 */
	@XmlElement(name = "IS_BEF_PUF_VW")
	public String getBefPufView() {
		return befPufView;
	}

	/**
	 * @param befPufView the befPufView to set
	 */
	public void setBefPufView(String befPufView) {
		this.befPufView = befPufView;
	}

	/**
	 * @return the savedRequest
	 */
	@XmlElement(name = "IS_SAVED_RQST")
	public String getSavedRequest() {
		return savedRequest;
	}

	/**
	 * @param savedRequest the savedRequest to set
	 */
	public void setSavedRequest(String savedRequest) {
		this.savedRequest = savedRequest;
	}

	/**
	 * @return the pagesVisited
	 */
	@XmlElement(name = "PAGES_VISITED")
	public String getPagesVisited() {
		return pagesVisited;
	}

	/**
	 * @param pagesVisited the pagesVisited to set
	 */
	public void setPagesVisited(String pagesVisited) {
		this.pagesVisited = pagesVisited;
	}
}
