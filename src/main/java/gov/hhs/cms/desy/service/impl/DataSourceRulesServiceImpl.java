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
import gov.hhs.cms.desy.service.DataSourceRulesService;
import gov.hhs.cms.desy.service.dto.DataSourceRulesDTO;
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
public class DataSourceRulesServiceImpl implements DataSourceRulesService {
	private final Logger log = LoggerFactory.getLogger(DataSourceRulesServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves data source rules based on data source selected by user.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.DataSourceRules#getDataSourceRules(int,
	 * java.lang.String)
	 */
	@Override
	public DataSourceRulesDTO getDataSourceRules(int dataSourceId, String userId) {
		log.info("DataSourceRulesImpl : getDataSourceRules #");
		
		String reqXML = requestXml(dataSourceId, userId);
		
		log.info("Request XML  for Data source Rules :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML  for Data source Rules   :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		DataSourceRulesDTO dataSourceRulesDTO = createDataSourceRules(xmlDocItr);

		return dataSourceRulesDTO;
	}

	/**
	 * @param dataSourceId
	 * @param userId
	 * @return
	 */
	private String requestXml(int dataSourceId, String userId) {
		userId = userId.toUpperCase();
		DataSourceRulesService dataSourceRules = null;
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP009
		XMLQuery query = new XMLQuery("DSYCP009");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP009_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 */
	public DataSourceRulesDTO createDataSourceRules(Iterator iter) {
		int recCount = 0;
		Element ele = null;
		DataSourceRulesDTO dataSourceRules = null;

		// There is always a single record as DSY_DATASTORE table has sigle record for a
		// given combination of data source and data type.
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			dataSourceRules = new DataSourceRulesDTO();
			// sets Select state swich to datastore object
			String stateSwitch = ele.getChildText("META-SLCT-STATE-SW");
			String dataStoreId = ele.getChildText("DATASTORE-ID");
			log.info("stateSwitch :" + stateSwitch);

			if (stateSwitch != null) {
				dataSourceRules.setSelectState(stateSwitch.endsWith("Y") ? true : false);
			}
			if (dataStoreId != null) {
				dataSourceRules.setDataSourceId(Integer.parseInt(dataStoreId));
			}

		}

		return dataSourceRules;
	}

	
}
