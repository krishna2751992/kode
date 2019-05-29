/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.ApprovalRequestSearchService;
import gov.hhs.cms.desy.service.dto.ApprovalDTO;
import gov.hhs.cms.desy.service.dto.ApprovalRequestSearchDTO;
import gov.hhs.cms.desy.service.dto.DataSourceDTO;
import gov.hhs.cms.desy.service.dto.DataTypeDTO;
import gov.hhs.cms.desy.service.dto.DuaDTO;
import gov.hhs.cms.desy.service.dto.FormatDTO;
import gov.hhs.cms.desy.service.dto.ManageApprovalDTO;
import gov.hhs.cms.desy.service.dto.MediaTypeDTO;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.StatesDTO;
import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUpdateStatusBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUpdateStatusDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUpdateStatusEventDTO;
import gov.hhs.cms.desy.web.rest.errors.ErrorConstants;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.ReqDBTableParams;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XMLReqParm;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class ApprovalRequestSearchServiceImpl implements ApprovalRequestSearchService {

	private final Logger log = LoggerFactory.getLogger(ApprovalRequestSearchServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves list of super requests based on Search criteria entered by user on
	 * Approval search screen.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.hhs.cms.desy.service.ApprovalRequestSearch#getApprovalRequest(gov.hhs.cms
	 * .desy.service.dto.ApprovalRequestSearchDTO, java.lang.String)
	 */
	@Override
	public List<ManageApprovalDTO> getApprovalRequest(ApprovalRequestSearchDTO approvalRequestSearch, String userId)
			{
		log.info("ApprovalRequestSearchImpl :: getApprovalRequest");

		String reqXML = requestXml(approvalRequestSearch, userId);

		log.info("Request XML for Approval Request :" + reqXML);		
		  
		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Approval Reqeust  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<RequestDTO> approvalRequestDTOLst = createApprovalRequestLst(xmlDocItr);
		List<ManageApprovalDTO> manageApprovalDTOLst = convetRequestToManageApprovalDTO(approvalRequestDTOLst);

		return manageApprovalDTOLst;
	}

	/**
	 * @param approvalRequestDTOLst
	 * @return
	 */
	private List<ManageApprovalDTO> convetRequestToManageApprovalDTO(List<RequestDTO> approvalRequestDTOLst) {
		List<ManageApprovalDTO> manageApprovalDTOLst = new ArrayList<ManageApprovalDTO>();

		for (RequestDTO requestDTO : approvalRequestDTOLst) {

			ManageApprovalDTO manageApprovalDTO = new ManageApprovalDTO();
			manageApprovalDTO.setSuperId(String.valueOf(requestDTO.getSuperID()));
			manageApprovalDTO.setRequestId(String.valueOf(requestDTO.getRequestID()));
			manageApprovalDTO.setDuaNumber(String.valueOf(requestDTO.getDua().getDuaNumber()));
			manageApprovalDTO.setSubmittedBy(requestDTO.getUser().getUserName());
			manageApprovalDTO.setSubmittedDate(String.valueOf(requestDTO.getFormattedDateCreated()));
			manageApprovalDTO.setStudyName(requestDTO.getDua().getStudyName());

			manageApprovalDTOLst.add(manageApprovalDTO);
		}

		return manageApprovalDTOLst;
	}

	/**
	 * @param requestSearch
	 * @param userId
	 * @return
	 */
	private String requestXml(ApprovalRequestSearchDTO requestSearch, String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP025
		XMLQuery query = new XMLQuery("DSYCP025");
		// adding search parameters to xml
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_REQ_ID, true, Types.INTEGER,
				requestSearch.getRequestID() != 0 ? Integer.toString(requestSearch.getRequestID()) : "-1"));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_DUA_NUM, true, Types.INTEGER,
				requestSearch.getDuaNum() != 0 ? Integer.toString(requestSearch.getDuaNum()) : "-1"));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_DUA_STUDY, true, Types.CHAR,
				requestSearch.getDuaStudyName() != null ? requestSearch.getDuaStudyName() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_SEARCH_USER_ID, true, Types.CHAR,
				requestSearch.getUserID() != null ? requestSearch.getUserID() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_SEARCH_USER_NAME, true, Types.CHAR,
				requestSearch.getUserName() != null ? requestSearch.getUserName() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_APROVER_ID, true, Types.CHAR,
				requestSearch.getApproverID() != null ? requestSearch.getApproverID() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_APROVAL_STATUS, true, Types.INTEGER,
				requestSearch.getApprovalStatusCode() >= 0 ? Integer.toString(requestSearch.getApprovalStatusCode())
						: "-1"));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_SUBMIT_FROM_DATE, true, Types.DATE,
				requestSearch.getSubFrom() != null
						? DateFunctions.getDateFormater(requestSearch.getSubFrom(), "yyyy-MM-dd")
						: null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_SUBMIT_TO_DATE, true, Types.DATE,
				requestSearch.getSubTo() != null ? DateFunctions.getDateFormater(requestSearch.getSubTo(), "yyyy-MM-dd")
						: null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_APROVED_FROM_DATE, true, Types.DATE,
				requestSearch.getApprovedFrom() != null
						? DateFunctions.getDateFormater(requestSearch.getApprovedFrom(), "yyyy-MM-dd")
						: null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP025_APROVED_TO_DATE, true, Types.DATE,
				requestSearch.getApprovedTo() != null
						? DateFunctions.getDateFormater(requestSearch.getApprovedTo(), "yyyy-MM-dd")
						: null));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<RequestDTO> createApprovalRequestLst(Iterator itr) {
		List<RequestDTO> requestDTOLst = new ArrayList<RequestDTO>();

		Element ele = null;
		while (itr.hasNext()) {
			ele = (Element) itr.next();

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

			user.setEmail(DBStringConverter.checkDBCharNull(ele.getChildText("EMAL-ADDRS")));
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
			String requestDesc = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM"));
			if (requestDesc != null)
				request.setRequestDesc(requestDesc);

			// *********************************** Recipient information
			// ************************//

			RecipientDTO recip = new RecipientDTO();
			// Nullable

			String recipID = DBStringConverter.checkDBIntegerNull(ele.getChildText("RECIP-ID").trim());
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
			String prevRequest = DBStringConverter.checkDBIntegerNull(ele.getChildText("RQST-ACTY-IND").trim());
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
			String outputTypeID = DBStringConverter.checkDBIntegerNull(ele.getChildText("VW-ID").trim());
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
			String outputFileIndn = DBStringConverter.checkDBCharNull(ele.getChildText("DATASET-NM"));
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
			String approvalStatus = DBStringConverter.checkDBCharNull(ele.getChildText("APRVL-STUS"));
			if (approvalStatus != null && approvalStatus.length() > 0) {
				approval.setStatusCode(Integer.parseInt(approvalStatus));
			}

			// Nullable
			approval.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("RQST-APRVL-DESC")));

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

			requestDTOLst.add(request);
		}

		return requestDTOLst;
	}

	/**
	 * This method Modifies the outputFile identifier(removes @ from the begining
	 * and replaces- with space for submitted requests)
	 * 
	 * @param outputFileiden
	 * @return
	 */
	private static String formatIdentifier(String outputFileiden) {
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

	private String getRequestXmlForUpdateStatus(String userId, String superId, int approvalStatus)  {
		JAXBContext context = null;
	    Marshaller m;
	    String xmlStr = "";
	    
		XmlUpdateStatusDTO xmlUpdateStatusDTO = new XmlUpdateStatusDTO();
		XmlUpdateStatusBodyDTO xmlUpdateStatusBodyDTO = new XmlUpdateStatusBodyDTO();

		XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
		xmlHeaderDTO.setAction("U");
		xmlHeaderDTO.setFunction("UPDATE");
		xmlHeaderDTO.setUserId(userId);

		XmlUpdateStatusEventDTO xmlUpdateStatusEventDTO = new XmlUpdateStatusEventDTO();
		xmlUpdateStatusEventDTO.setPn("DSYCP027");
		xmlUpdateStatusEventDTO.setApprovalSuperId(superId);
		xmlUpdateStatusEventDTO.setApprovalApprovarId(userId);
		xmlUpdateStatusEventDTO.setApprovalStatus(String.valueOf(approvalStatus));

		xmlUpdateStatusBodyDTO.setXmlUpdateStatusEventDTO(xmlUpdateStatusEventDTO);
		xmlUpdateStatusDTO.setXmlHeaderDTO(xmlHeaderDTO);
		xmlUpdateStatusDTO.setXmlUpdateStatusBodyDTO(xmlUpdateStatusBodyDTO);
		try {
			// create JAXB context and instantiate marshaller
			context = JAXBContext.newInstance(XmlUpdateStatusDTO.class);
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			StringWriter sw = new StringWriter();
			m.marshal(xmlUpdateStatusDTO, sw);
			xmlStr = sw.toString();			
		} catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
		}


		xmlStr = xmlStr.replaceAll("UTF-8", "IBM-1140");

		return xmlStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.hhs.cms.desy.service.ApprovalRequestSearch#updateApprovalStatus(java.lang
	 * .String, java.util.List, int)
	 */
	public String updateApprovalStatus(String userId, List<String> superIdLst, int approvalStatus)
	{
		String errorCodeString = "";
		userId = userId.toUpperCase();
		String requestXML = "";
		// creating hashmap so that mutltiple error messages can be stored and can be
		// returned to front end.
		Map errorHash = new HashMap();
		for (int i = 0; i < superIdLst.size() ; i++) {
			// creating UpdateStatus object so that information from new status can be
			// stored
			// and xml can be generated using dbmappings.xml

			requestXML = getRequestXmlForUpdateStatus(userId, superIdLst.get(i), approvalStatus);

			log.info("requestXML :" + requestXML);

			String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);

			log.info("Response XML for login user saved and submitted request(s)  :" + iibResponse);

			Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);
			Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);
			
			errorCodeString = requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString();
			log.info("error Code, {}",  errorCodeString);
			// if there is an error back end returns 4 in error_code tag in returning xml
			// and error code in <error_msg> tag
			// but there could be same error in two requests but to the front end it will
			// displayed once with ttttttwo superid's
			if (errorCodeString.equalsIgnoreCase(String.valueOf(ErrorConstants.ERROR_CODE_4))) {
				String errorCode = (String) requestIdMap.get(XmlMsgConst.ERRMSG_TAG);
				errorHash.put(superIdLst.get(i).toString(), errorCode);

			}
		}

		List<String> errorRequestIds = (errorHash != null) ? new ArrayList<>(errorHash.keySet())
				: new ArrayList<String>();

		return errorCodeString;

	}
	

}
