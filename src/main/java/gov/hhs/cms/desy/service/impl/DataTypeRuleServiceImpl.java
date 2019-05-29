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
import gov.hhs.cms.desy.service.DataTypeRuleService;
import gov.hhs.cms.desy.service.dto.DataTypeRuleDTO;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;
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
public class DataTypeRuleServiceImpl implements DataTypeRuleService {
	private final Logger log = LoggerFactory.getLogger(DataTypeRuleServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/* Retrieves data type rules based on data source and data type selected by user.
	 *
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.DataTypeRule#getDataTypeRule(int, int, java.lang.String)
	 */
	@Override
	public DataTypeRuleDTO getDataTypeRule(int dataSourceId, int dataTypeId, String userId) {
		log.info("DataTypeRuleServiceImpl :: getDataTypeRule #");
		DataTypeRuleDTO dataTypeRuleDTO = new DataTypeRuleDTO();

		String reqXML = requestXml(dataSourceId, dataTypeId, userId);

		log.info("Request XML  for Data Type Rule :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Data Type rule  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		dataTypeRuleDTO = createDataTypeRule(xmlDocItr, getOutputTypes(userId));

		return dataTypeRuleDTO;
	}

	/**
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param userId
	 * @return
	 */
	private String requestXml(int dataSourceId, int dataTypeId, String userId) {
		userId = userId.toUpperCase();
		DataTypeRuleService dataTypeRule = null;
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP008
		XMLQuery query = new XMLQuery("DSYCP008");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP008_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP008_DATATYPE_ID, true, Types.INTEGER,
				new Integer(dataTypeId).toString()));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @param outputTypeHash
	 * @return
	 * @throws Exception
	 */
	public static DataTypeRuleDTO createDataTypeRule(Iterator iter, LinkedHashMap<String, OutputTypeDTO> outputTypeHash){
		int recCount = 0;
		Element ele = null;
		DataTypeRuleDTO dataTypeRule = null;
		while (iter.hasNext()) {
			ele = (Element) iter.next();
			// DSY_Data_OBJ table in db2 always have single record of rules for a given
			// datastore and data type.
			if (recCount++ == 0) {
				dataTypeRule = new DataTypeRuleDTO();
				dataTypeRule.setDataTypeId(Integer.parseInt(ele.getChildText("OBJ-ID").trim()));

				dataTypeRule.setMustSearch(
						ele.getChildText("META-MUST-SRCH-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				dataTypeRule
						.setYearEnabled(ele.getChildText("META-YR-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				dataTypeRule.setCanSearch(
						ele.getChildText("META-CAN-SRCH-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				dataTypeRule.setSingleHICNEnabled(
						ele.getChildText("META-SNGL-HICAN-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				dataTypeRule.setZipPlus4Enabled(
						ele.getChildText("META-ZIP-PLUS4-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				dataTypeRule.setHeaderEnabled(
						ele.getChildText("META-HDR-SW").toUpperCase().charAt(0) == 'Y' ? true : false);

				// outputhash has all the four output datatypes.
				// System removes specific output type from the hash map based on data type rule
				if (ele.getChildText("META-VW-SW").toUpperCase().charAt(0) != 'Y') {
					outputTypeHash.remove(Integer.toString(2));
				}

				if (ele.getChildText("META-WHL-VW-SW").toUpperCase().charAt(0) != 'Y') {
					outputTypeHash.remove(Integer.toString(0));
				}

				if (ele.getChildText("META-FNDR-VW-SW").toUpperCase().charAt(0) != 'Y') {
					outputTypeHash.remove(Integer.toString(1));
				}

				if (ele.getChildText("META-WHL-REC-SW").toUpperCase().charAt(0) != 'Y') {
					outputTypeHash.remove(Integer.toString(-1));
				}

				dataTypeRule.setOutputTypes(outputTypeHash.values().stream().collect(Collectors.toList()));

				if (ele.getChildText("META-REC-FRMT-CD") != null
						&& ele.getChildText("META-REC-FRMT-CD").trim().length() > 0)
					dataTypeRule.setRecFormat(ele.getChildText("META-REC-FRMT-CD"));

				if (ele.getChildText("META-MAX-GRP-ID") != null
						&& ele.getChildText("META-MAX-GRP-ID").trim().length() > 0)
					dataTypeRule.setMaxConditionSet(Integer.parseInt(ele.getChildText("META-MAX-GRP-ID")));

			}
		}
		return dataTypeRule;
	}

	/*
	 * retrieves list of output types available from the database and is called by getdataTyperules method and
     * only values based on data type rules are being sent to front end.
     *
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.web.rest.OutputTypes#getOutputTypes(java.lang.String)
	 */

	private LinkedHashMap<String, OutputTypeDTO> getOutputTypes(String userId) {

		String reqXML = outputTypeRequestXml(userId);
		log.info("Request XML for OutputTypes :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for OutputTypes  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		LinkedHashMap<String, OutputTypeDTO>  outputTypeHash = createOutputTypes(xmlDocItr);

		return outputTypeHash;
	}

	/**
	 * @param userId
	 * @return
	 */
	private String outputTypeRequestXml(String userId) {
		userId=userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		//calling CICS DSYCP028
		XMLQuery query = new XMLQuery("DSYCP028");
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private LinkedHashMap<String, OutputTypeDTO>  createOutputTypes(Iterator iter) {
		List<OutputTypeDTO>  outputTypeDTOLst =  new ArrayList<OutputTypeDTO>();

		Element ele = null;
		OutputTypeDTO outputTypeDTO;
		LinkedHashMap<String, OutputTypeDTO> outputTypeHash = new LinkedHashMap<String, OutputTypeDTO>();
		while (iter.hasNext())
		{
			ele = (Element) iter.next();
			outputTypeDTO = (OutputTypeDTO) outputTypeHash.get(ele.getChildText("RQST-VW-ID"));
			if (outputTypeDTO == null)
			{
				outputTypeDTO = new OutputTypeDTO();
                //	retrieving output type ID
				outputTypeDTO.setViewID(ele.getChildText("RQST-VW-ID").trim());
				// retrieving output description
				if (ele.getChildText("RQST-OTPT-DESC") != null
							&& ele.getChildText("RQST-OTPT-DESC").trim().length() > 0)
					outputTypeDTO.setDescription(ele.getChildText("RQST-OTPT-DESC"));

				outputTypeHash.put(ele.getChildText("RQST-VW-ID"), outputTypeDTO);
			}

		}

		return outputTypeHash;
	}

}
