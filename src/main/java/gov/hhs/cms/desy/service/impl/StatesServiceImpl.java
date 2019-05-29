/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.sql.Types;
import java.util.ArrayList;
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

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.StatesService;
import gov.hhs.cms.desy.service.dto.StatesDTO;
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
public class StatesServiceImpl implements StatesService {

	private final Logger log = LoggerFactory.getLogger(StatesServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves list of states based on DUA and Data source selected by the users.
	 * Also it is being called by getDataSorce method.
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.States#getStates(int, int, java.lang.String)
	 */
	public List<StatesDTO> getStates(int duaNum, int dataSourceId, String userId) {
		List<StatesDTO> statesDTOLst = new ArrayList<StatesDTO>();

		String reqXML = requestXml(duaNum, dataSourceId, userId);
		log.info("Request XML for state values :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for State values  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		statesDTOLst = createStates(xmlDocItr);

		return statesDTOLst;
	}

	/**
	 * 
	 * @param duaNum
	 * @param dataSourceID
	 * @param userId
	 * @return
	 */
	private String requestXml(int duaNum, int dataSourceID, String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP004
		XMLQuery query = new XMLQuery("DSYCP004");
		query.addParmName(
				new XMLReqParm(ReqDBTableParams.DSYCP004_DUA_NUM, true, Types.INTEGER, new Integer(duaNum).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP004_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceID).toString()));

		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 */
	public List<StatesDTO> createStates(Iterator iter) {
		StatesDTO state = null;
		Element ele = null;
		// defining hash map so that duplicate enteries should not be there in the array
		// using LinkedHash map instead of hashMap as LinkhaspMap does not re-arrange
		// the list as compared to hash map.

		LinkedHashMap<String, StatesDTO> stateHash = new LinkedHashMap<String, StatesDTO>();

		while (iter.hasNext()) {
			ele = (Element) iter.next();
			state = (StatesDTO) stateHash.get(ele.getChildText("STATE-SEQ-CD"));
			if (state == null) {
				state = new StatesDTO();
				state.setStateCode(ele.getChildText("STATE-SEQ-CD").trim());
				state.setDescription(ele.getChildText("NCH-STATE-DESC").trim());

				stateHash.put(ele.getChildText("STATE-SEQ-CD"), state);
			}
		}

		return stateHash.values().stream().collect(Collectors.toList());
	}	
	
}
