/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.ErrorMsgService;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
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
public class ErrorMsgServiceImpl extends HelperService implements ErrorMsgService {

	private final Logger log = LoggerFactory.getLogger(ErrorMsgServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.ErrorMsg#getErrorMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public String getErrorMessage(String errorCode) {
		log.info("ErrorMsgImpl :: getErrorMessage #");
		log.info("errorCode :" + errorCode );		
		
		String reqXML = requestXml(errorCode, this.getCurrentUserId());
		
		log.info("Request XML  to get error message :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML  to validate finder file   :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		String errMsg = createErrorMessage(xmlDocItr);
		
		return errMsg;
	}
	
	/**
	 * @param errorCode
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private String requestXml(String errorCode,String userId) {
		userId=userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// creating a query object
		XMLQuery query = new XMLQuery("DSYCP039");
		//adding parameter error code to query object.
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP039_ERROR_CODE,
							true, Types.CHAR, errorCode));
		reqMsg.addEvent(query);
		
		return reqMsg.toString();
	}
	
	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private String createErrorMessage(Iterator iter) {
		
		int recCount = 0;
		String ErrorMsg = "";
		String ErrorCode="";
		Element ele = null;
		while (iter.hasNext())
		{
			   ele = (Element) iter.next();
			   if (recCount++ == 0)
			   {
				   ErrorCode=ele.getChildText("ERROR-CODE").trim();
				   //Nullable field
				   if (ele.getChildText("ERROR-TEXT") != null
						   && ele.getChildText("ERROR-TEXT").trim().length() > 0)
				   ErrorMsg=ele.getChildText("ERROR-TEXT").trim();
			   }
		}
		
		return ErrorMsg;
	}
	
	
}
