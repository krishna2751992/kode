/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.RequestService;
import gov.hhs.cms.desy.service.dto.ApprovalDTO;
import gov.hhs.cms.desy.service.dto.ColumnDTO;
import gov.hhs.cms.desy.service.dto.DataSourceDTO;
import gov.hhs.cms.desy.service.dto.DataTypeDTO;
import gov.hhs.cms.desy.service.dto.DuaDTO;
import gov.hhs.cms.desy.service.dto.FilterDTO;
import gov.hhs.cms.desy.service.dto.FinderFileDTO;
import gov.hhs.cms.desy.service.dto.FormatDTO;
import gov.hhs.cms.desy.service.dto.MediaTypeDTO;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.RequestSearchResultsDTO;
import gov.hhs.cms.desy.service.dto.SearchDTO;
import gov.hhs.cms.desy.service.dto.StatesDTO;
import gov.hhs.cms.desy.service.dto.SubRequestDTO;
import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.ReqDBTableParams;
import gov.hhs.cms.desy.xml.req.XMLIllegalCharConverter;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XMLReqParm;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class RequestServiceImpl implements RequestService {
	private final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves the request(saved or submitted request) information based on superId or requestID.
	 * if front end passes superID and requestID then transaction returns data for that request only
	 * if front end Passes SuperID and RequestID as -1 transaction will return all the sub-requests under that super
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.Request#getRequest(int, int, java.lang.String)
	 */
	public RequestDTO getRequest(int superId, int requestId, String userId) {
		log.info("RequestServiceImpl :: getRequest");
		
		RequestDTO requestDTO = new RequestDTO();

		String reqXML = requestXml(superId, requestId, userId);
		
		log.info("Request XML for get Request :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for get Reqeust  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);		

		requestDTO = createRequest(xmlDoc);

		return requestDTO;
	}

	/**
	 * @param superID
	 * @param requestID
	 * @param userId
	 * @return
	 */
	private String requestXml(int superID, int requestID, String userId) {

		userId = userId.toUpperCase();		
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// Calling CICS DSYCP016
		XMLQuery query = new XMLQuery("DSYCP016");

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP016_SUPER_ID, true, Types.INTEGER,
				new Integer(superID).toString()));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP016_REQUEST_ID, true, Types.INTEGER,
				requestID > 0 ? new Integer(requestID).toString() : "-1"));

		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * Converts XML document object for XML message response to an iterator object
	 * for request
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	private RequestDTO createRequest(Document doc) {
		RequestDTO requestDTO = new RequestDTO();
		Iterator dataRespItr = null;
		// retrives the xml message with in <DATA_RESP> tag
		if (doc.getRootElement().getChildren(XmlMsgConst.DATA_RESP) != null) {
			dataRespItr = doc.getRootElement().getChildren(XmlMsgConst.DATA_RESP).iterator();

			requestDTO = createRequest(dataRespItr);
		}

		return requestDTO;
	}

	/**
	 * Converts Iterator Object into RequestDTO object.
	 * 
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private RequestDTO createRequest(Iterator iter) {
		RequestDTO request = null;
		List<FilterDTO> filters = new ArrayList<FilterDTO>();
		List<FinderFileDTO> finderFiles = new ArrayList<FinderFileDTO>();
		SearchDTO search = new SearchDTO();
		List<ColumnDTO> outputColumns = new ArrayList<ColumnDTO>();
		List<SubRequestDTO> subRequests = new ArrayList<SubRequestDTO>();

		Element ele = null;
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			// adding Header information to Request Object
			request = createBaseRequest(ele);
			// adding Search Criteria object to filters object
			filters = createFilters(ele);
			// adding Finder File information to FinderFile objects
			finderFiles = createFinderFiles(ele);
			search = new SearchDTO();
			search.setFilters(filters);
			search.setFinderFiles(finderFiles);
			// Setting search Criteria Object containing filters and finder file information
			request.setSearchCriteria(search);
			// adding output columns to Output column object array.
			outputColumns = createOutputColumns(ele);
			request.setOutputColumns(outputColumns);
			// craeting sub requests based on Years and other information from xml message.
			subRequests = createSubRequests(ele);
			request.setSubRequests(subRequests);
			// If there are more than one sub requests then storing the Request Status as
			// "SUPER"
			// otherwise retrieve the status from sub requests and store with request
			// object.
			if (subRequests.size() > 1) {
				request.setStatus("SUPER");
			} else if (subRequests.size() == 1) {
				request.setStatus(subRequests.get(0).getRequestStatus());
			}
			// setting the Output description as "Select Available Fields if Custom View ID
			// with
			// the request is 0 and View ID from the back end is V-1(output type -1 is being
			// stored if its Bef Puf View, custom view or user has selected select Available
			// Fields option while making the request)
			if (request.getCustomViewID() == 0 && request.getOutputType() != null
					&& request.getOutputType().getViewID() != null
					&& request.getOutputType().getViewID().equalsIgnoreCase("V-1") && outputColumns.size() > 0) {
				OutputTypeDTO outType = request.getOutputType();
				outType.setViewID("2");
				outType.setDescription("SELECT AVAILABLE FIELDS");
				request.setOutputType(outType);
			}
		}
		return request;
	}

	/**
	 * Converts information from XML to Request object.
	 * 
	 * @param ele
	 * @return
	 * @throws Exception
	 */
	private RequestDTO createBaseRequest(Element ele) {

		RequestDTO request = new RequestDTO();
		// Adding Super ID to request Object
		request.setSuperID(Integer.parseInt(ele.getChildText("SUPER-ID").trim()));

		// **************** Dua Information for the request *****************//
		DuaDTO dua = new DuaDTO();

		dua.setDuaNumber(Integer.parseInt(ele.getChildText("DUA-NB").trim()));
		// As unsubmitted request does not return this tag -- check for null
		// adding dua expiration Date(database returns expiration date in MM/DD/YYYY
		// format) to dua object.
		if (ele.getChildText("EXPRTN-DT") != null && ele.getChildText("EXPRTN-DT").trim().length() > 0)
			dua.setExpirationDate(DateFunctions.makeDate(ele.getChildText("EXPRTN-DT").trim()));

		// As unsubmitted request does not return this tag -- check for null
		// adding Desy expiration Date(database returns expiration date in MM/DD/YYYY
		// format) to dua object.
		if (ele.getChildText("DESY-EXPRTN-DT") != null && ele.getChildText("DESY-EXPRTN-DT").trim().length() > 0)
			dua.setDesyExpirationDate(DateFunctions.makeDate(ele.getChildText("DESY-EXPRTN-DT").trim()));

		// As unsubmitted request does not return this tag -- check for null
		// adding Dua Requester to dua Object
		if (ele.getChildText("DUA-REQUESTER") != null && ele.getChildText("DUA-REQUESTER").trim().length() > 0)
			dua.setRequestor(ele.getChildText("DUA-REQUESTER").trim());

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("RQST-ENCRPTN-CD") != null && ele.getChildText("RQST-ENCRPTN-CD").trim().length() > 0)
			dua.setEncryptionSwitch(ele.getChildText("RQST-ENCRPTN-CD").trim().charAt(0));

		// As unsubmitted request does not return this tag -- check for null
		// Setting Desy Closed boolean in dua object.
		if (ele.getChildText("DUA-STUS-CD") != null && ele.getChildText("DUA-STUS-CD").trim().length() > 0) {
			char c = ele.getChildText("DUA-STUS-CD").trim().charAt(0);

			if (c == 'C') {
				dua.setDuaClosed(true);
			} else {
				dua.setDuaClosed(false);
			} // end-if
		}

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("RETURN-REQ") != null && ele.getChildText("RETURN-REQ").trim().length() > 0) {
			char c = ele.getChildText("RETURN-REQ").trim().charAt(0);
			if (c == 'Y' || c == 'y') {
				dua.setReturnRequired(true);
				dua.setFtapeSwitch('Y');
			} else {
				dua.setReturnRequired(false);
				dua.setFtapeSwitch('N');
			}
		}

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("STDY-NAME") != null && ele.getChildText("STDY-NAME").trim().length() > 0)
			dua.setStudyName(ele.getChildText("STDY-NAME").trim());
		request.setDua(dua);
		// *************************************************************//

		// ************** User Information for the request ************//
		UserDTO user = new UserDTO();
		user.setUserId(ele.getChildText("USER-ID").trim());

		user.setUserNum(Integer.parseInt(ele.getChildText("USER-NB").trim()));

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("USER-NAME") != null && ele.getChildText("USER-NAME").trim().length() > 0)
			user.setUserName(ele.getChildText("USER-NAME").trim());

		// Nullable
		// As unsubmitted request may return this tag as null -- check for null
		if (ele.getChildText("EMAL-ADDRS") != null && ele.getChildText("EMAL-ADDRS").trim().length() > 0)
			user.setEmail(DBStringConverter.checkDBCharNull(ele.getChildText("EMAL-ADDRS").trim()));

		request.setUser(user);
		// *************************************************************//

		// ************ Flag whether Saved Request or not ****************//
		boolean isSavedRequest = false;
		if (ele.getChildText("IS-SAVED-RQST") != null && ele.getChildText("IS-SAVED-RQST").trim().length() > 0) {
			isSavedRequest = ele.getChildText("IS-SAVED-RQST").trim().toUpperCase().charAt(0) == 'Y' ? true : false;
			request.setSavedRequest(isSavedRequest);
		}
		// **************************************************************//

		// ****************************Request Name *********************//
		// Nullable
		String RqstName = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim());
		request.setRequestDesc(XMLIllegalCharConverter
				.replaceIllegalCharactersRev(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim())));

		// **************************************************************//

		// ******************* Previous request ID if Copying an existing request
		// ************//
		// Nullable
		String previousRequestID = DBStringConverter.checkDBIntegerNull(ele.getChildText("RQST-ACTY-IND"));
		if (previousRequestID != null && previousRequestID.length() > 0) {
			request.setPrevRequestID(Integer.parseInt(previousRequestID));
		}

		// **********************************************************************************//

		// ****************************** Data Type Information
		// **************************//

		DataTypeDTO dataType = new DataTypeDTO();
		dataType.setDataTypeID(Integer.parseInt(ele.getChildText("OBJ-ID")));

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("OBJ-NM") != null && ele.getChildText("OBJ-NM").trim().length() > 0)
			dataType.setName(ele.getChildText("OBJ-NM"));

		request.setDataType(dataType);

		// *******************************************************************************//

		// ************************** Data Source Information
		// ****************************//

		DataSourceDTO dataSource = new DataSourceDTO();
		dataSource.setDataSourceId(Integer.parseInt(ele.getChildText("DATASTORE-ID")));

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("DATASTORE-NM") != null && ele.getChildText("DATASTORE-NM").trim().length() > 0)
			dataSource.setName(ele.getChildText("DATASTORE-NM"));
		request.setDataSource(dataSource);

		// ******************************************************************************//

		// ********************** State Infromation
		// *************************************//
		StatesDTO state = new StatesDTO();
		// Nullable
		state.setStateCode(DBStringConverter.checkDBCharNull(ele.getChildText("STATE-CD").trim()));

		// Nullable
		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("STATE-NAME") != null && ele.getChildText("STATE-NAME").trim().length() > 0)
			state.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("STATE-NAME").trim()));
		request.setState(state);

		// ******************************************************************************//

		// *************************** Format infromation
		// *******************************//
		FormatDTO format = new FormatDTO();
		// Nullable
		String formatID = DBStringConverter.checkDBIntegerNull(ele.getChildText("FRMT-ID"));
		if (formatID != null && formatID.length() > 0) {
			format.setFormatId(Integer.parseInt(formatID));
		}

		// Nullable

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("FRMT-DCRN") != null && ele.getChildText("FRMT-DCRN").trim().length() > 0)
			format.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("FRMT-DCRN")));

		request.setFormat(format);

		// ****************************************************************************//

		// ************************* Media type infromation
		// ***************************//

		MediaTypeDTO mediaType = new MediaTypeDTO();
		// Nullable
		String mediaID = DBStringConverter.checkDBIntegerNull(ele.getChildText("MDA-ID"));
		if (mediaID != null && mediaID.length() > 0) {
			mediaType.setMediaTypeID(Integer.parseInt(mediaID));
		}

		// Nullable
		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("MDA-DCRN") != null && ele.getChildText("MDA-DCRN").trim().length() > 0)
			mediaType.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("MDA-DCRN")));
		request.setMediaType(mediaType);

		// ****************************************************************************//

		// *********************** Output File identifier
		// *****************************//
		// Nullable
		String outputFileIndn = DBStringConverter.checkDBCharNull(ele.getChildText("DATASET-NM").trim());

		outputFileIndn = formatIdentifier(outputFileIndn);

		request.setOutputFileIdentifier(outputFileIndn);

		// ***************************************************************************//

		// ************************* Data Description Code ***************************//
		// Nullable
		request.setDataDescriptionCode(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OUT-LBL").trim()));

		// ***************************************************************************//

		// ********************************Comma Deliminated Format ******************//

		// As unsubmitted request may have null value -- check for null
		if (ele.getChildText("COMMA-DELIM-FMT") != null && ele.getChildText("COMMA-DELIM-FMT").trim().length() > 0)
			request.setCommaDelimited(
					ele.getChildText("COMMA-DELIM-FMT").toUpperCase().charAt(0) == 'Y' ? true : false);
		// ***************************************************************************//

		// ****************************Output type ***********************************//

		OutputTypeDTO outputType = new OutputTypeDTO();

		// As unsubmitted request may have null value -- check for null
		if (ele.getChildText("RQST-CSTM-VW-ID") != null && ele.getChildText("RQST-CSTM-VW-ID").trim().length() > 0) {
			// retrieving and setting custom view ID with the request
			int customViewID = Integer.parseInt(ele.getChildText("RQST-CSTM-VW-ID"));
			request.setCustomViewID(customViewID);
			// retrieving is befPuf View(back end is returning after checking if User ID
			// associated with that view is null that means its bef puf view)
			String isBefPufview = null;
			if (ele.getChildText("IS-BEF-PUF-VW") != null && ele.getChildText("IS-BEF-PUF-VW").trim().length() > 0) {
				isBefPufview = ele.getChildText("IS-BEF-PUF-VW").trim();
			}

			if (customViewID > 0) {
				// if Custom View - BefPuf View - OutputType = C2
				// Not Bef Puf View - output Type= C-1
				if (isBefPufview != null && isBefPufview.equalsIgnoreCase("Y")) {
					outputType.setViewID("2", "C", 0);
				} else {
					outputType.setViewID("-1", "C", 1);
				}

				if (!isSavedRequest) {
					// if its a submitted Request
					if (ele.getChildText("RQST-VW-NAME") != null
							&& ele.getChildText("RQST-VW-NAME").trim().length() > 0) {
						outputType.setDescription(ele.getChildText("RQST-VW-NAME").trim());
					}

				} else {
					// if its a saved request
					if (ele.getChildText("RQST-OTPT-DESC") != null
							&& ele.getChildText("RQST-OTPT-DESC").trim().length() > 0) {
						outputType.setDescription(
								DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OTPT-DESC").trim()));
					}
				}
			} else {
				// If its not a custom view
				if (ele.getChildText("VW-ID") != null && ele.getChildText("VW-ID").trim().length() > 0)
					outputType.setViewID(ele.getChildText("VW-ID").trim());

				if (ele.getChildText("RQST-OTPT-DESC") != null
						&& ele.getChildText("RQST-OTPT-DESC").trim().length() > 0)
					outputType.setDescription(
							DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OTPT-DESC").trim()));

			}
		} else {
			if (ele.getChildText("VW-ID") != null && ele.getChildText("VW-ID").trim().length() > 0)
				outputType.setViewID(ele.getChildText("VW-ID").trim());

			if (ele.getChildText("RQST-OTPT-DESC") != null && ele.getChildText("RQST-OTPT-DESC").trim().length() > 0)
				outputType.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OTPT-DESC").trim()));

		}

		request.setOutputType(outputType);

		// *****************************************************************************//

		// ************************* Dropped Record Count
		// ******************************//

		// As unsubmitted request may have null value -- check for null
		if (ele.getChildText("DROP-REC-CD") != null && ele.getChildText("DROP-REC-CD").trim().length() > 0)
			request.setDropRecordRequired(ele.getChildText("DROP-REC-CD").toUpperCase());

		// *****************************************************************************//

		// ************************* Reciepient
		// ****************************************//

		RecipientDTO recip = new RecipientDTO();
		// Nullable
		String recipID = DBStringConverter.checkDBIntegerNull(ele.getChildText("RECIP-ID"));
		if (recipID != null && recipID.length() > 0) {
			recip.setRecipID(Integer.parseInt(recipID));
		}

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("RECIP-NM") != null && ele.getChildText("RECIP-NM").trim().length() > 0)
			recip.setName(DBStringConverter.checkDBCharNull(ele.getChildText("RECIP-NM").trim()));

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("ADR-EMAIL-NAME") != null && ele.getChildText("ADR-EMAIL-NAME").trim().length() > 0)
			recip.setEmail(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-EMAIL-NAME").trim()));

		request.setRecipient(recip);

		// ****************************** Modification Date
		// ***************************//

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("MOD-DT") != null && ele.getChildText("MOD-DT").trim().length() > 0) {
			Date modifiedDate = DateFunctions.parseDate(ele.getChildText("MOD-DT").trim());
			request.setDateModified(modifiedDate);
		}

		// ****************************************************************************//

		// *******************************Create Date
		// *********************************//

		// As unsubmitted request does not return this tag -- check for null
		if (ele.getChildText("CRT-DT") != null && ele.getChildText("CRT-DT").trim().length() > 0) {
			Date createDate = DateFunctions.parseDate(ele.getChildText("CRT-DT").trim());
			request.setDateCreated(createDate);
		}

		// ****************************************************************************//

		// ************************* Pages Visited for saved Request
		// ******************//

		// This tag will be there only for saved requests and is being used for Sub
		// Navigation bar
		if (ele.getChildText("PAGES-VISITED") != null && ele.getChildText("PAGES-VISITED").trim().length() > 0) {
			int pagesVisited = Integer.parseInt(ele.getChildText("PAGES-VISITED").trim());
			request.setPagesVisited(pagesVisited);
		}

		// ***************************************************************************//
		return request;
	}

	/**
	 * This Method parses XML message into List of filter Objects.
	 * 
	 * @param eleDataResp
	 * @return
	 */
	private List<FilterDTO> createFilters(Element eleDataResp) {
		// tags are being stored In XMLMsgConst class with (_) for CUD opertaions but
		// Query opertaions use (-)
		String tagSearchList = (XmlMsgConst.SEARCH_LIST_TAG).replace('_', '-');
		String tagSearchElement = (XmlMsgConst.SEARCH_ELEMENT_TAG).replace('_', '-');

		Iterator searchListItr = null;
		Element searchListEle = null;
		Iterator searchElementItr = null;
		if (eleDataResp.getChildren(tagSearchList) != null) {
			searchListItr = eleDataResp.getChildren(tagSearchList).iterator();
		}
		while (searchListItr.hasNext()) {
			searchListEle = (Element) searchListItr.next();
		}
		if (searchListEle.getChildren(tagSearchElement) != null) {
			searchElementItr = searchListEle.getChildren(tagSearchElement).iterator();
		}

		FilterDTO filter = null;
		Element ele = null;
		ArrayList filterList = new ArrayList();

		while (searchElementItr.hasNext()) {
			ele = (Element) searchElementItr.next();
			filter = new FilterDTO();
			// Element_ID
			filter.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID").trim()));

			// Element name
			// As unsubmitted request does not return this tag -- check for null
			if (ele.getChildText("ELE-NM") != null && ele.getChildText("ELE-NM").trim().length() > 0)
				filter.setColumnName(ele.getChildText("ELE-NM").trim());

			// Operator
			filter.setOperator(ele.getChildText("OPPR-LST").trim());

			// Value Entered
			String value = ele.getChildText("DATA-VALUE").trim();
			filter.setValue(value);

			// Search criteria set
			filter.setGroupID(Integer.parseInt(ele.getChildText("GROUP-ID").trim()));

			// Look up Text if val type is 1(look up)
			// Nullable
			// As unsubmitted request does not return this tag -- check for null
			if (ele.getChildText("DATA-CD-TXT") != null && ele.getChildText("DATA-CD-TXT").trim().length() > 0) {
				String lookupText = DBStringConverter.checkDBCharNull(ele.getChildText("DATA-CD-TXT").trim());
				if (lookupText != null && lookupText.length() > 0) {
					// Add LookupValue to the LookupText only if Value is not empty or "space".
//						if(value != null && value.length()>0  && !value.equalsIgnoreCase("space"))
					lookupText = value + " - " + lookupText;
					filter.setLookupText(lookupText);
				}
			}

			// Search for ICD IND Description
			String icdIndDesc = "";

			// This is for saved requests
			if (ele.getChildText("ELE-ICD-IND-CD") != null && ele.getChildText("ELE-ICD-IND-CD").trim().length() > 0) {
				System.out.println("**** element indicator code not null ****");
				System.out.println(ele.getChildText("ELE-ICD-IND-CD"));

				filter.setEleIcdIndCd(ele.getChildText("ELE-ICD-IND-CD").trim());
			}
			// This is for submitted requests
			else if (ele.getChildText("ICD-IND-DESC-ELE") != null) {
				System.out.println("**IN ICD-IND-DESC-ELE**");
				icdIndDesc = ele.getChildText("ICD-IND-DESC-ELE").trim();

				if (icdIndDesc.equals("ICD-9")) {
					icdIndDesc = "9";
					System.out.println("&&&&&icdIndDescEle**" + icdIndDesc + "**&&&&&");
					filter.setEleIcdIndCd(icdIndDesc);
				} else if (icdIndDesc.equals("ICD-10")) {
					icdIndDesc = "0";
					System.out.println("&&&&&icdIndDescEle**" + icdIndDesc + "**&&&&&");
					filter.setEleIcdIndCd(icdIndDesc);
				} else {
					icdIndDesc = "   ";
					System.out.println("&&&&&icdIndDescEle**" + icdIndDesc + "**&&&&&");
					filter.setEleIcdIndCd(icdIndDesc);
				}
			}

			// Sequence Id- this value will exist if val type is 1 (look up)
			// Nullable
			// As submitted request does not return this tag -- check for null
			String sequenceID = DBStringConverter.checkDBIntegerNull(ele.getChildText("ELE-NB"));
			if (sequenceID != null && sequenceID.length() > 0) {
				filter.setSquenceID(Integer.parseInt(sequenceID));
			}

			// WHr_CLs from DSY_ELEMENT table
			// Nullable
			if (ele.getChildText("WHR-CLS") != null && ele.getChildText("WHR-CLS").trim().length() > 0)
				filter.setWhereClause(DBStringConverter.checkDBCharNull(ele.getChildText("WHR-CLS")));

			filterList.add(filter);
		}

		return filterList;
	}

	/**
	 * This method parses XML message into List of Finder File Objects.
	 * 
	 * @param eleDataResp
	 * @return
	 */
	private List<FinderFileDTO> createFinderFiles(Element eleDataResp) {
		// tags are being stored In XMLMsgConst class with (_) for CUD opertaions but
		// Query opertaions use (-)
		String tagInputFileList = (XmlMsgConst.INPUT_FILE_LIST_TAG).replace('_', '-');
		String tagInputFile = (XmlMsgConst.INPUT_FILE_TAG).replace('_', '-');

		Iterator inputFileListItr = null;
		Element inputFileListEle = null;
		Iterator inputFileItr = null;
		if (eleDataResp.getChildren(tagInputFileList) != null) {
			inputFileListItr = eleDataResp.getChildren(tagInputFileList).iterator();
		}
		while (inputFileListItr.hasNext()) {
			inputFileListEle = (Element) inputFileListItr.next();
		}
		if (inputFileListEle.getChildren(tagInputFile) != null) {
			inputFileItr = inputFileListEle.getChildren(tagInputFile).iterator();
		}

		FinderFileDTO finderFile = null;
		Element ele = null;
		ArrayList finderFileList = new ArrayList();
		while (inputFileItr.hasNext()) {
			ele = (Element) inputFileItr.next();
			finderFile = new FinderFileDTO();

			// File name
			finderFile.setFileName(ele.getChildText("FIL-NM").trim());

			// Start Position
			// Nullable
			String startPos = DBStringConverter.checkDBIntegerNull(ele.getChildText("STTG-POS"));
			if (startPos != null && startPos.length() > 0) {
				finderFile.setStartPosition(Integer.parseInt(startPos));
			}

			// Finder Element ID
			// Nullable
			String finderEleID = DBStringConverter.checkDBIntegerNull(ele.getChildText("FINDER-ELE-NB"));
			if (finderEleID != null && finderEleID.length() > 0) {
				ColumnDTO col = new ColumnDTO();
				col.setColumnID(Integer.parseInt(finderEleID));
				finderFile.setFinderColumn(col);
			}

			// Header Start Position
			// Nullable
			String headerStartPos = DBStringConverter.checkDBIntegerNull(ele.getChildText("HDR-START-POS"));
			if (headerStartPos != null && headerStartPos.length() > 0) {
				finderFile.setHeaderStartPosition(Integer.parseInt(headerStartPos));
			}

			// Search Criteria Set
			// Nullable
			String groupID = DBStringConverter.checkDBIntegerNull(ele.getChildText("GRP-ID"));
			if (groupID != null && groupID.length() > 0) {
				finderFile.setGroupID(Integer.parseInt(groupID));
			}

			// Search for ICD IND Description
			String icdIndDescFil = "";

			// This is for saved requests
			if (ele.getChildText("FIL-ICD-IND-CD") != null && ele.getChildText("FIL-ICD-IND-CD").trim().length() > 0) {
				System.out.println("**** file indicator code not null ****");
				System.out.println(ele.getChildText("FIL-ICD-IND-CD"));

				finderFile.setFilIcdIndCd(ele.getChildText("FIL-ICD-IND-CD").trim());
			}
			// This is for submitted requests
			else if (ele.getChildText("ICD-IND-DESC-FIL") != null) {
				icdIndDescFil = ele.getChildText("ICD-IND-DESC-FIL").trim();

				if (icdIndDescFil.equals("ICD-9")) {
					icdIndDescFil = "9";
					System.out.println("&&&&&icdIndDescFil**" + icdIndDescFil + "**&&&&&");
					finderFile.setFilIcdIndCd(icdIndDescFil);
				} else if (icdIndDescFil.equals("ICD-10")) {
					icdIndDescFil = "0";
					System.out.println("&&&&&icdIndDescFil**" + icdIndDescFil + "**&&&&&");
					finderFile.setFilIcdIndCd(icdIndDescFil);
				} else {
					icdIndDescFil = "   ";
					System.out.println("&&&&&icdIndDescFil**" + icdIndDescFil + "**&&&&&");
					finderFile.setFilIcdIndCd(icdIndDescFil);
				}
			}

			finderFileList.add(finderFile);
		}

		return finderFileList;

	}

	/**
	 * This Method parses XML Message into List of Column objects
	 * 
	 * @param eleDataResp
	 * @return
	 */
	private List<ColumnDTO> createOutputColumns(Element eleDataResp) {
		// tags are being stored In XMLMsgConst class with (_) for CUD opertaions but
		// Query opertaions use (-)
		String tagOutputColList = (XmlMsgConst.OUTPUT_COL_LIST_TAG).replace('_', '-');
		String tagColumn = (XmlMsgConst.ELM_TAG).replace('_', '-');

		Iterator outputColListItr = null;
		Element outputColListEle = null;
		Iterator columnItr = null;
		if (eleDataResp.getChildren(tagOutputColList) != null) {
			outputColListItr = eleDataResp.getChildren(tagOutputColList).iterator();
		}
		while (outputColListItr.hasNext()) {
			outputColListEle = (Element) outputColListItr.next();
		}
		if (outputColListEle.getChildren(tagColumn) != null) {
			columnItr = outputColListEle.getChildren(tagColumn).iterator();
		}

		ColumnDTO col = null;
		Element ele = null;
		LinkedHashMap<String, ColumnDTO> columnHash = new LinkedHashMap<String, ColumnDTO>();

		while (columnItr.hasNext()) {
			ele = (Element) columnItr.next();
			col = (ColumnDTO) columnHash.get(ele.getChildText("ELM-ID"));
			if (col == null) {
				col = new ColumnDTO();
				// Element ID
				col.setColumnID(Integer.parseInt(ele.getChildText("ELM-ID").trim()));

				// Element Name
				// As unsubmitted request does not return this tag -- check for null
				if (ele.getChildText("ELM-NM") != null && ele.getChildText("ELM-NM").trim().length() > 0)
					col.setName(ele.getChildText("ELM-NM").trim());

				columnHash.put(ele.getChildText("ELM-ID"), col);
			}

		}

		return columnHash.values().stream().collect(Collectors.toList());
	}

	/**
	 * This Method parses XML Message into List of subRequest objects
	 * 
	 * @param eleDataResp
	 * @return
	 */
	private List<SubRequestDTO> createSubRequests(Element eleDataResp) {
		// tags are being stored In XMLMsgConst class with (_) for CUD opertaions but
		// Query opertaions use (-)
		String tagRequestList = (XmlMsgConst.REQUEST_LIST_TAG).replace('_', '-');
		String tagRequest = (XmlMsgConst.REQUEST_TAG).replace('_', '-');

		Iterator requestListItr = null;
		Element requestListEle = null;
		Iterator requestItr = null;
		if (eleDataResp.getChildren(tagRequestList) != null) {
			requestListItr = eleDataResp.getChildren(tagRequestList).iterator();
		}
		while (requestListItr.hasNext()) {
			requestListEle = (Element) requestListItr.next();
		}
		if (requestListEle.getChildren(tagRequest) != null) {
			requestItr = requestListEle.getChildren(tagRequest).iterator();
		}

		SubRequestDTO subRequest = null;
		Element ele = null;
		LinkedHashMap<String, SubRequestDTO> subRequestHash = new LinkedHashMap<String, SubRequestDTO>();

		while (requestItr.hasNext()) {
			ele = (Element) requestItr.next();
			subRequest = (SubRequestDTO) subRequestHash.get(ele.getChildText("DATA-YEAR"));
			if (subRequest == null) {
				subRequest = new SubRequestDTO();

				// Request ID associated with each request
				if (ele.getChildText("DATA-RQST-ID") != null && ele.getChildText("DATA-RQST-ID").trim().length() > 0)
					subRequest.setRequestID(Integer.parseInt(ele.getChildText("DATA-RQST-ID").trim()));

				// Year for each Request
				if (ele.getChildText("DATA-YEAR") != null && ele.getChildText("DATA-YEAR").trim().length() > 0) {
					int year = Integer.parseInt(ele.getChildText("DATA-YEAR").trim());
					if (year > 0)
						subRequest.setDataYear(year);
				}

				// Update Month/Year
				if (ele.getChildText("RQST-MO-YR") != null && ele.getChildText("RQST-MO-YR").trim().length() > 0)
					subRequest.setUpdateMonthYear(
							DBStringConverter.checkDBCharNull(ele.getChildText("RQST-MO-YR").trim()));

				// Output File name
				if (ele.getChildText("RQST-OTPT-DS-NAME") != null
						&& ele.getChildText("RQST-OTPT-DS-NAME").trim().length() > 0)
					subRequest.setOutputFileName(ele.getChildText("RQST-OTPT-DS-NAME").trim());

				// output copybook Name
				if (ele.getChildText("RQST-CPYBK-NAME") != null
						&& ele.getChildText("RQST-CPYBK-NAME").trim().length() > 0)
					subRequest.setOutputCopybook(ele.getChildText("RQST-CPYBK-NAME").trim());

				// Dropped File name
				if (ele.getChildText("RQST-DROP-DS-NAME") != null
						&& ele.getChildText("RQST-DROP-DS-NAME").trim().length() > 0)
					subRequest.setDroppedFileName(ele.getChildText("RQST-DROP-DS-NAME").trim());

				// Record Count
				// Nullable
				if (ele.getChildText("LOGIC-REC-COUNT") != null
						&& ele.getChildText("LOGIC-REC-COUNT").trim().length() > 0) {
					String recordCount = DBStringConverter.checkDBIntegerNull(ele.getChildText("LOGIC-REC-COUNT"));
					if (recordCount != null) {
						subRequest.setRecordCount(recordCount);
					}
				}

				// Dropped record count
				// Nullable
				if (ele.getChildText("LOGIC-DROP-COUNT") != null
						&& ele.getChildText("LOGIC-DROP-COUNT").trim().length() > 0) {
					String dropRecordCount = DBStringConverter.checkDBIntegerNull(ele.getChildText("LOGIC-DROP-COUNT"));
					if (dropRecordCount != null) {
						subRequest.setDroppedRecordCount(dropRecordCount);
					}
				}

				// Request Status
				if (ele.getChildText("CPLN-STUS") != null && ele.getChildText("CPLN-STUS").trim().length() > 0)
					subRequest.setRequestStatus(ele.getChildText("CPLN-STUS"));

				subRequestHash.put(ele.getChildText("DATA-YEAR"), subRequest);

			}

		}

		return subRequestHash.values().stream().collect(Collectors.toList());

	}

	/**
	 * This method parses XML message into array of RequestDTO used for request
	 * search
	 * 
	 * @param iter
	 * @param reqResult
	 * @return
	 */
	private Collection createRequestHeaders(Iterator iter, RequestSearchResultsDTO reqResult) {
		Element ele = null;
		List<RequestDTO> request = null;
		ArrayList requestHeaderList = null;
		LinkedHashMap<String, RequestDTO> requestHash = new LinkedHashMap<String, RequestDTO>();
		int superID = 0;
		int superIDCount = 0;
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			// create a array of request associated with single element in xml - Creates
			// RequestHeader Object for each subrequest
			request = createRequestHeader(ele);
			superIDCount++;
			for (int i = 0; i < request.size(); i++) {
				requestHash.put(Integer.toString(request.get(i).getRequestID()), request.get(i));
			}
		}
		// setting number of superIds as retrived from back end
		reqResult.setSuperCount(superIDCount);
		return requestHash.values();

	}

	/**
	 * This Method parses xml message into List of RequestDTO Object based on number
	 * of Sub requests associated with that request
	 * 
	 * @param ele
	 * @return
	 */
	private List<RequestDTO> createRequestHeader(Element ele) {
		int superID = 0;
		String userID = null;
		String userName = null;
		int duaNumber = 0;
		DuaDTO dua = null;
		String requestDesc = null;
		String recipientName = null;
		RecipientDTO recip = null;
		Date createDate = null;
		Date desyExpirationdate = null;
		String dataDescription = null;
		// tags are being stored In XMLMsgConst class with (_) for CUD opertaions but
		// Query opertaions use (-)
		String tagRequestList = (XmlMsgConst.REQUEST_LIST_TAG).replace('_', '-');
		String tagRequest = (XmlMsgConst.REQUEST_TAG).replace('_', '-');
		boolean superFlag = false;
		UserDTO user = new UserDTO();
		recip = new RecipientDTO();
		dua = new DuaDTO();
		List<RequestDTO> requestList = new ArrayList<RequestDTO>();

		// Super ID
		superID = Integer.parseInt(ele.getChildText("SUPER-ID").trim());

		// ********************* User Information ******************************//
		userID = (ele.getChildText("USER-ID"));
		userName = ele.getChildText("CMS-PRSN-NAME");
		user.setUserId(userID);
		user.setUserName(userName);

		// ********************************************************************//

		// ******************** Dua Information *******************************//

		duaNumber = Integer.parseInt(ele.getChildText("DUA-NB"));

		// Nullable
		if (ele.getChildText("DESY-EXPRTN-DT") != null && ele.getChildText("DESY-EXPRTN-DT").trim().length() > 0) {
			desyExpirationdate = DateFunctions.makeDate(ele.getChildText("DESY-EXPRTN-DT").trim());
			dua.setDesyExpirationDate(desyExpirationdate);
		}

		// Setting Desy Closed boolean in dua object.
		if (ele.getChildText("DUA-STUS-CD") != null && ele.getChildText("DUA-STUS-CD").trim().length() > 0) {
			char c = ele.getChildText("DUA-STUS-CD").trim().charAt(0);

			if (c == 'C') {
				dua.setDuaClosed(true);
			} else {
				dua.setDuaClosed(false);
			} // end-if
		}

		if (ele.getChildText("SUPPRESS-COPY-FLG") != null
				&& ele.getChildText("SUPPRESS-COPY-FLG").trim().length() > 0) {
			char c = ele.getChildText("SUPPRESS-COPY-FLG").trim().charAt(0);
			if (c == 'Y' || c == 'y') {
				dua.setSupressFlag(true);
			} else {
				dua.setSupressFlag(false);
			} // end-if
		}

		dua.setDuaNumber(duaNumber);

		// ******************************************************************//

		// Request Description
		// Nullable
		requestDesc = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim());

		// reciepient Name
		// Nullable
		recipientName = DBStringConverter.checkDBCharNull(ele.getChildText("RECIP-NM").trim());

		recip.setName(recipientName);

		// Submit Date
		// Nullable
		if (ele.getChildText("CRT-DT") != null && ele.getChildText("CRT-DT").trim().length() > 0) {
			createDate = DateFunctions.parseDate(ele.getChildText("CRT-DT").trim());
		}

		// Data Description Code
		// Nullable
		dataDescription = DBStringConverter.checkDBCharNull(ele.getChildText("DATA-DESC").trim());

		Iterator requestListItr = null;
		Element requestListEle = null;
		Iterator requestItr = null;
		if (ele.getChildren(tagRequestList) != null) {
			requestListItr = ele.getChildren(tagRequestList).iterator();
		}
		while (requestListItr.hasNext()) {
			requestListEle = (Element) requestListItr.next();
		}
		if (requestListEle.getChildren(tagRequest) != null) {
			requestItr = requestListEle.getChildren(tagRequest).iterator();
		}

		// ************* Retriving sub Request information ***************************//
		Element requestEle = null;

		while (requestItr.hasNext()) {

			int requestID = -1;
			requestEle = (Element) requestItr.next();
			RequestDTO requestHead = new RequestDTO();
			// Nullable for unsubmitted requests
			if (requestEle.getChildText("DATA-RQST-ID") != null
					&& requestEle.getChildText("DATA-RQST-ID").trim().length() > 0) {
				String reqID = DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-RQST-ID"));
				if (reqID != null) {
					requestID = Integer.parseInt(reqID);
					requestHead.setRequestID(requestID);
				}
			}
			// Nullable for unsubmitted requests
			if (requestEle.getChildText("DATA-YEAR") != null
					&& requestEle.getChildText("DATA-YEAR").trim().length() > 0)
				requestHead.setDataYear(DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-YEAR")));

			// *************************************************************//

			requestHead.setStatus(requestEle.getChildText("CPLN-STUS").trim());
			requestHead.setSuperID(superID);
			requestHead.setUser(user);
			requestHead.setDua(dua);
			requestHead.setRequestDesc(requestDesc);
			requestHead.setRecipient(recip);
			requestHead.setDateCreated(createDate);
			requestHead.setDataDescriptionCode(dataDescription);
			requestList.add(requestHead);
			if (requestID == -1) {
				requestHead.setRequestID(superID);
			}
			if (superFlag == false) {
				if ((requestID != superID) && (requestID != -1)) {
					superFlag = true;
				}
			}

		}

		// if This request has multiple sub requests - generate an extra request for
		// super ID with Status and year as "SUPER"
		if (superFlag) {
			RequestDTO requestHeadSuper = new RequestDTO();
			requestHeadSuper.setSuperID(superID);
			requestHeadSuper.setUser(user);
			requestHeadSuper.setDua(dua);
			requestHeadSuper.setRequestDesc(requestDesc);
			requestHeadSuper.setRecipient(recip);
			requestHeadSuper.setDateCreated(createDate);
			requestHeadSuper.setDataDescriptionCode(dataDescription);
			requestHeadSuper.setRequestID(superID);
			requestHeadSuper.setDataYear("SUPER");
			requestHeadSuper.setStatus("SUPER");
			requestList.add(requestHeadSuper);
		}

		return requestList;
	}

	/**
	 * This method parses xml message into array of Request used for approval Search
	 * 
	 * @param iter
	 * @return
	 */
	private Collection createAprRequestHeaders(Iterator iter) {
		Element ele = null;
		RequestDTO request = null;
		LinkedHashMap<String, RequestDTO> requestHash = new LinkedHashMap<String, RequestDTO>();
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			request = createAprRequestHeader(ele);
			requestHash.put(Integer.toString(request.getSuperID()), request);
		}
		return requestHash.values();

	}

	/**
	 * This method parses xml message into Request Object used for Approval Search
	 * 
	 * @param ele
	 * @return
	 */
	private RequestDTO createAprRequestHeader(Element ele) {
		RequestDTO request = new RequestDTO();
		// super ID
		request.setSuperID(Integer.parseInt(ele.getChildText("SUPER-ID").trim()));
		// request ID
		if (ele.getChildText("DATA-RQST-ID") != null && ele.getChildText("DATA-RQST-ID").trim().length() > 0) {
			request.setRequestID(Integer.parseInt(ele.getChildText("DATA-RQST-ID")));
		}

		// **********************User Information
		// ********************************************//
		UserDTO user = new UserDTO();
		user.setUserId(ele.getChildText("USER-ID"));
		// Nullable
		user.setUserName(DBStringConverter.checkDBCharNull(ele.getChildText("CMS-PRSN-NAME")));
		// Nullable

		user.setEmail(DBStringConverter.checkDBCharNull(ele.getChildText("EMAL-ADDRS").trim()));
		request.setUser(user);

		// **********************************************************************************//

		// ***************************** Dua Information
		// ************************************//

		DuaDTO dua = new DuaDTO();
		dua.setDuaNumber(Integer.parseInt(ele.getChildText("DUA-NB")));

		dua.setExpirationDate(DateFunctions.makeDate(ele.getChildText("EXPRTN-DT")));

		// Nullable
		dua.setRequestor(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-CNTCT-NAME")));
		dua.setStudyName(ele.getChildText("STDY-NAME"));
		request.setDua(dua);

		// **********************************************************************************//

		// ********************************** Data Source Information
		// ***********************//

		DataSourceDTO dataSource = new DataSourceDTO();
		// Nullable
		dataSource.setName(DBStringConverter.checkDBCharNull(ele.getChildText("DATASTORE-NM")));
		request.setDataSource(dataSource);

		// **********************************************************************************//

		// **************************** Data Type Information
		// *******************************//
		DataTypeDTO dataType = new DataTypeDTO();
		dataType.setName(ele.getChildText("OBJ-NM"));
		request.setDataType(dataType);

		// Request Description
		// Nullable
		String requestDesc = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim());
		if (requestDesc != null)
			request.setRequestDesc(requestDesc);

		// *********************************** Recipient information
		// ************************//

		RecipientDTO recip = new RecipientDTO();
		// Nullable

		String recipID = DBStringConverter.checkDBIntegerNull(ele.getChildText("RECIP-ID"));
		if (recipID != null && recipID.length() > 0)
			recip.setRecipID(Integer.parseInt(recipID));

		// Nullable
		recip.setName(DBStringConverter.checkDBCharNull(ele.getChildText("RECIP-NM")));

		request.setRecipient(recip);

		// *****************************************************************************//

		// Submit date
		// nullable
		if (ele.getChildText("CRT-DT") != null && ele.getChildText("CRT-DT").trim().length() > 0) {
			String crtDate = ele.getChildText("CRT-DT").trim();
			if (crtDate != null) {
				Date createDate = DateFunctions.parseDate(ele.getChildText("CRT-DT").trim());
				request.setDateCreated(createDate);
			}
		}

		// Request Status
		request.setStatus(ele.getChildText("CPLN-STUS"));

		// ********************************Format information
		// *******************************//

		FormatDTO format = new FormatDTO();
		// Nullable
		format.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("FRMT-DCRN")));
		request.setFormat(format);

		// **********************************************************************************//

		// Data Desciption code
		// nullable
		request.setDataDescriptionCode(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OUT-LBL")));

		// ******************************* Media type
		// ****************************************//

		MediaTypeDTO mediaType = new MediaTypeDTO();
		// nullable
		mediaType.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("MDA-DCRN")));
		request.setMediaType(mediaType);

		// **********************************************************************************//
		// Previous request id if request was copied from other request
		// Nullable
		String prevRequest = DBStringConverter.checkDBIntegerNull(ele.getChildText("RQST-ACTY-IND"));
		if (prevRequest != null && prevRequest.length() > 0) {
			request.setPrevRequestID(Integer.parseInt(prevRequest));
		}

		// ****************************** State Information
		// ********************************//

		StatesDTO state = new StatesDTO();
		// NULLABLE
		state.setStateCode(DBStringConverter.checkDBCharNull(ele.getChildText("STATE-CD")));
		// NULLABLE
		state.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("STATE-NAME")));
		request.setState(state);

		// ********************************************************************************//

		// Comma Delimited format
		if (ele.getChildText("COMMA-DELIM-FMT") != null) {
			if (ele.getChildText("COMMA-DELIM-FMT").trim().equalsIgnoreCase("Y"))
				request.setCommaDelimited(true);
			else
				request.setCommaDelimited(false);
		}

		// ********************************out put type
		// ***********************************//
		OutputTypeDTO outputType = new OutputTypeDTO();
		// Nullable
		String outputTypeID = DBStringConverter.checkDBIntegerNull(ele.getChildText("VW-ID"));
		if (outputTypeID != null && outputTypeID.length() > 0) {
			outputType.setViewID(outputTypeID);
		}

		// Nullable
		outputType.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-OTPT-DESC")));
		request.setOutputType(outputType);

		// *******************************************************************************//

		// dropped Record count
		if (ele.getChildText("DROP-REC-CD") != null && ele.getChildText("DROP-REC-CD").length() > 0) {
			request.setDropRecordRequired(ele.getChildText("DROP-REC-CD").toUpperCase());

		}

		// Custom view Id
		// Nullable
		String customViewID = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-CSTM-VW-ID"));
		if (customViewID != null)
			request.setCustomViewID(Integer.parseInt(customViewID));

		// output file identifier
		// Nullable
		String outputFileIndn = DBStringConverter.checkDBCharNull(ele.getChildText("DATASET-NM").trim());
		outputFileIndn = formatIdentifier(outputFileIndn);
		request.setOutputFileIdentifier(outputFileIndn);

		// modification date
		if (ele.getChildText("MODE-DT") != null && ele.getChildText("MODE-DT").trim().length() > 0) {
			Date modifiedDate = DateFunctions.parseDashDateYMD(ele.getChildText("MODE-DT").trim().substring(0, 10));
			request.setDateModified(modifiedDate);
		}

		// ****************************** Approval information
		// ***************************//

		ApprovalDTO approval = new ApprovalDTO();
		// Nullable
		String approvalStatus = DBStringConverter.checkDBCharNull(ele.getChildText("APRVL-STUS").trim());
		if (approvalStatus != null && approvalStatus.length() > 0) {
			approval.setStatusCode(Integer.parseInt(approvalStatus));
		}

		// Nullable
		approval.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-APRVL-DESC").trim()));

		// Nullable
		String approverID = DBStringConverter.checkDBCharNull(ele.getChildText("APRVL-APRVR-ID"));
		if (approverID != null)
			approval.setApproverUserID(approverID);
		// Nullable
		String approverName = DBStringConverter.checkDBCharNull(ele.getChildText("APPROVER-NAME"));
		if (approverName != null)
			approval.setApproverName(approverName);

		// Nullable

		String approvalDate = DBStringConverter.checkDBCharNull(ele.getChildText("APRVL-TMSTMP"));
		if (approvalDate != null) {
			Date approvedDate = DateFunctions.parseDashDateYMD(approvalDate.trim().substring(0, 10));
			approval.setApprovalDate(approvedDate);
		}
		request.setApproval(approval);

		// ********************************************************************************//

		return request;
	}

	/**
	 * This method Modifies the outputFile identifier(removes @ from the beginning
	 * and replaces- with space for submitted requests)
	 * 
	 * @param outputFileiden
	 * @return
	 */
	private String formatIdentifier(String outputFileiden) {
		if (outputFileiden != null && outputFileiden.length() > 0) {
			// for submitted requests back end appends @ before output File identifier
			if (outputFileiden.startsWith("@")) {
				String newiden = outputFileiden.substring(1);
				newiden = newiden.replace('-', ' ');
				return newiden.trim();
			} else {
				return outputFileiden.trim();
			}
		} else
			return null;
	}

	/**
	 * adds output column names and names to search elements in Request object for
	 * un-submitted requests, as system is not storing the names while saving the
	 * requests. *
	 * 
	 * @param iter
	 * @param request
	 */
	private void addColumnNames(Iterator iter, RequestDTO request) {
		// retrieves all the elements with id and name from DSY_ELEMENT table
		LinkedHashMap colHash = geColumnNames(iter);
		SearchDTO search = request.getSearchCriteria();

		// setting Element names for all the element in Search criteria
		if (search != null) {
			List<FilterDTO> filters = search.getFilters();
			if (filters != null && filters.size() > 0) {
				for (int i = 0; i < filters.size(); i++) {
					ColumnDTO col = (ColumnDTO) colHash.get(Integer.toString(filters.get(i).getColumnID()));
					if (col != null) {
						filters.get(i).setColumnName(col.getName());
					}
				}
			}
		}

		// setting all the element names for output elements
		List<ColumnDTO> outColumns = request.getOutputColumns();
		if (outColumns != null && outColumns.size() > 0) {
			for (int i = 0; i < outColumns.size(); i++) {
				ColumnDTO col = (ColumnDTO) colHash.get(Integer.toString(outColumns.get(i).getColumnID()));
				if (col != null) {
					outColumns.get(i).setName(col.getName());
				}
			}
		}
	}

	/**
	 * This method retrieves names for all the column Id's and names from
	 * dsy_element tables
	 * 
	 * @param iter
	 * @return
	 */
	private LinkedHashMap<String, ColumnDTO> geColumnNames(Iterator iter) {
		ColumnDTO col = null;
		Element ele = null;
		LinkedHashMap<String, ColumnDTO> columnHash = new LinkedHashMap<String, ColumnDTO>();

		while (iter.hasNext()) {
			ele = (Element) iter.next();
			col = (ColumnDTO) columnHash.get(ele.getChildText("ELE-ID"));
			if (col == null) {
				col = new ColumnDTO();
				col.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID").trim()));

				col.setName(ele.getChildText("ELE-NM"));

				columnHash.put(ele.getChildText("ELE-ID"), col);
			}
		}

		return columnHash;
	}

	
}
