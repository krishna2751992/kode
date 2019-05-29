/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
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
import org.springframework.beans.factory.annotation.Value;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.RecipientsService;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
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
public class RecipientsServiceImpl implements RecipientsService{
	private final Logger log = LoggerFactory.getLogger(RecipientsServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;

	@Value("${desyiib}")
	private String iiburl;
	
	/* 
	 * retrives list of recipients based on dau number and user id and is 
     * is being called by getDua method.
     * 
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.Recipients#getRecipients(int, java.lang.String)
	 */
	@Override
	public List<RecipientDTO> getRecipients(int duaNum,String userId) {
		log.info("RecipientsImpl :: getRecipients #");
		String reqXML = requestXml(duaNum,userId);
		log.info("Request XML for Recipients :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Recipients  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<RecipientDTO>  recipientDTOLst = createRecipients(xmlDocItr);
		
		return recipientDTOLst;
	}
	
	/**
	 * @param duaNum
	 * @param userId
	 * @return
	 */
	private String requestXml(int duaNum,String userId) {
	
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP031
		XMLQuery query = new XMLQuery("DSYCP031");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP031_DUA_NUM,
			true, Types.INTEGER, new Integer(duaNum).toString()));
		reqMsg.addEvent(query);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param iter
	 * @return
	 */
	private List<RecipientDTO>  createRecipients(Iterator iter){
	
		RecipientDTO recip = null;
		Element ele = null;
		LinkedHashMap<String, RecipientDTO> recipHash = new LinkedHashMap<String, RecipientDTO>();

		while (iter.hasNext())
		{
				ele = (Element) iter.next();
			    recip = (RecipientDTO) recipHash.get(ele.getChildText("ADR-ID"));
				if (recip == null)
				{
					recip = new RecipientDTO();
					recip.setRecipID(Integer.parseInt(ele.getChildText("ADR-ID").trim()));
					if (ele.getChildText("ADR-CNTCT-NAME") != null
											&& ele.getChildText("ADR-CNTCT-NAME").trim().length() > 0)
					recip.setName(ele.getChildText("ADR-CNTCT-NAME"));
					recip.setEmail(ele.getChildText("ADR-EMAIL-NAME"));
				
					recipHash.put(ele.getChildText("ADR-ID"), recip);
				}				
		 }
		
		return recipHash.values().stream().collect(Collectors.toList());
	}	
	
}
