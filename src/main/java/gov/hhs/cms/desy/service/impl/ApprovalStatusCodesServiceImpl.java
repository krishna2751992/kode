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

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.ApprovalStatusCodesService;
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
public class ApprovalStatusCodesServiceImpl implements ApprovalStatusCodesService{

	private final Logger log = LoggerFactory.getLogger(ApprovalStatusCodesServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	/* 
	 * Retrieves the list of Denial reasons and is being used to populate the drop down on Approval screen 
	 * when user is changing the approval status for a particular request.
	 * 
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.ApprovalStatusCodes#getApprovalStatusCodes(java.lang.String)
	 */
	@Override
	public List<ApprovalDTO> getApprovalStatusCodes(String userId){
		log.info("ApprovalStatusCodesImpl :: getApprovalStatusCodes #");
		
		String reqXML = requestXml(userId);
		
		log.info("Request XML for Approval Statuse coes and descriptions :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for Approval Statuse coes and descriptions  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<ApprovalDTO> approvalDTOLst = createApprovalStatusCodesLst(xmlDocItr);
		
		
		return approvalDTOLst;		
	}
	
	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP026");
		//setting parameter S in TYPE tag
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP026_TYPE,
				  true, Types.CHAR, "S"));
   
		reqMsg.addContent(query);	
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<ApprovalDTO>  createApprovalStatusCodesLst(Iterator itr) {
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
