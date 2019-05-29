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
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.ResendEncryptEmailService;
import gov.hhs.cms.desy.service.dto.EncryptionSoftwareTypeDTO;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
import gov.hhs.cms.desy.service.dto.ResendEncryptEmailDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
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
public class ResendEncryptEmailServiceImpl implements ResendEncryptEmailService{

	private final Logger log = LoggerFactory.getLogger(ResendEncryptEmailServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.ResendEncryptEmail#getResendEncryptEmail(int, java.lang.String)
	 */
	@Override
	public ResendEncryptEmailDTO getResendEncryptEmail(int requestId, String userId) {
		log.info("ResendEncryptEmailImpl :: getResendEncryptEmail");

		String reqXML = requestXml(requestId,userId);

		log.info("Request XML for getResendEncryptEmail :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for get getAllNews  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		ResendEncryptEmailDTO resendEncryptEmailDTO = createEncryptionRecipInfo(requestId, xmlDocItr);
		
		return resendEncryptEmailDTO;
	}
	
	
	/**
	 * @param requestID
	 * @param userId
	 * @return
	 */
	private String requestXml(int requestID, String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// Calling CICS DSYCP056
		XMLQuery query = new XMLQuery("DSYCP056");
	   
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP056_REQ_ID,
			   true, Types.INTEGER, requestID > 0 ? new Integer(requestID).toString() : "-1"));

		reqMsg.addEvent(query);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param requestId
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private ResendEncryptEmailDTO createEncryptionRecipInfo(int requestId, Iterator iter) {
	
		ResendEncryptEmailDTO resendEncryptEmail = new ResendEncryptEmailDTO();
		
		Element ele = null;
		while (iter.hasNext())
		{
			ele = (Element) iter.next();

			//************************* Recipient *****************//
			RecipientDTO recip= new RecipientDTO();

			//Nullable
			if(ele.getChildText("ADR-ID")!=null && ele.getChildText("ADR-ID").trim().length()>0)
			{
				String recipID=DBStringConverter.checkDBIntegerNull(ele.getChildText("ADR-ID").trim());
				if(recipID != null && recipID.length()>0)
				{
					  recip.setRecipID(Integer.parseInt(recipID));
				} else {
					recip.setRecipID(0);	
				}
			} else {
				recip.setRecipID(0);	
			}

			if(ele.getChildText("ADR-CNTCT-NAME")!=null && ele.getChildText("ADR-CNTCT-NAME").trim().length()>0)
			{
				recip.setName(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-CNTCT-NAME").trim()));
			} else {
				recip.setName("");
			}
			
			if(ele.getChildText("ADR-EMAIL-NAME")!=null && ele.getChildText("ADR-EMAIL-NAME").trim().length()>0)
			{
				recip.setEmail(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-EMAIL-NAME").trim()));
			} else {
				recip.setEmail("");
			}

			resendEncryptEmail.setRecipient(recip);

		    //************************* Encryption Software Type **************************//
		    EncryptionSoftwareTypeDTO encryptST = new EncryptionSoftwareTypeDTO();

		  	if (ele.getChildText("ENCRPT-SFTWR-TYP") != null) {
	
			  String encryptSoftwareType=DBStringConverter.checkDBIntegerNull(ele.getChildText("ENCRPT-SFTWR-TYP").trim());
			  if(encryptSoftwareType != null && encryptSoftwareType.length()>0)
			  {
				  encryptST.setEncryptSoftwareType(Integer.parseInt(encryptSoftwareType));
			  } else {
				  encryptST.setEncryptSoftwareType(0);
			  }
			} else {
			  encryptST.setEncryptSoftwareType(0);
			}
					
			if (ele.getChildText("ENCRPT-SFTWR-DESC") != null) {
		
			  if(ele.getChildText("ENCRPT-SFTWR-DESC")!=null && ele.getChildText("ENCRPT-SFTWR-DESC").trim().length()>0)
			  {
				  encryptST.setDescription(DBStringConverter.checkDBCharNull(ele.getChildText("ENCRPT-SFTWR-DESC").trim()));
			  } else {
				  encryptST.setDescription("");
			  }
			} else {
			  encryptST.setDescription("");
			}

			resendEncryptEmail.setEncryptST(encryptST);
	
		} // end-while
		//if the object is null --> throw error
		if (resendEncryptEmail != null && resendEncryptEmail.getRecipient() !=null) 
		{
			resendEncryptEmail.setRequestID(requestId);	
		} else {
			throw new SystemException("Could not retrieve request from db");
		}
		
		return resendEncryptEmail;
	}
}
