/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
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
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.DataSourceService;
import gov.hhs.cms.desy.service.DuaInfoService;
import gov.hhs.cms.desy.service.RecipientsService;
import gov.hhs.cms.desy.service.dto.DataSourceDTO;
import gov.hhs.cms.desy.service.dto.DuaDTO;
import gov.hhs.cms.desy.service.dto.DuaInfoDTO;
import gov.hhs.cms.desy.service.dto.RecipientDTO;
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
public class DuaInfoServiceImpl implements DuaInfoService{

	private final Logger log = LoggerFactory.getLogger(DuaInfoServiceImpl.class);

	@Value("${desyiib}")
	private String iiburl;
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;

	@Inject
	private DataSourceService dataSource;
	
	@Inject
	private RecipientsService recipients;
	
	@Override
	public DuaInfoDTO getDua(int duaNum,String userId) {
		log.info("DuaInfoServiceImpl :: getDua #");
		DuaInfoDTO duaInfoDTO = new DuaInfoDTO();
		
		log.info("IIB URL :" + iiburl);

		String reqXML = getDuaRequestXml(duaNum, userId);
		log.info("Request XML  for DUA details :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for DUA deatils  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		DuaDTO duaDTO = getDuaDTO(xmlDocItr);		
		
		List<DataSourceDTO> dataSourceDTOLst = dataSource.getDataSources(duaNum, userId);
		
		List<RecipientDTO> recipientDTOLst = recipients.getRecipients(duaNum, userId);
		
		duaInfoDTO.setDuaDTO(duaDTO);
		duaInfoDTO.setDataSourceDTOLst(dataSourceDTOLst);
		duaInfoDTO.setRecipientsDTOLst(recipientDTOLst);
		
		return duaInfoDTO;
	}
	
	/**
	 * @param duaNum
	 * @param userId
	 * @return
	 */
	private String getDuaRequestXml(int duaNum,String userId) {
		
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
        // calling CICS DSYCP037	
		XMLQuery query = new XMLQuery("DSYCP037");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP037_DUA_NUM,
			true, Types.INTEGER, new Integer(duaNum).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP037_USER_ID,
						true, Types.CHAR, userId));	
		reqMsg.addEvent(query);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private DuaDTO getDuaDTO(Iterator iter) {
		
		int recCount = 0;
		Element ele = null;
		DuaDTO dua = null;
		while (iter.hasNext())
		{
			ele = (Element) iter.next();
			if (recCount++ == 0)
			{
				dua = new DuaDTO();
				dua.setDuaNumber(Integer.parseInt(ele.getChildText("DUA-NUM").trim()));
				
				dua.setStudyName(ele.getChildText("STDY-NAME"));
				
				
				dua.setExpirationDate(DateFunctions.makeDate(ele.getChildText("EXPRTN-DT")));	
		        //nullbale		
				dua.setRequestor(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-CNTCT-NAME")));
				
				dua.setReturnRequired(ele.getChildText("RETN-REQD").charAt(0) == 'Y'
										? true : false);
				
				dua.setEncryptionSwitch(ele.getChildText("ENCRPT-CD").charAt(0));			
				
				dua.setFtapeSwitch(ele.getChildText("FRGN-TAPE-SW").charAt(0));
			    //Nullable	
				String desyExpirationDate = DBStringConverter.checkDBCharNull(ele.getChildText("DESY-EXPRTN-DT").trim());
				if (desyExpirationDate != null && desyExpirationDate.length()>0)			
				{
					dua.setDesyExpirationDate(DateFunctions.makeDate(desyExpirationDate));
				} 
			}
		}
		
		if (dua == null)
			throw new SystemException("Could not retrieve dua from db and DUA null or empty!");
		
		return dua;
		
	}
	

}
