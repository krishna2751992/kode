/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.DenialReasonCodesService;
import gov.hhs.cms.desy.service.dto.ApprovalDTO;
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
public class DenialReasonCodesServiceImpl implements DenialReasonCodesService{

	private final Logger log = LoggerFactory.getLogger(DenialReasonCodesServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	@Override
	public List<ApprovalDTO> getDenialReasonCodes(String userId){
		log.info("DenialReasonCodesImpl :: getDenialReasonCodes #");
		
		String reqXML = requestXml(userId);
		
		log.info("Request XML for Denial Reasons :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Denial Reasons  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<ApprovalDTO> approvalDTOLst = createDenialReasonCodesLst(xmlDocItr);
		
		return approvalDTOLst;		
	}
	
	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
			//calling CICS DSYCP026 
			XMLQuery query = new XMLQuery("DSYCP026");
	    //setting parameter D in TYPE tag
	    query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP026_TYPE,
				  true, Types.CHAR, "D"));
       
        reqMsg.addContent(query);	
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<ApprovalDTO>  createDenialReasonCodesLst(Iterator itr) {
		List<ApprovalDTO> approvalDTOLst= new ArrayList<ApprovalDTO>();
		Element ele = null;
		ApprovalDTO approvalDTO;
		while (itr.hasNext())
		{
		  ele = (Element) itr.next();
		  approvalDTO = new ApprovalDTO();
		  approvalDTO.setStatusCode(Integer.parseInt(ele.getChildText("APRVL-STUS")));
		  approvalDTO.setDescription(ele.getChildText("APRVL-DCRN"));
		  approvalDTOLst.add(approvalDTO);
		}
		
		return approvalDTOLst;
	}	

}
