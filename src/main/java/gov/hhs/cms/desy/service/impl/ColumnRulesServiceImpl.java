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
import gov.hhs.cms.desy.service.ColumnRulesService;
import gov.hhs.cms.desy.service.dto.ColumnRulesDTO;
import gov.hhs.cms.desy.service.dto.LookupDTO;
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
public class ColumnRulesServiceImpl implements ColumnRulesService {
	private final Logger log = LoggerFactory.getLogger(ColumnRulesServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves element information based on field selected by user on search
	 * criteria screen.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.ColumnRules#getColumnRules(int, int, int,
	 * java.lang.String)
	 */
	@Override
	public ColumnRulesDTO getColumnRules(int dataSourceId, int dataTypeId, int columnId, String userId) {
		ColumnRulesDTO columnRulesDTO = new ColumnRulesDTO();

		String reqXML = requestXml(dataSourceId, dataTypeId, columnId, userId);
		
		log.info("Request XML for Column Rules :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Column Rules  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		columnRulesDTO = createColumnRules(xmlDocItr);

		int valType = columnRulesDTO.getValType();
		log.info("Val Type  :" + valType);
		valType =1;
		if (valType == 1) {
			// calling getLookupValues method to retrieve the list of Lookup objects
			String lookupReqXML = requestXmlForLookup(columnId, userId);
			
			log.info("Request XML for Lookup  :" + reqXML);

			iibResponse = dsyHttpAsync.getResponseFromIIB(lookupReqXML);
			
			log.info("Response XML for Lookup  :" + reqXML);

			xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

			xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

			List<LookupDTO> lookups = createLookups(xmlDocItr);

			// saving lookups list with column rules object
			columnRulesDTO.setLookups(lookups);
		}

		return columnRulesDTO;
	}

	/**
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param columnId
	 * @param userId
	 * @return
	 */
	private String requestXml(int dataSourceId, int dataTypeId, int columnId, String userId) {
		userId = userId.toUpperCase();
		ColumnRulesService columnRules = null;
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP007
		XMLQuery query = new XMLQuery("DSYCP007");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP007_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP007_DATATYPE_ID, true, Types.INTEGER,
				new Integer(dataTypeId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP007_ELE_ID, true, Types.INTEGER,
				new Integer(columnId).toString()));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}


	/**
	 * @param iter
	 * @return
	 */
	public static ColumnRulesDTO createColumnRules(Iterator iter) {
		int recCount = 0;
		Element ele = null;
		ColumnRulesDTO columnRulesDTO = null;
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			// there is always sigle record for a partucular element ID
			if (recCount++ == 0) {
				columnRulesDTO = new ColumnRulesDTO();
				columnRulesDTO.setColumnId(Integer.parseInt(ele.getChildText("ELE-ID").trim()));
				// Nullable
				if (ele.getChildText("ELE-NM") != null && ele.getChildText("ELE-NM").trim().length() > 0) {
					columnRulesDTO.setColumnName(DBStringConverter.checkDBCharNull(ele.getChildText("ELE-NM").trim()));
				}

				// Nullable
				if (ele.getChildText("OPER-LIST") != null && ele.getChildText("OPER-LIST").trim().length() > 0) {
					columnRulesDTO
							.setOperators(DBStringConverter.checkDBCharNull(ele.getChildText("OPER-LIST").trim()));
				}
				// Nullable
				if (ele.getChildText("ELE-LGTH") != null && ele.getChildText("ELE-LGTH").trim().length() > 0) {
					String eleLength = DBStringConverter.checkDBIntegerNull(ele.getChildText("ELE-LGTH").trim());
					if (eleLength != null) {
						columnRulesDTO.setLength(Integer.parseInt(eleLength));
					}
				}

				// Nullable
				if (ele.getChildText("VAL-TYPE") != null && ele.getChildText("VAL-TYPE").trim().length() > 0) {
					String valType = DBStringConverter.checkDBIntegerNull(ele.getChildText("VAL-TYPE").trim());
					if (valType != null) {
						columnRulesDTO.setValType(Integer.parseInt(valType));
					}
				}

				// Nullable
				if (ele.getChildText("ICD-IND-REQD") != null && ele.getChildText("ICD-IND-REQD").trim().length() > 0) {
					if ((ele.getChildText("ICD-IND-REQD").trim()).toUpperCase().charAt(0) == 'Y') {
						columnRulesDTO.setEnableIcd(
								DBStringConverter.checkDBCharNull(ele.getChildText("ICD-IND-REQD").trim()));
					}
				}

				boolean enableHICAN = false;
				if (ele.getChildText("META-SNGL-HICAN-SW") != null
						&& ele.getChildText("META-SNGL-HICAN-SW").trim().length() > 0) {
					if ((ele.getChildText("META-SNGL-HICAN-SW").trim()).toUpperCase().charAt(0) == 'Y') {
						enableHICAN = true;
					} else {
						enableHICAN = false;
					}
					columnRulesDTO.setEnableHICN(enableHICAN);
				}
				// Nullable
				if (ele.getChildText("WHR-CLS") != null && ele.getChildText("WHR-CLS").trim().length() > 0) {
					String whereClause = DBStringConverter.checkDBCharNull(ele.getChildText("WHR-CLS").trim());
					columnRulesDTO.setWhereClause(whereClause);
					if (whereClause.equalsIgnoreCase("N")) {
						columnRulesDTO.setSearchable(false);
					} else {
						columnRulesDTO.setSearchable(true);
					}

					if (whereClause.toUpperCase().startsWith("F") || whereClause.toUpperCase().startsWith("H")) {
						columnRulesDTO.setEnableFinder(true);
						columnRulesDTO.setSelectFinder(true);
					} else if (whereClause.toUpperCase().startsWith("Y,F")) {
						columnRulesDTO.setEnableFinder(true);
						columnRulesDTO.setSelectFinder(false);
					} else if (whereClause.toUpperCase().startsWith("Y,H")) {
						columnRulesDTO.setEnableFinder(true);
						if (enableHICAN) {
							columnRulesDTO.setSelectFinder(false);
						} else {
							columnRulesDTO.setSelectFinder(true);
						}

					} else {
						columnRulesDTO.setEnableFinder(false);
						columnRulesDTO.setSelectFinder(false);
					}

					// Checking Header Start Position - true only if WHR_CLS in DSY_ELEMENT table
					// like'%F,P%' or like '%H,P%'
					if (whereClause.toUpperCase().indexOf("F,P") > 0 || whereClause.toUpperCase().indexOf("H,P") > 0) {
						columnRulesDTO.setHeaderStartPositionEnabled(true);
					} else {
						columnRulesDTO.setHeaderStartPositionEnabled(false);
					}

				}
			}
		}
		return columnRulesDTO;
	}

	/**
	 * @param columnId
	 * @param userId
	 * @return
	 */
	private String requestXmlForLookup(int columnId, String userId) {

		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId.toUpperCase(), Db2Crud.READ);
		// calling CICS DSYCP036
		XMLQuery query = new XMLQuery("DSYCP036");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP036_ELE_ID, true, Types.INTEGER,
				new Integer(columnId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP036_REQ_DATA, true, Types.CHAR, "   "));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<LookupDTO> createLookups(Iterator iter) {
		List<LookupDTO> lookupList = new ArrayList<LookupDTO>();
		LookupDTO lookup = null;
		Element ele = null;
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			lookup = new LookupDTO();
			String lookupID = null;
			String lookupValue = null;
			String lookupText = null;

			if (ele.getChildText("SEQUENSE") != null && ele.getChildText("SEQUENSE").trim().length() > 0) {
				lookupID = ele.getChildText("SEQUENSE").trim();
				lookup.setLookupID(Integer.parseInt(lookupID));
			}
			if (ele.getChildText("LOOKUP-VAL") != null && ele.getChildText("LOOKUP-VAL").trim().length() > 0) {
				lookupValue = ele.getChildText("LOOKUP-VAL").trim();
				lookup.setLookupValue(lookupValue);
			}

			if (ele.getChildText("LOOKUP-TEXT") != null && ele.getChildText("LOOKUP-TEXT").trim().length() > 0) {
				lookupText = ele.getChildText("LOOKUP-TEXT").trim();
				lookup.setLookupText(lookupText);
			}
			if ((lookupID != null && lookupID.length() > 0) && (lookupValue != null && lookupValue.length() > 0))

				lookup.setLookupIDValue(lookupID + "~" + lookupValue);
			lookupList.add(lookup);
		}

		return lookupList;
	}
}
