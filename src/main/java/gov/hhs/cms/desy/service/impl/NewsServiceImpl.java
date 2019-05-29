/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.service.NewsService;
import gov.hhs.cms.desy.service.dto.NewsDTO;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlNewsBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlNewsDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlNewsEventDTO;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.ReqDBTableParams;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XMLReqParm;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;


@Named
public class NewsServiceImpl extends HelperService implements NewsService {

	private final Logger log = LoggerFactory.getLogger(NewsServiceImpl.class);
	
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.dto.News#getAllNews(java.lang.String)
	 */
	@Override
	public List<NewsDTO> getAllNews() {
		log.info("NewsImpl :: getAllNews");

		String reqXML = requestXml(this.getCurrentUserId());

		log.info("Request XML for get getAllNews, {}:", reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for get getAllNews, {}:", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		return getAllNewsItems(xmlDocItr);
		
	}
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.dto.News#updateNews(gov.hhs.cms.desy.service.dto.NewsDTO, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateNews(NewsDTO newsDTO, String action) {
		log.info("NewsImpl :: updateNews");
		
		String requestXML = requestXmForUpdateNews(newsDTO, this.getCurrentUserId(), action);
		log.info("Request XML for News update, {}:", requestXML);
		
		String iibResponse = dsyHttpAsync.getResponseFromIIB( requestXML);

		log.info("Response XML for News item update, {}:", iibResponse);
		
		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);
		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

		log.info("Error code value in Request id response XML, {}:", requestIdMap.get(XmlMsgConst.ERRCODE_TAG));

		int errTagValue = (requestIdMap.get(XmlMsgConst.ERRCODE_TAG) != null)?((Integer) requestIdMap.get(XmlMsgConst.RETCODE_TAG)).intValue():0;
				
		return String.valueOf(errTagValue);
	}
	
	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
    //	Creating message for DSYCP041
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP041");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP041_NEWS_ID,
								  true, Types.INTEGER, "-1"));
		reqMsg.addContent(query);
		reqMsg.setUser(userId);
		
		return reqMsg.getPrettyXML();
	}
	
	/**
	 * @param iter
	 * @return
	 * @throws Exception
	 */
	private List<NewsDTO> getAllNewsItems(Iterator iter)  {
		
		Element ele = null;
		NewsDTO newsDTO = null;
		List<NewsDTO> newsDTOLst = new ArrayList<NewsDTO>();
		while (iter.hasNext())
		{
			ele = (Element) iter.next();
			// retrieving all fields from xml message and saving the values with newsItem Object 
			newsDTO = new NewsDTO();
			newsDTO.setId(Integer.parseInt(ele.getChildText("SYS-NEWS-ID")));
			newsDTO.setDesc(ele.getChildText("SYS-NEWS-TXT"));
			newsDTO.setPostDate(ele.getChildText("SYS-NEWS-PST-DT"));
			newsDTO.setActive(ele.getChildText("SYS-NEWS-STUS-CD"));
			newsDTOLst.add(newsDTO);
		}
		
		return newsDTOLst;
	}
	
	
	
	/**
	 * @param newsDTO
	 * @param userId
	 * @param action
	 * @return
	 * @throws Exception
	 */
	private String requestXmForUpdateNews(NewsDTO newsDTO, String userId, String action){
		JAXBContext context = null;
	    Marshaller m;
	    StringWriter sw = new StringWriter();
	    String xmlStr = "";	   
		
		XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
		xmlHeaderDTO.setFunction("UPDATE");
		xmlHeaderDTO.setAction(action);
		xmlHeaderDTO.setUserId(userId);
		
		XmlNewsDTO xmlNewsDTO = new XmlNewsDTO();
		XmlNewsBodyDTO xmlNewsBodyDTO = new XmlNewsBodyDTO();
		XmlNewsEventDTO xmlNewsEventDTO = new XmlNewsEventDTO();
		xmlNewsEventDTO.setPn("DSYCP042");
		xmlNewsEventDTO.setSysNewsId(String.valueOf(newsDTO.getId())); 
		//xmlNewsEventDTO.setSysNewsPstDt(DateFunctions.formatDate(newsDTO.getPostDate()));
		xmlNewsEventDTO.setSysNewsPstDt(newsDTO.getPostDate()); // this should be MM/dd/yyyy format
		xmlNewsEventDTO.setSysNewsTxt(newsDTO.getDesc());
		log.info("newsDTO.isActive() ;" + newsDTO.isActive());
		xmlNewsEventDTO.setSysNewsStusCd(newsDTO.isActive()?"O":"N");//SYS_NEWS_STUS_CD  
		
		xmlNewsBodyDTO.setXmlNewsEventDTO(xmlNewsEventDTO);
		
		xmlNewsDTO.setXmlHeaderDTO(xmlHeaderDTO);
		xmlNewsDTO.setXmlNewsBodyDTO(xmlNewsBodyDTO);
		
		// create JAXB context and instantiate marshaller
		try {
			context = JAXBContext.newInstance(XmlNewsDTO.class);
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(xmlNewsDTO, sw);
		} catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
		}

	     xmlStr = sw.toString();

	     xmlStr = xmlStr.replaceAll("UTF-8", "IBM-1140");
	     
	     return xmlStr;
	}
	
	
}
