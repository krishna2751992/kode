/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.ValidateUserService;
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
public class ValidateUserServiceImpl implements ValidateUserService{

	private final Logger log = LoggerFactory.getLogger(ValidateUserServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	/**
	 * This method validates user based on userID. 
	 * It is being used during user login to validate. It does the following validations
	 * 1. User should exist in dadss system as desy user.
	 * 2. user should have dua associated with user account.
	 * 3. User should have at least one dua which is not expired.
	 * System returns different error messages from DSY_EVENT_ERROR table based an above validations
	 */
	
	@Override
	public boolean isValidUser(String userId) {
		log.info("ValidateUserImpl : isValidUser #");
		log.info("userId #" + userId);
		
		boolean isValidUser = true;
		String requestXML = requestXml(userId);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);

		log.info("Response XML to validate login user   :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

	    log.info("Error code value in isValidUser response XML :" + requestIdMap.get(XmlMsgConst.ERRCODE_TAG));

	    // retrieves the superID from xml
	    String errorCode =  requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString();

	    log.info("Error Code : " + errorCode);
	    
		   // if there is an error message - system throws an exception 
		   // with errormessage as returned from back end
		   if(!(errorCode.equalsIgnoreCase("00000000"))) {
			   isValidUser = false;
			throw new SystemException(requestIdMap.get("ERROR-TEXT").toString());
		   }
		   
		return isValidUser;
	}
	
	private String requestXml(String userId) {
		//converting user id to uppercase as user information is stored in DESY and DADSS database as uppercase
		userId=userId.toUpperCase();
		//creating xml message for DSYCP040
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP040");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP040_USER_ID,
				  true, Types.CHAR, userId));
		reqMsg.addEvent(query);
		
		return reqMsg.getPrettyXML();
	}
}
