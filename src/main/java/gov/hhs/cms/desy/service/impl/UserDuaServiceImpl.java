/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.service.UserDuaService;
import gov.hhs.cms.desy.service.dto.DuaDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
import gov.hhs.cms.desy.service.util.DateFunctions;
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
public class UserDuaServiceImpl extends HelperService implements UserDuaService {
	private final Logger log = LoggerFactory.getLogger(UserDuaServiceImpl.class);


	/*  
	 * Retrieves DAU info based on User ID. 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.UserDua#getUserDuaList(java.lang.String)
	 */
	@Override
	public List<DuaDTO> getUserDuaList() {
		log.info("UserDuaImpl :: getUserDuaList #");

		String reqXML = requestXml(this.getCurrentUserId());
		log.info("Request XML  for user assinged DUA details, {}:", reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for user assigned DUA deatils, {}:", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<DuaDTO> duaDTOLst = getAllDuaDetail(xmlDocItr);

		return CollectionUtils.isEmpty(duaDTOLst) ? Collections.emptyList() : duaDTOLst;
	}

	/**
	 * @param userID
	 * @return
	 */
	private String requestXml(String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling the CICS DSYCP001
		XMLQuery query = new XMLQuery("DSYCP001");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP001_USER_ID, true, Types.CHAR, userId));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 */
	public List<DuaDTO> getAllDuaDetail(Iterator iter) {
		
		if(iter == null) {
			return Collections.emptyList();
		}

		DuaDTO duaDTO = null;
		Element ele = null;
		HashMap<String, DuaDTO> headerHash = new HashMap<String, DuaDTO>();
		List<DuaDTO> duaList = new ArrayList<DuaDTO>();

		while (iter.hasNext()) {
			ele = (Element) iter.next();

			if (headerHash.get(String.valueOf(ele.getChildText("DUA-NUM").trim())) == null) {
				duaDTO = new DuaDTO();
				duaDTO.setDuaNumber(Integer.parseInt(ele.getChildText("DUA-NUM").trim()));

				duaDTO.setStudyName(ele.getChildText("STDY-NAME"));

				duaDTO.setExpirationDate(DateFunctions.makeDate(ele.getChildText("EXPRTN-DT")));
				// nullable
				duaDTO.setRequestor(DBStringConverter.checkDBCharNull(ele.getChildText("ADR-CNTCT-NAME").trim()));

				duaDTO.setReturnRequired(ele.getChildText("RETN-REQD").charAt(0) == 'Y' ? true : false);

				duaDTO.setEncryptionSwitch(ele.getChildText("ENCRPT-CD").charAt(0));

				duaDTO.setFtapeSwitch(ele.getChildText("FRGN-TAPE-SW").charAt(0));
				// Nullable
				String desyExpirationDate = DBStringConverter
						.checkDBCharNull(ele.getChildText("DESY-EXPRTN-DT").trim());
				if (desyExpirationDate != null && desyExpirationDate.length() > 0) {
					duaDTO.setDesyExpirationDate(DateFunctions.makeDate(ele.getChildText("DESY-EXPRTN-DT")));
				}

				log.info("dua DTO :" + duaDTO);
				headerHash.put(String.valueOf(ele.getChildText("DUA-NUM")).trim(), duaDTO);
			}

		}

		duaList = headerHash.values().stream().collect(Collectors.toList());
		return duaList;

	}
	
}
