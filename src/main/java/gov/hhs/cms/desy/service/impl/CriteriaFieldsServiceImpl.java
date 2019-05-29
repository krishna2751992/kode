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
import gov.hhs.cms.desy.service.CriteriaFieldsService;
import gov.hhs.cms.desy.service.dto.CriteriaFieldsDTO;
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
public class CriteriaFieldsServiceImpl implements CriteriaFieldsService {
	private final Logger log = LoggerFactory.getLogger(CriteriaFieldsServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves list of elements based on data source and data type selected by the
	 * user and being used to display the list of available search elements on
	 * search Criteria screen.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.CriteriaFields#getCriteriaFields(int, int,
	 * java.lang.String)
	 */
	@Override
	public List<CriteriaFieldsDTO> getCriteriaFields(int dataSourceId, int dataTypeId, String userId) {
		log.info("CriteriaFieldsServiceImpl getCriteriaFields #");
		List<CriteriaFieldsDTO> criteriaFieldsDTOLst = new ArrayList<CriteriaFieldsDTO>();

		String reqXML = requestXml(dataSourceId, dataTypeId, userId);
		
		log.info("Request XML for Criteria Fields :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Criteria Fields  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		criteriaFieldsDTOLst = createCriteriaSearchFields(xmlDocItr);

		return criteriaFieldsDTOLst;
	}

	/**
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param userId
	 * @return
	 */
	private String requestXml(int dataSourceId, int dataTypeId, String userId) {
		userId = userId.toUpperCase();
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP006
		XMLQuery query = new XMLQuery("DSYCP006");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP006_DATASOURCE_ID, true, Types.INTEGER,
				Integer.toString(dataSourceId)));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP006_DATATYPE_ID, true, Types.INTEGER,
				Integer.toString(dataTypeId)));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}


	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<CriteriaFieldsDTO> createCriteriaSearchFields(Iterator iter) {

		List<CriteriaFieldsDTO> criteriaFieldsDTOLst = new ArrayList<CriteriaFieldsDTO>();
		Element ele = null;
		CriteriaFieldsDTO criteriaFieldsDTO;
		while (iter.hasNext()) {
			criteriaFieldsDTO = new CriteriaFieldsDTO();
			ele = (Element) iter.next();

			// setting element id and name with CriteriaFieldsDTO object
			criteriaFieldsDTO.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID")));
			criteriaFieldsDTO.setName(ele.getChildText("ELE-NM"));

			criteriaFieldsDTOLst.add(criteriaFieldsDTO);
		}

		return criteriaFieldsDTOLst;
	}

}
