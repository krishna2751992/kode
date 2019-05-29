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
import gov.hhs.cms.desy.service.SavedViewsService;
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
public class SavedViewsServiceImpl implements SavedViewsService {
	private final Logger log = LoggerFactory.getLogger(SavedViewsServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves the list of available views(predefined as well as user defined
	 * views) based on data source and data type selected by the user and is being
	 * used to display the list of views on Output screen.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.SavedViews#getSavedViews(int, int,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<OutputTypeDTO> getSavedViews(int dataSourceId, int dataTypeId, String userId, String userNumber){

		String reqXML = requestXml(dataSourceId, dataTypeId, userId, userNumber);
		
		log.info("Request XML  for saved views :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for saved views  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<OutputTypeDTO> outputTypeDTOLst = createSavedViewsLst(xmlDocItr);

		return outputTypeDTOLst;
	}

	/**
	 * @param dataSourceId
	 * @param dataTypeId
	 * @param userId
	 * @param userNumber
	 * @return
	 */
	private String requestXml(int dataSourceId, int dataTypeId, String userId, String userNumber) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// Calling CICS DSYCP010
		XMLQuery query = new XMLQuery("DSYCP010");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP010_DATASOURCE_ID, true, Types.INTEGER,
				Integer.toString(dataSourceId)));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP010_DATATYPE_ID, true, Types.INTEGER,
				Integer.toString(dataTypeId)));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP010_USER_NUM, true, Types.INTEGER, userNumber));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<OutputTypeDTO> createSavedViewsLst(Iterator itr) {
		List<OutputTypeDTO> outputTypeDTOLst = new ArrayList<OutputTypeDTO>();
		Element ele = null;
		OutputTypeDTO view = null;

		while (itr.hasNext()) {
			ele = (Element) itr.next();
			view = new OutputTypeDTO();
			// setting view id and view name with Outputtype object
			view.setViewID(ele.getChildText("RQST-CSTM-VW-ID"), "C",
					Integer.parseInt(ele.getChildText("PRSN-USER-NUM")));
			view.setDescription(ele.getChildText("RQST-VW-NAME"));
			outputTypeDTOLst.add(view);
		}

		return outputTypeDTOLst;
	}
	
}
