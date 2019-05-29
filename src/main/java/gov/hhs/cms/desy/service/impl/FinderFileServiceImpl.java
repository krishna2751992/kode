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

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.FinderFileService;
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
public class FinderFileServiceImpl implements FinderFileService {

	private final Logger log = LoggerFactory.getLogger(FinderFileServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Validates the finder file on the mainframe when user is adding finder file into Search Criteria on 
	 * Search screen while entering Request information.
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.FinderFile#isFinderFileExist(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean isFinderFileExist(String fileName, String userId) {
		log.info("FinderFileServiceImpl :: isFinderFileExist$");
		String reqXML = requestXml(fileName, userId);
		
		log.info("Request XML  to validate finder file :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML  to validate finder file   :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		boolean isFinderFileExist = validateFinderFile(xmlDocItr);

		return isFinderFileExist;
	}

	/**
	 * @param fileName
	 * @param userId
	 * @return
	 */
	private String requestXml(String fileName, String userId) {
		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP015
		XMLQuery query = new XMLQuery("DSYCP015");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP015_FILE_NAME, true, Types.CHAR, fileName));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param itr
	 * @return
	 */
	private boolean validateFinderFile(Iterator itr) {
		Element ele = null;
		boolean fileExists = false;

		while (itr.hasNext()) {

			ele = (Element) itr.next();
			fileExists = ((ele.getChildText("FILE-EXISTS") != null)
					&& ((Integer.parseInt(ele.getChildText("FILE-EXISTS"))) > 0)) ? true : false;
		}
		return fileExists;
	}
	
}
