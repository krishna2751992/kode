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
import gov.hhs.cms.desy.service.DataSourceService;
import gov.hhs.cms.desy.service.dto.DataSourceDTO;
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
public class DataSourceServiceImpl implements DataSourceService {

	private final Logger log = LoggerFactory.getLogger(DataSourceServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;



	/*
	 * Retrieves list of data sources based on DUA number and user ID
	 * Also it is being called by getDuaInfo method.
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.DataSource#getDataSources(int,
	 * java.lang.String)
	 */
	public List<DataSourceDTO> getDataSources(int duaNum, String userId) {
		log.info("DataSourceServiceImpl :: getDataSources #");
		List<DataSourceDTO> dataSourceDTOLst = new ArrayList<DataSourceDTO>();

		String reqXML = requestXml(duaNum, userId);
		
		log.info("Request XML for Data sources :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Data sources  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		dataSourceDTOLst = createDataSources(xmlDocItr);

		return dataSourceDTOLst;
	}

	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(int duaNum, String userId) {

		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP002 program
		XMLQuery query = new XMLQuery("DSYCP002");
		query.addParmName(
				new XMLReqParm(ReqDBTableParams.DSYCP002_DUA_NUM, true, Types.INTEGER, new Integer(duaNum).toString()));
		reqMsg.addEvent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 */
	private List<DataSourceDTO> createDataSources(Iterator iter) {
		DataSourceDTO dataSourceDTO = null;
		Element ele = null;
		LinkedHashMap<String, DataSourceDTO> dataHash = new LinkedHashMap<String, DataSourceDTO>();
		List<DataSourceDTO> dataSourceDTOlst = new ArrayList<DataSourceDTO>();

		while (iter.hasNext()) {
			ele = (Element) iter.next();
			dataSourceDTO = (DataSourceDTO) dataHash.get(ele.getChildText("DATASTORE-ID"));
			if (dataSourceDTO == null) {
				dataSourceDTO = new DataSourceDTO();
				dataSourceDTO.setDataSourceId(Integer.parseInt(ele.getChildText("DATASTORE-ID").trim()));

				dataSourceDTO.setName(ele.getChildText("DATASTORE-NM"));

				dataHash.put(ele.getChildText("DATASTORE-ID"), dataSourceDTO);
			}

		}

		dataSourceDTOlst = dataHash.values().stream().collect(Collectors.toList());

		return dataSourceDTOlst;

	}

	
}
