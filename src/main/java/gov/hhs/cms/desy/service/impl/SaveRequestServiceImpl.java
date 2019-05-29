/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.SaveRequestService;
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
import gov.hhs.cms.desy.service.dto.SearchDTO;
import gov.hhs.cms.desy.service.dto.StatesDTO;
import gov.hhs.cms.desy.service.dto.SubRequestDTO;
import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlBody;
import gov.hhs.cms.desy.service.xml.dto.XmlDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlDataRespDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlEvent;
import gov.hhs.cms.desy.service.xml.dto.XmlFilterDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlFinderFileDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlFinderFileLstDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlOutputColsLstDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlRequestDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlRequestListDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlSearchListDTO;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class SaveRequestServiceImpl implements SaveRequestService{
	private final Logger log = LoggerFactory.getLogger(SaveRequestServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	//@Inject
	//private ModelMapper mapper;
	
	/* 
	 * This method parses information from request Object into XML message
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.SaveRequest#saveUserRequest(gov.hhs.cms.desy.service.dto.RequestDTO)
	 */
	@Override
	public String saveUserRequest(RequestDTO requestDTO) {			
		log.info(" SaveRequestServiceImpl :: saveUserRequest#");
		String requestId = "";
		String requestXML = "";

		Map<String,String> submitReqMap = saveUserRequestMap(requestDTO);

		for (Map.Entry mapElement : submitReqMap.entrySet()) { 

			requestId = (String)mapElement.getKey();
			requestXML = ((String)mapElement.getValue());                         
		}
		log.info("Request ID :" + requestId);
		log.info("requestXML :" + requestXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB( requestXML);
	
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

	private Map<String, String> saveUserRequestMap(RequestDTO requestDTO) {
	    JAXBContext context = null;
	    Marshaller m = null;
	    String xmlStr = "";
	    
		log.info("SaveRequestImpl :: saveUserRequest #");		

		//retrieve the superID from the request object, if request object does not contain
		// superID, system calls getSuperID method which retrieves superID from database
		log.info("Request id from request object :" + requestDTO.getSuperID());

		String requestId = getRequestId(requestDTO);
		requestDTO.setSuperID(Integer.parseInt(requestId));

		log.info("Request is new, so new request id generated :" + requestDTO.getSuperID());

		XmlDTO xmlDTO = new XmlDTO();
		log.info("requestDTO.getUser().getUserId() :" + requestDTO.getUser().getUserId()); 

		XmlHeaderDTO xmlHeaderDTO = getXmlHeaderDTO(requestDTO.getUser().getUserId());
	    XmlSearchListDTO xMLSearchList = new XmlSearchListDTO();
	    xMLSearchList.setXmlFilterDTOLst(getXmlSearchListDTOLst(requestDTO.getFilterDTOLst()));

	    XmlEvent xmlEvent = new XmlEvent();
        xmlEvent.setPn("DSYCP020");
        
        XmlBody xmlBody = new XmlBody();
        xmlBody.setXmlEvent(xmlEvent);
	    
        XmlDataRespDTO xmlDataRespDTO = new XmlDataRespDTO();        
	    xmlDataRespDTO.setxMLSearchList(xMLSearchList); 
	    
	    XmlFinderFileLstDTO xmlFinderFileLstDTO = new XmlFinderFileLstDTO();
	    xmlFinderFileLstDTO.setXmlFinderFileDTOLst(getXmlFinderFileDTOLst(requestDTO.getFinderFileDTOLst()));
	    xmlDataRespDTO.setXmlFinderFileLstDTO(xmlFinderFileLstDTO);
	    xmlDataRespDTO.setXmlOutputColsLst(getXmlOutputColsLst(requestDTO.getOutputColumns()));
        
	    XmlRequestListDTO xmlRequestListDTO = new XmlRequestListDTO();
        xmlRequestListDTO.setXmlRequestDTOLst(getXMLSubRequestDTOLst(requestDTO.getSubRequests()));
        xmlDataRespDTO.setXmlRequestListDTO(xmlRequestListDTO);
        xmlDataRespDTO.setSuperRequest((requestDTO.isSuperRequest())?"Y":"N");
        xmlDataRespDTO.setDuaNumber(requestDTO.getDua().getDuaNumber());
        //xmlDataRespDTO.setExpirationDate(requestDTO.getDua().getFormattedExpirationDate());
        xmlDataRespDTO.setStudyName(requestDTO.getDua().getStudyName());
        xmlDataRespDTO.setReturnRequired((requestDTO.getDua().getReturnRequired())?"Y":"N");
        xmlDataRespDTO.setUserNum(String.valueOf(requestDTO.getUser().getUserNum()));
        xmlDataRespDTO.setUserId(requestDTO.getUser().getUserId());
        xmlDataRespDTO.setUserName(requestDTO.getUser().getUserName());
        xmlDataRespDTO.setRequestDesc(requestDTO.getRequestDesc()); //RQST-NM-XML Tag
        xmlDataRespDTO.setPrevRequestID(String.valueOf(requestDTO.getPrevRequestID()));  //RQST-ACTY-IND XML Tag
        xmlDataRespDTO.setDataSourceID(String.valueOf(requestDTO.getDataSource().getDataSourceId()));
        xmlDataRespDTO.setDataSourceName(requestDTO.getDataSource().getName());  //DATASTORE-NM

        Date expirationDate = (requestDTO.getDua().getFormattedExpirationDate()!=null)?new Date(requestDTO.getDua().getFormattedExpirationDate()):requestDTO.getDua().getDesyExpirationDate();
        xmlDataRespDTO.setExpirationDate(DateFunctions.formatDate(expirationDate)); //DESY-EXPRTN-DT
        xmlDataRespDTO.setSuperID(requestDTO.getSuperID());//SUPER-ID
        
        if(requestDTO.getDataType() != null) {
            xmlDataRespDTO.setDataTypeID(String.valueOf(requestDTO.getDataType().getDataTypeID())); //OBJ-ID XML Tag
            xmlDataRespDTO.setDataTypeName(requestDTO.getDataType().getName());  //OBJ-NM        	
        }

        log.info("requestDTO.getState() :" + requestDTO.getState());
        if(requestDTO.getState() != null) {
            xmlDataRespDTO.setStateCode(requestDTO.getState().getStateCode());        
            xmlDataRespDTO.setStateDescription(requestDTO.getState().getDescription()); //STATE-NAME XML Tag        	
        }

        if(requestDTO.getFormat() != null)
        	xmlDataRespDTO.setFormatID(String.valueOf(requestDTO.getFormat().getFormatId()));

        if(requestDTO.getMediaType() != null) {
            xmlDataRespDTO.setMediaTypeID(requestDTO.getMediaType().getMediaTypeID()); //MDA-ID XML Tag
            xmlDataRespDTO.setMediaTypeDescription(requestDTO.getMediaType().getDescription());   //MDA-DCRN        	
        }

        xmlDataRespDTO.setOutputFileIdentifier(requestDTO.getOutputFileIdentifier()); //DATASET-NM XML Tag
        xmlDataRespDTO.setDataDescriptionCode(requestDTO.getDataDescriptionCode()); //RQST-OUT-LBL XML Tag
        xmlDataRespDTO.setUserEmail(requestDTO.getUser().getEmail()); //EMAL_ADDRS
        xmlDataRespDTO.setCommaDelimited((requestDTO.isCommaDelimited())?"Y":"N"); //COMMA-DELIM-FMT

        if(requestDTO.getOutputType() !=null) {
            xmlDataRespDTO.setViewIdInt(requestDTO.getOutputType().getViewID()); //VW-ID
            xmlDataRespDTO.setOutputTypeDescription(requestDTO.getOutputType().getDescription());//RQST-OTPT-DESC        	
        }

        xmlDataRespDTO.setDropRecordRequired(requestDTO.getDropRecordRequired()); //DROP-REC-CD
        xmlDataRespDTO.setCustomViewID(String.valueOf(requestDTO.getCustomViewID())); //RQST_CSTM_VW_ID

        if(requestDTO.getRecipient() !=null) {
            xmlDataRespDTO.setRecipID(String.valueOf(requestDTO.getRecipient().getRecipID())); //RECIP_ID
            xmlDataRespDTO.setRecipName(requestDTO.getRecipient().getName()); //RECIP-NM
            xmlDataRespDTO.setRecipEmail(requestDTO.getRecipient().getEmail()); //ADR-EMAIL-NAME        	
        }

        xmlDataRespDTO.setBefPufView((requestDTO.isBefPufView())?"Y":"N"); //IS-BEF-PUF-VW
        xmlDataRespDTO.setSavedRequest((requestDTO.isSavedRequest())?"Y":"N");
        xmlDataRespDTO.setPagesVisited(String.valueOf(requestDTO.getPagesVisited()));

        xmlEvent.setXmlDataRespDTO(xmlDataRespDTO);

        xmlDTO.setXmlBody(xmlBody);        

        xmlDTO.setXmlHeaderDTO(xmlHeaderDTO);
        try {
    	    // create JAXB context and instantiate marshaller
   	     context = JAXBContext.newInstance(XmlDTO.class);
   	     m = context.createMarshaller();
   	     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

   	     StringWriter sw = new StringWriter();
   	     
   	    
   	     m.marshal(xmlDTO, sw);
   	     xmlStr = sw.toString();        	
        }catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
        }

	     xmlStr = xmlStr.replaceAll("UTF-8", "IBM-1140");

	     log.info("XML String from RequestDTO :\n" + xmlStr.replaceAll("_", "-"));

	     Map<String, String> requestxmlMap = new HashMap<String, String>();
	     requestxmlMap.put(requestId, xmlStr);

		return requestxmlMap;
	}
	
	private XmlHeaderDTO getXmlHeaderDTO(String userID) {
        XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
        xmlHeaderDTO.setUserId(userID);
        xmlHeaderDTO.setAction("C");
        xmlHeaderDTO.setFunction("REQUEST");
        
        return xmlHeaderDTO;
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
	
/*	private XmlRequestDTO convertSubRequestDTO(SubRequestDTO subRequestDTO) {
		
		XmlRequestDTO xmlRequestDTO = modelMapper.map(subRequestDTO, XmlRequestDTO.class);
		xmlRequestDTO.setDataYear(String.valueOf(subRequestDTO.getDataYear()));
		
		return xmlRequestDTO;
	}*/
	
	
	private XmlRequestDTO convertSubRequestDTO(SubRequestDTO subRequestDTO) {
		
		XmlRequestDTO xmlRequestDTO = new XmlRequestDTO();
		xmlRequestDTO.setDataYear(String.valueOf(subRequestDTO.getDataYear()));
		return xmlRequestDTO;
	}
	
	private List<XmlFilterDTO> getXmlSearchListDTOLst(List<FilterDTO> filterDTOLst) {
		
	    List<XmlFilterDTO> xmlFilterDTOLst = filterDTOLst.stream().map(filterDTO -> convertFilterDTO(filterDTO)).collect(Collectors.toList());

	    return xmlFilterDTOLst;
	}
	
/*	private XmlFilterDTO convertFilterDTO(FilterDTO filterDTO) {
		
		XmlFilterDTO xmlFilterDTO = modelMapper.map(filterDTO, XmlFilterDTO.class);
		
		return xmlFilterDTO;
	}*/

	private XmlFilterDTO convertFilterDTO(FilterDTO filterDTO) {
		
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
	
	private List<XmlFinderFileDTO> getXmlFinderFileDTOLst(List<FinderFileDTO> finderFileDTOLst) {
		
		List<XmlFinderFileDTO> xmlFinderFileDTOLst= finderFileDTOLst.stream().map(finderFileDTO -> convertXmlFinderFileDTO(finderFileDTO)).collect(Collectors.toList());

	    return xmlFinderFileDTOLst;
	}
	
/*	private XmlFinderFileDTO convertXmlFinderFileDTO(FinderFileDTO finderFileDTO) {
		
		XmlFinderFileDTO xmlFinderFileDTO = modelMapper.map(finderFileDTO, XmlFinderFileDTO.class);
		xmlFinderFileDTO.setColumnID(finderFileDTO.getFinderColumn().getColumnID());
		
		return xmlFinderFileDTO;
	}*/
	
	//because of the inner class, we can't use moder mapper, we have to manually set values
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
	

	
	@Override
	public String getRequestId(RequestDTO requestDTO) {
		log.info("SaveRequestImpl :: getReqesutId #");
		
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
