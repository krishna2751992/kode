package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.service.RequestStatusService;
import gov.hhs.cms.desy.service.dto.DsyRequestsStatusDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.ReqDBTableParams;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XMLReqParm;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

@Named
public class RequestStatusServiceImpl extends HelperService implements RequestStatusService {

	private static final String DESY_EXPRTN_DT = "DESY-EXPRTN-DT";
	private final Logger log = LoggerFactory.getLogger(RequestStatusServiceImpl.class);

	
	/**
	 * Get a new SuperID from database based on sequence and is called by Save
	 * Request and Submit Request methods.
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public String getRequestId() {
		log.info("RequestStatusServiceImpl :: getRequestId#");
		String reqXML = requestXmlForRequestId(this.getCurrentUserId());
		log.info("Request XML for Request Id: {}", reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		log.info("Response XML for Request Id: {}", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map hash = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);
		// retrieves the superID from xml
		String requestID =  getRetCode(hash);
		log.info("request id, {}:" , requestID);

		return requestID;
	}

	/**
	 * This method returns all request created by the login user
	 */
	@Override
	public List<DsyRequestsStatusDTO> manageRequests() {
		log.info("manageRequests #");
		List<DsyRequestsStatusDTO> dsyRequestsStatusDTOLst = new ArrayList<>();

		String reqXML = requestXml(this.getCurrentUserId());
		log.info("Request XML for login user saved and submitted request(s): {}", reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for login user saved and submitted request(s): {}", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		Element ele = null;
		while (xmlDocItr.hasNext()) {
			ele = (Element) xmlDocItr.next();

			getRequestStatus(ele, dsyRequestsStatusDTOLst);
		}


		return dsyRequestsStatusDTOLst;
	}

	/**
	 * @param userId
	 * @return
	 */
	private String requestXmlForRequestId(String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_UPDATE, userId, Db2Crud.CREATE);
		// calling CICS DSYCP011
		XMLQuery query = new XMLQuery("DSYCP011");
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param hash
	 * @return
	 */
	private String getRetCode(Map hash) {

		if (hash.get(XmlMsgConst.ERRCODE_TAG) != null)
			return hash.get(XmlMsgConst.RETCODE_TAG).toString();
		else
			return "";
	}
	
	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP014
		XMLQuery query = new XMLQuery("DSYCP014");
		// adding the search parameters with the xml
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_REQ_ID, true, Types.INTEGER, "-1"));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_DUA_NUM, true, Types.INTEGER, "-1"));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_DUA_STUDY, true, Types.CHAR, null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_SEARCH_USER_ID, true, Types.CHAR, userId));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_SEARCH_USER_NAME, true, Types.CHAR, null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_SUBMIT_FROM_DATE, true, Types.DATE, null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_SUBMIT_TO_DATE, true, Types.DATE, null));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP014_REQUEST_ACTION, true, Types.CHAR, "BOTH"));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	

	/**
	 * @param ele
	 * @return
	 */
	private void getRequestStatus(Element ele, List<DsyRequestsStatusDTO> dsyRequestsStatusDTOLst){		
		
		log.info("RequestStatusImpl :: getRequestStatus #");
		
		DsyRequestsStatusDTO dsyRequestsDTO = new DsyRequestsStatusDTO();
		
		//tags are being stored In XMLMsgConst class with (_) for CUD opertaions but Query opertaions use (-)
		String tagRequestList= (XmlMsgConst.REQUEST_LIST_TAG).replace('_','-');
        String tagRequest= (XmlMsgConst.REQUEST_TAG).replace('_','-'); 
        
        boolean superFlag=false; 
        String superId = ele.getChildText("SUPER-ID").trim();
        String userId = ele.getChildText("USER-ID");
        String userName = ele.getChildText("CMS-PRSN-NAME");  
        String duaNumber = ele.getChildText("DUA-NB");
        String expirationDate = "";
        boolean duaClosed = false;
        boolean supressFlag = false;
        String requestDesc = "";
        String recipientName = "";
        String createDate = "";
        String dataDescription ="";
        //Nullable           
        if (ele.getChildText(DESY_EXPRTN_DT) != null
                     && ele.getChildText(DESY_EXPRTN_DT).trim().length() > 0) {
             
			log.info("DUA Expiration date, {}", ele.getChildText(DESY_EXPRTN_DT).trim());
              
              expirationDate = ele.getChildText(DESY_EXPRTN_DT).trim();
              log.info("expirationDate :" + expirationDate);
        }
        
        // Setting Desy Closed boolean in dua object.
        if(ele.getChildText("DUA-STUS-CD")!=null && ele.getChildText("DUA-STUS-CD").trim().length()>0)
        {        
               char c=ele.getChildText("DUA-STUS-CD").trim().charAt(0);

               if (c == 'C') {
            	   duaClosed = true;   
               } else {
            	   duaClosed = false;
               } // end-if                             
        }                   
        
        if(ele.getChildText("SUPPRESS-COPY-FLG")!=null && ele.getChildText("SUPPRESS-COPY-FLG").trim().length()>0)
        {      
               char c=ele.getChildText("SUPPRESS-COPY-FLG").trim().charAt(0);
               if (c=='Y' || c=='y') {
            	   supressFlag = true; 
               } else {
            	   supressFlag = false;
               } // end-if                             
        }
        
        //******************************************************************//
        
        
        //Request Description //Nullable
        requestDesc = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim());
        
        //reciepient Name //Nullable          
        recipientName = DBStringConverter.checkDBCharNull(ele.getChildText("RECIP-NM").trim());
   
        //Submit Date //Nullable        
        if (ele.getChildText("CRT-DT") != null
                 && ele.getChildText("CRT-DT").trim().length() > 0)
        {
        	Date createDateDt = DateFunctions.makeDate(ele
                    .getChildText("CRT-DT").trim());
        	log.info("Request Date before formated :" + createDateDt);
        	
        	createDate = DateFunctions.formatXmlDateStr(ele.getChildText("CRT-DT").trim());
        	log.info("Request Date After fo rmated:" + createDate);
        	
        }     
        
        //Data Description Code    //Nullable
        dataDescription = DBStringConverter.checkDBCharNull(ele.getChildText("DATA-DESC").trim());
        
        Iterator requestListItr=null;
        Element requestListEle=null;
        Iterator requestItr=null;
        if (ele.getChildren(tagRequestList) != null)
        {
                     requestListItr = ele.getChildren(tagRequestList).iterator();
        }
        while (requestListItr.hasNext())
        {
                     requestListEle = (Element) requestListItr.next();
        }
        if (requestListEle.getChildren(tagRequest) != null)
        {
                     requestItr = requestListEle.getChildren(tagRequest).iterator();
        }
                     
                     
        //************* Retriving sub Request information ***************************//
                     Element requestEle = null;                     

                     while (requestItr.hasNext())
                     {
                            
                            int requestId=-1;
                            requestEle = (Element) requestItr.next();
                            DsyRequestsStatusDTO dsyRequestsStatusDTO = new DsyRequestsStatusDTO();
                         //Nullable for unsubmitted requests
                            if (requestEle.getChildText("DATA-RQST-ID") != null
                                                && requestEle.getChildText("DATA-RQST-ID").trim().length() > 0)
                            {
                                   String reqId=DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-RQST-ID").trim());
                                   if(reqId!=null)
                                   {    
                                	   requestId = Integer.parseInt(reqId);
                                       dsyRequestsStatusDTO.setRequestId(reqId);
                                   }
                            }
                       //Nullable for unsubmitted requests          
                            if (requestEle.getChildText("DATA-YEAR") != null
                                                && requestEle.getChildText("DATA-YEAR").trim().length() > 0)
                            	dsyRequestsStatusDTO.setRequestYear(DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-YEAR").trim()));
                     
                            
               //*************************************************************//        
                            
                            dsyRequestsStatusDTO.setRequestStatus(requestEle.getChildText("CPLN-STUS").trim());
                            dsyRequestsStatusDTO.setSuperId(superId);
                            dsyRequestsStatusDTO.setUserId(userId);
                            dsyRequestsStatusDTO.setUserName(userName);
                            dsyRequestsStatusDTO.setDuaNumber(duaNumber);
                            dsyRequestsStatusDTO.setDuaClosed(duaClosed);
                            dsyRequestsStatusDTO.setSupressFlag(supressFlag);                     
                            dsyRequestsStatusDTO.setRequestDesc(requestDesc);
                            dsyRequestsStatusDTO.setRecipientName(recipientName);                            
                            dsyRequestsStatusDTO.setCreateDate(createDate);                            
                            dsyRequestsStatusDTO.setDataDescription(dataDescription);  
                            dsyRequestsStatusDTO.setDesyExpirationDate(expirationDate);

                            dsyRequestsStatusDTOLst.add(dsyRequestsStatusDTO); 
                            
                            if(requestId==-1)
                            {
                            	dsyRequestsStatusDTO.setRequestId(superId);
                            }
                            if(superFlag==false)
                            {
                                   if((requestId != Integer.parseInt(superId))&& (requestId !=-1))
                                   {
                                         superFlag=true;
                                   }
                            }
                     
                     }
                     
                     // if This request has multiple sub requests - generate an extra request for super ID with Status and year as "SUPER"
                     if(superFlag)
                     {                        
                        DsyRequestsStatusDTO superRequest = new DsyRequestsStatusDTO();
                        superRequest.setSuperId(superId);
                        superRequest.setUserId(userId);
                        superRequest.setUserName(userName);
                        superRequest.setDuaNumber(duaNumber);
                        superRequest.setDuaClosed(duaClosed);
                        superRequest.setSupressFlag(supressFlag); 
                        superRequest.setRequestDesc(requestDesc);
                        superRequest.setRecipientName(recipientName); 
                        superRequest.setCreateDate(createDate); 
                        superRequest.setDataDescription(dataDescription);
                        superRequest.setDesyExpirationDate(expirationDate);
                        
                        superRequest.setRequestId(superId);
                        superRequest.setRequestYear("SUPER");
                        superRequest.setRequestStatus("SUPER");                     
                        
                        dsyRequestsStatusDTOLst.add(superRequest);
                        
                     }
		
		
	}	
	
}
