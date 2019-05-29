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
import gov.hhs.cms.desy.service.DataSourceYearsService;
import gov.hhs.cms.desy.service.dto.DataSourceYearsDTO;
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
public class DataSourceYearsServiceImpl implements DataSourceYearsService {
	private final Logger log = LoggerFactory.getLogger(DataSourceYearsServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves list of years based on DUA, data source, data type and state
	 * selected by user and is being called by getDataType method.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.DataSourceYears#getDataSourceYears(int, int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<DataSourceYearsDTO> getDataSourceYears(int duaNum, int dataSourceId, int dataTypeId, String stateCode,
			String userId) {
		log.info("DataSourceYearsImpl : getDataSourceYears #");
		List<DataSourceYearsDTO> dataSourceYearsDTOLst = new ArrayList<DataSourceYearsDTO>();

		String reqXML = requestXml(duaNum, dataSourceId, dataTypeId, stateCode, userId);
		
		log.info("Request XML for Data source Years :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Data source Years :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		dataSourceYearsDTOLst = createDataSourceYears(xmlDocItr);

		return dataSourceYearsDTOLst;
	}


	/**
	  * Retrieves list of years based on DUA, data source, and data type 
	 * selected by user and is being called by getDataType method.
	 */
	@Override
	public List<DataSourceYearsDTO> getDataSourceYearsWithNoStateCode(int duaNum, int dataSourceId, int dataTypeId, String userId) {
		log.info("DataSourceYearsImpl : getDataSourceYears #");
		List<DataSourceYearsDTO> dataSourceYearsDTOLst = new ArrayList<DataSourceYearsDTO>();

		String reqXML = requestXmlWithoutStateCode(duaNum, dataSourceId, dataTypeId, userId);
		
		log.info("Request XML for Data source Years :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Data source Years :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		dataSourceYearsDTOLst = createDataSourceYears(xmlDocItr);

		return dataSourceYearsDTOLst;
	}

	/**
	 * @param duaNum
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param stateCode
	 * @param userId
	 * @return
	 */
	private String requestXml(int duaNum, int dataSourceId, int dataTypeId, String stateCode, String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP005
		XMLQuery query = new XMLQuery("DSYCP005");
		query.addParmName(
				new XMLReqParm(ReqDBTableParams.DSYCP005_DUA_NUM, true, Types.INTEGER, new Integer(duaNum).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_DATATYPE_ID, true, Types.INTEGER,
				new Integer(dataTypeId).toString()));

		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_STATE_CD, true, Types.CHAR, stateCode));

		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param duaNum
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param userId
	 * @return
	 */
	private String requestXmlWithoutStateCode(int duaNum, int dataSourceId, int dataTypeId, String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP005
		XMLQuery query = new XMLQuery("DSYCP005");
		query.addParmName(
				new XMLReqParm(ReqDBTableParams.DSYCP005_DUA_NUM, true, Types.INTEGER, new Integer(duaNum).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_DATATYPE_ID, true, Types.INTEGER,
				new Integer(dataTypeId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP005_STATE_CD, true, Types.CHAR, ""));
		
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<DataSourceYearsDTO> createDataSourceYears(Iterator iter) {
		DataSourceYearsDTO dataSourceYearsDTO = null;
		Element ele = null;
		LinkedHashMap<String, DataSourceYearsDTO> yearHash = new LinkedHashMap<String, DataSourceYearsDTO>();
		int updateMonth = 0;
		int year = 0;

		while (iter.hasNext()) {
			ele = (Element) iter.next();
			dataSourceYearsDTO = (DataSourceYearsDTO) yearHash.get(ele.getChildText("DATA-YEAR"));
			if (dataSourceYearsDTO == null) {
				dataSourceYearsDTO = new DataSourceYearsDTO();
				year = Integer.parseInt(ele.getChildText("DATA-YEAR").trim());
				if (ele.getChildText("FIL-UPD-MONTH") != null && ele.getChildText("FIL-UPD-MONTH").trim().length() > 0)
					updateMonth = Integer.parseInt(ele.getChildText("FIL-UPD-MONTH"));
				String monthYear = getMonthYear(year, updateMonth);
				dataSourceYearsDTO.setYear(year);
				dataSourceYearsDTO.setUpdateDate(monthYear);

				yearHash.put(ele.getChildText("DATA-YEAR"), dataSourceYearsDTO);
			}

		}

		return yearHash.values().stream().collect(Collectors.toList());

	}

	/**
	 * Converts Month/year into Update Month/Years
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 */
	private String getMonthYear(int year, int month)  {
		String monthYear = "";

		if (year > 0 && month > 0) {
			int newYear = year + (month - 1) / 12;
			int newMonth = month % 12;
			if (newMonth == 0)
				newMonth = 12;
			monthYear = ((newMonth < 10) ? "0" : "") + Integer.toString(newMonth) + "/" + Integer.toString(newYear);
		}

		return monthYear;
	}

}
