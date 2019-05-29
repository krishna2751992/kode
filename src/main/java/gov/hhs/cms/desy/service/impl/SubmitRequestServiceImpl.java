/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.SubmitRequestService;
import gov.hhs.cms.desy.service.dto.ColumnDTO;
import gov.hhs.cms.desy.service.dto.FilterDTO;
import gov.hhs.cms.desy.service.dto.FinderFileDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.SubRequestDTO;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlFilterDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlFinderFileDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlFinderFileLstDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlOutputColsLstDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlRequestDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlRequestListDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlSearchListDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlSubmitRequestBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlSubmitRequestDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlSubmitRequestEventDTO;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;



/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class SubmitRequestServiceImpl implements SubmitRequestService{

	private final Logger log = LoggerFactory.getLogger(SaveRequestServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	

	/* 
	 * This method parses information from request Object into xml message
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.SubmitRequest#submitUserRequest(gov.hhs.cms.desy.service.dto.RequestDTO)
	 */
	@Override
	public String submitUserRequest(RequestDTO requestDTO) {	

		String requestId = "";
		String requestXML = "";

		Map<String,String> submitReqMap = submitUserRequestMap(requestDTO);

		for (Map.Entry mapElement : submitReqMap.entrySet()) { 			
			requestId = (String)mapElement.getKey();
			requestXML = ((String)mapElement.getValue()); 
		}

		log.info("Request ID :" + requestId);
		log.info("requestXML :" + requestXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);
		
		log.info("Response XML for login user saved and submitted request(s)  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

	    log.info("Error code value in Request id response XML :" + requestIdMap.get(XmlMsgConst.ERRCODE_TAG));

	    // retrieves the superID from xml
	    requestId=  requestIdMap.get(XmlMsgConst.RETCODE_TAG).toString();

	    log.info("Reqeust Id from BDC : " + requestId);

		return requestId;
	}	

	/**
	 * @param requestDTO
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> submitUserRequestMap(RequestDTO requestDTO) {		

		log.info("SubmitRequestImpl :: submitUserRequest #");
	    JAXBContext context = null;
	    Marshaller m = null;
	    String xmlStr = "";
	    
		//retrieve the superID from the request object, if request object does not contain
		// superID, system calls getSuperID method which retrieves superID from database

		log.info("Request id from request object :" + requestDTO.getSuperID());

		String requestId = getRequestId(requestDTO);
		requestDTO.setSuperID(Integer.parseInt(requestId));

		XmlSubmitRequestDTO xmlSubmitRequestDTO = new XmlSubmitRequestDTO();
		XmlHeaderDTO xmlHeaderDTO = getXmlHeaderDTO(requestDTO.getUser().getUserId());

		XmlSubmitRequestEventDTO xmlSubmitRequestEventDTO = new XmlSubmitRequestEventDTO();
		xmlSubmitRequestEventDTO.setPn("DSYCP021");
		xmlSubmitRequestEventDTO.setSuperRequest((requestDTO.isSuperRequest())?"Y":"N");
		xmlSubmitRequestEventDTO.setDuaNumber(requestDTO.getDua().getDuaNumber());
		//xmlSubmitRequestEventDTO.setExpirationDate(requestDTO.getDua().getFormattedExpirationDate());
		xmlSubmitRequestEventDTO.setStudyName(requestDTO.getDua().getStudyName());
		xmlSubmitRequestEventDTO.setReturnRequired((requestDTO.getDua().getReturnRequired())?"Y":"N");
		xmlSubmitRequestEventDTO.setUserNum(String.valueOf(requestDTO.getUser().getUserNum()));
		xmlSubmitRequestEventDTO.setUserId(requestDTO.getUser().getUserId());
		xmlSubmitRequestEventDTO.setUserName(requestDTO.getUser().getUserName());
		xmlSubmitRequestEventDTO.setRequestDesc(requestDTO.getRequestDesc()); //RQST-NM XML Tag
		xmlSubmitRequestEventDTO.setPrevRequestID(String.valueOf(requestDTO.getPrevRequestID()));  //RQST-ACTY-IND XML Tag

        Date expirationDate = (requestDTO.getDua().getFormattedExpirationDate()!=null)?new Date(requestDTO.getDua().getFormattedExpirationDate()):requestDTO.getDua().getDesyExpirationDate();
        xmlSubmitRequestEventDTO.setExpirationDate(DateFunctions.formatDate(expirationDate)); //DESY-EXPRTN-DT
        xmlSubmitRequestEventDTO.setSuperID(requestDTO.getSuperID());//SUPER-ID
        
		if(requestDTO.getDataType() != null) {
			xmlSubmitRequestEventDTO.setDataTypeID(String.valueOf(requestDTO.getDataType().getDataTypeID())); //OBJ-ID XML Tag
			xmlSubmitRequestEventDTO.setDataTypeName(requestDTO.getDataType().getName());  //OBJ-NM        	
        }

		xmlSubmitRequestEventDTO.setDataSourceID(String.valueOf(requestDTO.getDataSource().getDataSourceId()));
		xmlSubmitRequestEventDTO.setDataSourceName(requestDTO.getDataSource().getName());  //DATASTORE-NM

        if(requestDTO.getState() != null) {
        	xmlSubmitRequestEventDTO.setStateCode(requestDTO.getState().getStateCode());        
        	xmlSubmitRequestEventDTO.setStateDescription(requestDTO.getState().getDescription()); //STATE-NAME XML Tag        	
        }

        if(requestDTO.getFormat() != null)
        	xmlSubmitRequestEventDTO.setFormatID(String.valueOf(requestDTO.getFormat().getFormatId()));

        if(requestDTO.getMediaType() != null) {
        	xmlSubmitRequestEventDTO.setMediaTypeID(requestDTO.getMediaType().getMediaTypeID()); //MDA-ID XML Tag
        	xmlSubmitRequestEventDTO.setMediaTypeDescription(requestDTO.getMediaType().getDescription());   //MDA-DCRN        	
        }

        xmlSubmitRequestEventDTO.setOutputFileIdentifier(requestDTO.getOutputFileIdentifier()); //DATASET-NM XML Tag
        xmlSubmitRequestEventDTO.setDataDescriptionCode(requestDTO.getDataDescriptionCode()); //RQST-OUT-LBL XML Tag
        xmlSubmitRequestEventDTO.setUserEmail(requestDTO.getUser().getEmail()); //EMAL_ADDRS
        xmlSubmitRequestEventDTO.setCommaDelimited((requestDTO.isCommaDelimited())?"Y":"N"); //COMMA-DELIM-FMT

        if(requestDTO.getOutputType() !=null) {
        	xmlSubmitRequestEventDTO.setViewIdInt(requestDTO.getOutputType().getViewID()); //VW-ID
        	xmlSubmitRequestEventDTO.setOutputTypeDescription(requestDTO.getOutputType().getDescription());//RQST-OTPT-DESC        	
        }

        xmlSubmitRequestEventDTO.setDropRecordRequired(requestDTO.getDropRecordRequired()); //DROP-REC-CD
        xmlSubmitRequestEventDTO.setCustomViewID(String.valueOf(requestDTO.getCustomViewID())); //RQST_CSTM_VW_ID

        if(requestDTO.getRecipient() !=null) {
        	xmlSubmitRequestEventDTO.setRecipID(String.valueOf(requestDTO.getRecipient().getRecipID())); //RECIP_ID
        	xmlSubmitRequestEventDTO.setRecipName(requestDTO.getRecipient().getName()); //RECIP-NM
        	xmlSubmitRequestEventDTO.setRecipEmail(requestDTO.getRecipient().getEmail()); //ADR-EMAIL-NAME        	
        }

        xmlSubmitRequestEventDTO.setBefPufView((requestDTO.isBefPufView())?"Y":"N"); //IS-BEF-PUF-VW
        xmlSubmitRequestEventDTO.setSavedRequest((requestDTO.isSavedRequest())?"Y":"N");
        xmlSubmitRequestEventDTO.setPagesVisited(String.valueOf(requestDTO.getPagesVisited()));

	    XmlSearchListDTO xMLSearchList = new XmlSearchListDTO();
	    xMLSearchList.setXmlFilterDTOLst(getXmlSearchListDTOLst(requestDTO.getFilterDTOLst()));

	    xmlSubmitRequestEventDTO.setxMLSearchList(xMLSearchList);

	    XmlFinderFileLstDTO xmlFinderFileLstDTO = new XmlFinderFileLstDTO();
	    xmlFinderFileLstDTO.setXmlFinderFileDTOLst(getXmlFinderFileDTOLst(requestDTO.getFinderFileDTOLst()));

	    xmlSubmitRequestEventDTO.setXmlFinderFileLstDTO(xmlFinderFileLstDTO);

	    xmlSubmitRequestEventDTO.setXmlOutputColsLst(getXmlOutputColsLst(requestDTO.getOutputColumns()));

	    XmlRequestListDTO xmlRequestListDTO = new XmlRequestListDTO();
        xmlRequestListDTO.setXmlRequestDTOLst(getXMLSubRequestDTOLst(requestDTO.getSubRequests()));

        xmlSubmitRequestEventDTO.setXmlRequestListDTO(xmlRequestListDTO);

		XmlSubmitRequestBodyDTO xmlBody = new XmlSubmitRequestBodyDTO();
        xmlBody.setXmlSubmitRequestEventDTO(xmlSubmitRequestEventDTO);

	    xmlSubmitRequestDTO.setXmlHeaderDTO(xmlHeaderDTO);
	    xmlSubmitRequestDTO.setXmlSubmitRequestBodyDTO(xmlBody);
	    try {
		    // create JAXB context and instantiate marshaller
		     context = JAXBContext.newInstance(XmlSubmitRequestDTO.class);
		     m = context.createMarshaller();
		     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		     StringWriter sw = new StringWriter();	     
		     m.marshal(xmlSubmitRequestDTO, sw);
		     xmlStr = sw.toString();	     

		     xmlStr = xmlStr.replaceAll("UTF-8", "IBM-1140");
		     log.info("XML String \n" + xmlStr);	    	
	    }catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
	    }


	     Map<String, String> requestxmlMap = new HashMap<String, String>();
	     requestxmlMap.put(requestId, xmlStr);

		return requestxmlMap;
	}
	
	private List<XmlFinderFileDTO> getXmlFinderFileDTOLst(List<FinderFileDTO> finderFileDTOLst) {
		
		List<XmlFinderFileDTO> xmlFinderFileDTOLst= finderFileDTOLst.stream().map(finderFileDTO -> convertXmlFinderFileDTO(finderFileDTO)).collect(Collectors.toList());

	    return xmlFinderFileDTOLst;
	}
	
	private XmlFinderFileDTO convertXmlFinderFileDTO(FinderFileDTO finderFileDTO) {
		
		XmlFinderFileDTO xmlFinderFileDTO = new XmlFinderFileDTO();
		xmlFinderFileDTO.setColumnID(finderFileDTO.getFinderColumn().getColumnID());
		xmlFinderFileDTO.setFileName(finderFileDTO.getFileName());
		xmlFinderFileDTO.setFilIcdIndCd(finderFileDTO.getFilIcdIndCd());
		xmlFinderFileDTO.setGroupID(finderFileDTO.getGroupID());
		xmlFinderFileDTO.setHeaderStartPosition(finderFileDTO.getHeaderStartPosition());
		xmlFinderFileDTO.setStartPosition(finderFileDTO.getStartPosition());
		
		return xmlFinderFileDTO;
	}

	private XmlHeaderDTO getXmlHeaderDTO(String userID) {
        XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
        xmlHeaderDTO.setUserId(userID);
        xmlHeaderDTO.setAction("C");
        xmlHeaderDTO.setFunction("REQUEST");
        
        return xmlHeaderDTO;
	}
	
	private List<XmlFilterDTO> getXmlSearchListDTOLst(List<FilterDTO> filterDTOLst) {
		
	    List<XmlFilterDTO> xmlFilterDTOLst = filterDTOLst.stream().map(filterDTO -> convertFilterDTO(filterDTO)).collect(Collectors.toList());

	    return xmlFilterDTOLst;
	}
	
	private XmlFilterDTO convertFilterDTO(FilterDTO filterDTO) {		
		//XmlFilterDTO xmlFilterDTO = modelMapper.map(filterDTO, XmlFilterDTO.class);
		
		XmlFilterDTO xmlFilterDTO = new XmlFilterDTO();
		xmlFilterDTO.setColumnID(filterDTO.getColumnID());
		xmlFilterDTO.setEleIcdIndCd(filterDTO.getEleIcdIndCd());
		xmlFilterDTO.setGroupID(filterDTO.getGroupID());
		xmlFilterDTO.setLookupText(filterDTO.getLookupText());
		xmlFilterDTO.setOperator(filterDTO.getOperator());
		xmlFilterDTO.setSquenceID(filterDTO.getSquenceID());
		xmlFilterDTO.setValue(filterDTO.getValue());
		xmlFilterDTO.setWhereClause(filterDTO.getWhereClause());
		return xmlFilterDTO;
	}
	
	private XmlOutputColsLstDTO getXmlOutputColsLst(List<ColumnDTO> columnDTOLst) {
        XmlOutputColsLstDTO xmlOutputColsLst = new XmlOutputColsLstDTO();
        List<String> outputElmIdLst = columnDTOLst.stream().map(colmDto -> String.valueOf(colmDto.getColumnID())).collect(Collectors.toList());
        xmlOutputColsLst.setElmIdLst(outputElmIdLst);
		
        return xmlOutputColsLst;
	}
	
	private List<XmlRequestDTO> getXMLSubRequestDTOLst(List<SubRequestDTO> subRequestDTOLst){
		List<XmlRequestDTO> xmlRequestDTOLst = subRequestDTOLst.stream().map(subReqDTO -> convertSubRequestDTO(subReqDTO)).collect(Collectors.toList());
		
		return xmlRequestDTOLst;		
	}
	
	private XmlRequestDTO convertSubRequestDTO(SubRequestDTO subRequestDTO) {
		
		XmlRequestDTO xmlRequestDTO = new XmlRequestDTO();
		xmlRequestDTO.setDataYear(String.valueOf(subRequestDTO.getDataYear()));
		return xmlRequestDTO;
	}
	
	@Override
	public String getRequestId(RequestDTO requestDTO) {
		log.info("SubmitRequestImpl :: getReqesutId #");
		
		String superId ="";
		superId= String.valueOf(requestDTO.getSuperID());
		if(superId== null || (superId.length() > 0 && superId.trim()=="") || Integer.parseInt(superId)==0) {
			
			String reqXML = requestIdRequestXml(requestDTO.getUser().getUserId());
			
			String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
			
			log.info("Response XML for Data sources  :" + iibResponse);			
		
			Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);
			
			Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);
			
		    log.info("Error code value in Request id response XML :" + requestIdMap.get(XmlMsgConst.ERRCODE_TAG));
		 
		    // retrieves the superID from xml
		    superId=  requestIdMap.get(XmlMsgConst.RETCODE_TAG).toString();

		    log.info("NEW Reqeust Id from BDC : " + superId);
		}
		
		return superId;
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private String requestIdRequestXml(String userId) {
		log.info("SaveRequestImpl :: requestIdRequestXml #");
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_UPDATE, userId, Db2Crud.CREATE);
		//calling CICS DSYCP011
		XMLQuery query = new XMLQuery("DSYCP011");
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}	

}
