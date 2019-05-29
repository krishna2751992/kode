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

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.SelectableFieldsService;
import gov.hhs.cms.desy.service.dto.SelectableFieldsDTO;
import gov.hhs.cms.desy.service.dto.ViewFieldsDTO;
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
public class SelectableFieldsServiceImpl implements SelectableFieldsService {
	private final Logger log = LoggerFactory.getLogger(SelectableFieldsServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * Retrieves the list available elements to be displayed on out put screen when
	 * user selects "SELECT AVAILABLE FIELDS" option on output screen. This list is
	 * based on data source and data type selected by the user.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.SelectableFields#getSelectableFields(int, int,
	 * java.lang.String)
	 */
	@Override
	public List<SelectableFieldsDTO> getSelectableFields(int dataSourceId, int dataTypeId, String userId) {
		log.info("SelectableFieldsServiceImpl :: getSelectableFields#");
		List<SelectableFieldsDTO> selectableFieldsDTOLst = new ArrayList<SelectableFieldsDTO>();

		String reqXML = requestXml(dataSourceId, dataTypeId, userId);
		log.info("Request XML for Selectable Fields :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Selectable Fields  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		selectableFieldsDTOLst = createSelectableFields(xmlDocItr);

		return selectableFieldsDTOLst;
	}

	/**
	 * @param dataSourceID
	 * @param dataTypeID
	 * @param userId
	 * @return
	 */
	private String requestXml(int dataSourceID, int dataTypeID, String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP012
		XMLQuery query = new XMLQuery("DSYCP012");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP012_DATASOURCE_ID, true, Types.INTEGER,
				Integer.toString(dataSourceID)));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP012_DATATYPE_ID, true, Types.INTEGER,
				Integer.toString(dataTypeID)));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<SelectableFieldsDTO> createSelectableFields(Iterator iter) {

		List<SelectableFieldsDTO> selectableDTOLst = new ArrayList<SelectableFieldsDTO>();
		Element ele = null;
		SelectableFieldsDTO celectableFieldsDTO;
		while (iter.hasNext()) {
			celectableFieldsDTO = new SelectableFieldsDTO();
			ele = (Element) iter.next();

			// setting element id and name with CriteriaFieldsDTO object
			celectableFieldsDTO.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID")));
			celectableFieldsDTO.setName(ele.getChildText("ELE-NM"));

			selectableDTOLst.add(celectableFieldsDTO);
		}

		return selectableDTOLst;
	}

	/*
	 * Retrieves the list of elements based on predefined or user defined view,
	 * selected by the user on output screen and is being used to display the list
	 * when user select Bef-Puf view or user defined view on output screen.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see gov.hhs.cms.desy.service.SelectableFields#getFieldsForSelectedView(int,
	 * java.lang.String)
	 */
	@Override
	public List<ViewFieldsDTO> getFieldsForSelectedView(int viewID, String userId) {
		log.info("SelectableFieldsImpl :: getFieldsForSelectedView#");
		List<ViewFieldsDTO> viewFieldsDTOLst = new ArrayList<ViewFieldsDTO>();

		String reqXML = requestXmlForViewFields(viewID, userId);
		log.info("Request XML for Selectable View Fields  :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for Selectable View Fields  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		viewFieldsDTOLst = createViewFields(xmlDocItr);

		return viewFieldsDTOLst;
	}

	/**
	 * @param viewID
	 * @param userId
	 * @return
	 */
	private String requestXmlForViewFields(int viewID, String userId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		// calling CICS DSYCP013
		XMLQuery query = new XMLQuery("DSYCP013");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP013_CUSTOM_VIEW_ID, true, Types.INTEGER,
				Integer.toString(viewID)));

		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<ViewFieldsDTO> createViewFields(Iterator iter) {

		List<ViewFieldsDTO> viewFieldsDTOLst = new ArrayList<ViewFieldsDTO>();
		Element ele = null;
		ViewFieldsDTO viewFieldsDTO;
		while (iter.hasNext()) {
			viewFieldsDTO = new ViewFieldsDTO();
			ele = (Element) iter.next();

			// setting element id and name with CriteriaFieldsDTO object
			viewFieldsDTO.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID")));
			viewFieldsDTO.setName(ele.getChildText("ELE-NM"));

			viewFieldsDTOLst.add(viewFieldsDTO);
		}

		return viewFieldsDTOLst;
	}

}
