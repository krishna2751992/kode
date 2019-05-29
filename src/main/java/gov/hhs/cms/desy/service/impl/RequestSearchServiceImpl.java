/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.RequestSearchService;
import gov.hhs.cms.desy.service.dto.DuaDTO;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
import gov.hhs.cms.desy.service.dto.RequestDTO;
import gov.hhs.cms.desy.service.dto.RequestSearchDTO;
import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
import gov.hhs.cms.desy.service.util.DateFunctions;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
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
public class RequestSearchServiceImpl implements RequestSearchService{

	private final Logger log = LoggerFactory.getLogger(RequestSearchServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;	
	
	
	/*
	 * Retrieves list of requests based on Search criteria selected by user on Request search screen if user selected Admin Search option.
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.RequestSearch#getRequestsLst(gov.hhs.cms.desy.service.dto.RequestSearchDTO, java.lang.String)
	 */
	public List<RequestDTO> getRequestsLst(RequestSearchDTO requestSearch,String userID) {
		log.info("RequestSearchImpl :: getRequestsLst");

		String reqXML = requestXml(requestSearch,userID);

		log.info("Request XML for get getRequestsLst :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for get getRequestsLst  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<RequestDTO> requestDTOLst = getRequestDTOLst(xmlDocItr);
		
		return requestDTOLst;
	}
	
	private String requestXml(RequestSearchDTO requestSearch,String userID) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userID, Db2Crud.READ);
	    //calling CICS DSYCP046
		XMLQuery query = new XMLQuery("DSYCP046");
		// adding search parameters to xml message
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_REQ_ID,
				true, Types.INTEGER, requestSearch.getRequestID() != 0 ? Integer.toString(requestSearch
							.getRequestID()) : "-1"));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_DUA_NUM,
				true, Types.INTEGER, requestSearch.getDuaNum() != 0 ? Integer.toString(requestSearch
							.getDuaNum()) : "-1"));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_DUA_STUDY,
				true, Types.CHAR, requestSearch
							.getDuaStudyName() != null ? requestSearch.getDuaStudyName() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_SEARCH_USER_ID,
				true, Types.CHAR, requestSearch
							.getUserID() != null ? requestSearch.getUserID() : null));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_SEARCH_USER_NAME,
				true, Types.CHAR, requestSearch
							.getUserName() != null ? requestSearch.getUserName() : null));
		
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_SUBMIT_FROM_DATE,
				true, Types.DATE, requestSearch
							.getSubFrom() != null ? DateFunctions.getDateFormater(
							 requestSearch.getSubFrom(), "yyyy-MM-dd") : null));
	
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_SUBMIT_TO_DATE,
				true, Types.DATE, requestSearch
							.getSubTo() != null ? DateFunctions.getDateFormater(
							 requestSearch.getSubTo(), "yyyy-MM-dd") : null));
					 
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP046_REQUEST_ACTION,
				true, Types.CHAR, requestSearch
									.getRequestAction() != null ? requestSearch.getRequestAction() : null));
		
	
		reqMsg.addContent(query);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * This method parses XML message into list of request used for request search
	 * @param iter
	 * @return
	 */
	private List<RequestDTO> getRequestDTOLst(Iterator iter){
		List<RequestDTO> requestList = new ArrayList<RequestDTO>();
		

		int superID=0;
		String userID=null;
		String userName=null;
		int duaNumber=0;
		DuaDTO dua=null;
		String requestDesc=null;
		String recipientName=null;
		RecipientDTO recip=null;
		Date createDate=null;
		Date desyExpirationdate=null;
		String dataDescription=null;
        //tags are being stored In XMLMsgConst class with (_) for CUD opertaions but Query opertaions use (-)
	    String tagRequestList= (XmlMsgConst.REQUEST_LIST_TAG).replace('_','-');
		String tagRequest= (XmlMsgConst.REQUEST_TAG).replace('_','-'); 
		boolean superFlag=false; 
	    UserDTO user = new UserDTO();
	    recip = new RecipientDTO();
	    dua= new DuaDTO();
	    Element ele = null;
		
		
		// Super ID
		superID=Integer.parseInt(ele.getChildText("SUPER-ID").trim());

		//********************* User Information ******************************//
		userID=(ele.getChildText("USER-ID"));
		userName=ele.getChildText("CMS-PRSN-NAME");
		user.setUserId(userID);
		user.setUserName(userName);
		
		
		//********************************************************************//
		
		
		
		//******************** Dua Information *******************************//
		
		duaNumber=Integer.parseInt(ele.getChildText("DUA-NB"));
		
        //Nullable		
		if (ele.getChildText("DESY-EXPRTN-DT") != null
				&& ele.getChildText("DESY-EXPRTN-DT").trim().length() > 0)
		{
			desyExpirationdate = DateFunctions.makeDate(ele
						.getChildText("DESY-EXPRTN-DT").trim());
			dua.setDesyExpirationDate(desyExpirationdate);
		}
		 
		// Setting Desy Closed boolean in dua object.
		if(ele.getChildText("DUA-STUS-CD")!=null && ele.getChildText("DUA-STUS-CD").trim().length()>0)
		{	  
			char c=ele.getChildText("DUA-STUS-CD").trim().charAt(0);

			if (c == 'C') {
				dua.setDuaClosed(true);	
			} else {
				dua.setDuaClosed(false);
			} // end-if					
		}			
		
		if(ele.getChildText("SUPPRESS-COPY-FLG")!=null && ele.getChildText("SUPPRESS-COPY-FLG").trim().length()>0)
		{	
			char c=ele.getChildText("SUPPRESS-COPY-FLG").trim().charAt(0);
			if (c=='Y' || c=='y') {
				dua.setSupressFlag(true);	
			} else {
				dua.setSupressFlag(false);
			} // end-if					
		}
		
		dua.setDuaNumber(duaNumber);

		
		//******************************************************************//
		
		
		//Request Description
		//		Nullable
	    requestDesc = DBStringConverter.checkDBCharNull(ele.getChildText("RQST-NM").trim());
		
		
		
		//reciepient Name
		//Nullable		
		recipientName= DBStringConverter.checkDBCharNull(ele.getChildText("RECIP-NM").trim());
	    
	    recip.setName(recipientName);
        
        //Submit Date
        //Nullable		
		if (ele.getChildText("CRT-DT") != null
			  && ele.getChildText("CRT-DT").trim().length() > 0)
  		{
		   createDate = DateFunctions.parseDate(ele.getChildText("CRT-DT").trim());
	    }
	    
		
		//Data Description Code
		//Nullable
		  dataDescription=DBStringConverter.checkDBCharNull(ele.getChildText("DATA-DESC").trim());
		
		
		
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
					
					int requestID=-1;
					requestEle = (Element) requestItr.next();
					RequestDTO requestHead = new RequestDTO();
				    //Nullable for unsubmitted requests
					if (requestEle.getChildText("DATA-RQST-ID") != null
								&& requestEle.getChildText("DATA-RQST-ID").trim().length() > 0)
					{
						String reqID=DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-RQST-ID").trim());
						if(reqID!=null)
						{
							        requestID=Integer.parseInt(reqID);
									requestHead.setRequestID(requestID);
						}
					}
			        //Nullable for unsubmitted requests		
					if (requestEle.getChildText("DATA-YEAR") != null
								&& requestEle.getChildText("DATA-YEAR").trim().length() > 0)
					requestHead.setDataYear(DBStringConverter.checkDBIntegerNull(requestEle.getChildText("DATA-YEAR").trim()));
				
					
			//*************************************************************//		
					
					requestHead.setStatus(requestEle.getChildText("CPLN-STUS").trim());
					requestHead.setSuperID(superID);
					requestHead.setUser(user);
					requestHead.setDua(dua);
					requestHead.setRequestDesc(requestDesc);
					requestHead.setRecipient(recip);
					requestHead.setDateCreated(createDate);
					requestHead.setDataDescriptionCode(dataDescription);
					requestList.add(requestHead);
					if(requestID==-1)
					{
						requestHead.setRequestID(superID);
					}
					if(superFlag==false)
					{
						if((requestID != superID)&& (requestID !=-1))
						{
							superFlag=true;
						}
					}
				
				}
				
				// if This request has multiple sub requests - generate an extra request for super ID with Status and year as "SUPER"
				if(superFlag)
				{
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
	
}
