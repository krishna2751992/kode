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
import gov.hhs.cms.desy.service.DataTypeService;
import gov.hhs.cms.desy.service.dto.DataTypeDTO;
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
public class DataTypeServiceImpl implements DataTypeService {
	private final Logger log = LoggerFactory.getLogger(DataTypeServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/* 
	 * Retrieves list of data types based on DAU and data source selected by user
	 * and is being called by getDataSorce method.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.DataType#getDataTypes(int, char, int,
	 * java.lang.String)
	 */
	@Override
	public List<DataTypeDTO> getDataTypes(int duaNum, char encryptionSwitch, int dataSourceId, String userId){
		log.info("DataTypeServiceImpl :: getDataTypes #");
		List<DataTypeDTO> dataTypeDTOLst = new ArrayList<DataTypeDTO>();

		String reqXML = duaRequestXml(duaNum, encryptionSwitch, dataSourceId, userId);
		
		log.info("Request XML for Data Type :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Data Type :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		dataTypeDTOLst = createDataTypes(xmlDocItr);

		return dataTypeDTOLst;
	}

	/**
	 * @param duaNum
	 * @param encryptionSwitch
	 * @param dataSourceId
	 * @param userId
	 * @return
	 */
	private String duaRequestXml(int duaNum, char encryptionSwitch, int dataSourceId, String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP003
		XMLQuery query = new XMLQuery("DSYCP003");
		query.addParmName(
				new XMLReqParm(ReqDBTableParams.DSYCP003_DUA_NUM, true, Types.INTEGER, new Integer(duaNum).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP003_DATASOURCE_ID, true, Types.INTEGER,
				new Integer(dataSourceId).toString()));

		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 */
	private List<DataTypeDTO> createDataTypes(Iterator iter) {
		DataTypeDTO dataTypeDTO = null;
		Element ele = null;
		// defining hash map so that duplicate enteries should not be there in the array
		// Using LinkedHash map instead of hashMap as LinkhaspMap does not re-arrange
		// the list as compared to hash map.
		LinkedHashMap<String, DataTypeDTO> dataHash = new LinkedHashMap<String, DataTypeDTO>();

		while (iter.hasNext()) {
			ele = (Element) iter.next();
			dataTypeDTO = (DataTypeDTO) dataHash.get(ele.getChildText("OBJ-ID"));
			if (dataTypeDTO == null) {
				dataTypeDTO = new DataTypeDTO();
				dataTypeDTO.setDataTypeID(Integer.parseInt(ele.getChildText("OBJ-ID").trim()));

				dataTypeDTO.setName(ele.getChildText("OBJ-NM").trim());

				dataHash.put(ele.getChildText("OBJ-ID"), dataTypeDTO);
			}
		}

		// Converting hash map into an array and returning back to calling program.
		return dataHash.values().stream().collect(Collectors.toList());

	}

	
}
