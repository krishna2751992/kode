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
import gov.hhs.cms.desy.service.ColumnNamesService;
import gov.hhs.cms.desy.service.dto.ColumnDTO;
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
public class ColumnNamesServiceImpl implements ColumnNamesService{
	private final Logger log = LoggerFactory.getLogger(ColumnNamesServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;
	
	public List<ColumnDTO> getColumnDTO(String userId, String datasourceId, String dataTypeId){
		
		List<ColumnDTO>  columnDTOLst = new ArrayList<ColumnDTO>();
		String requestXML = getRequestXML(userId, datasourceId, dataTypeId);
		
		log.info("Request XML for get getColumnDTO :" + requestXML);		
		
		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);		
		
		log.info("Response XML for get getColumnDTO  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);
		
		columnDTOLst = geColumnNames(xmlDocItr);
		
		return columnDTOLst;
	}
	
	private String getRequestXML(String userId, String datasourceId, String dataTypeId) {
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		//calling CICS DSYCP045
		XMLQuery query = new XMLQuery("DSYCP045");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP045_DATASOURCE_ID,
							true, Types.INTEGER, new Integer(datasourceId).toString()));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP045_DATATYPE_ID,
									true, Types.INTEGER, new Integer(dataTypeId).toString()));					
		reqMsg.addEvent(query);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * This method retrieves names for all the column Ids and names from dsy_element tables
	 * @param iter
	 * @return
	 */
	private  List<ColumnDTO> geColumnNames(Iterator iter)
	{
		ColumnDTO col = null;
		Element ele = null;
		List<ColumnDTO> columnDTOLst = new ArrayList<ColumnDTO>();

		while (iter.hasNext())
		{
			ele = (Element) iter.next();
			col = new ColumnDTO();
			col.setColumnID(Integer.parseInt(ele.getChildText("ELE-ID").trim()));
			
			col.setName(ele.getChildText("ELE-NM"));
			
			columnDTOLst.add(col);
		 }
				
		 return columnDTOLst;

	}
	
	
}
