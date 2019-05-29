/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

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
import org.springframework.beans.factory.annotation.Value;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.OutputTypesService;
import gov.hhs.cms.desy.service.dto.OutputTypeDTO;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class OutputTypesServiceImpl implements OutputTypesService{
	private final Logger log = LoggerFactory.getLogger(OutputTypesServiceImpl.class);
	
	@Inject
	private DsyHttpAsync dsyHttpAsync;

	@Value("${desyiib}")
	private String iiburl;
	

	/* 
	 * retrieves list of output types available from the database and is called by getdataTyperules method and 
     * only values based on data type rules are being sent to front end.
     * 
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.web.rest.OutputTypes#getOutputTypes(java.lang.String)
	 */
	@Override
	public List<OutputTypeDTO> getOutputTypes(String userId) {
		log.info("OutputTypesServiceImpl :: getOutputTypes#");
		String reqXML = requestXml(userId);
		log.info("Request XML for OutputTypes :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for OutputTypes  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<OutputTypeDTO>  outputTypeDTOLst = createOutputTypes(xmlDocItr);
		
		return outputTypeDTOLst;
	}
	
	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
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
	private List<OutputTypeDTO>  createOutputTypes(Iterator iter) {
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
		outputTypeDTOLst = outputTypeHash.values().stream().collect(Collectors.toList());
		
		return outputTypeDTOLst;
	}
	
	
}
