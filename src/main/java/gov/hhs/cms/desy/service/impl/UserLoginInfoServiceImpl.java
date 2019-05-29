/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.UserLoginInfoService;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUserBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUserDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUserDataDTO;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class UserLoginInfoServiceImpl implements UserLoginInfoService{

	private final Logger log = LoggerFactory.getLogger(UserLoginInfoServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	@Value("${desyiib}")
	private String iiburl;
	
	/* (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.UserLoginInfo#updateLogonInfo(java.lang.String)
	 */
	@Override
	public String updateLogonInfo(String userId) {
		log.info("UserLoginInfoImpl :: updateLogonInfo #");
		
		String requestXML = getRequestXML(userId);
		
		log.info("Request XML for user Login info  :" + requestXML);
		
		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);
		log.info("Response XML for login user info  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);
		
		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);
		
	    log.info("Error code value in response XML :" + requestIdMap.get(XmlMsgConst.ERRCODE_TAG));
	    
	    return requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString();
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private String getRequestXML(String userId) {
	     JAXBContext cp031 = null;
	     Marshaller marCp031 = null;
	     String reqXmlStr = "";
	     
	     XmlHeaderDTO headerCP031 = new XmlHeaderDTO();
	     headerCP031.setAction("U");
	     headerCP031.setFunction("UPDATE");
	     headerCP031.setUserId(userId.toUpperCase());
		
	     XmlUserDataDTO xmlUserDataDTO = new XmlUserDataDTO();
	     xmlUserDataDTO.setPn("DSYCP032");
	     xmlUserDataDTO.setUserId(userId.toUpperCase());
	     
	     XmlUserBodyDTO xmlUserBodyDTO = new XmlUserBodyDTO();
	     xmlUserBodyDTO.setXmlUserDataDTO(xmlUserDataDTO);
	     
	     XmlUserDTO xmlUserDTO = new XmlUserDTO();
	     xmlUserDTO.setXmlHeaderDTO(headerCP031);
	     xmlUserDTO.setXmlUserBodyDTO(xmlUserBodyDTO);
	     
	     try {
			 // create JAXB context and instantiate marshaller
		     cp031 = JAXBContext.newInstance(XmlUserDTO.class);
		     marCp031 = cp031.createMarshaller();
		     marCp031.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		     
		     StringWriter sw1 = new StringWriter();
		     marCp031.marshal(xmlUserDTO, sw1);
		     reqXmlStr = sw1.toString();
		     
	     }catch (JAXBException e) {
				throw new SystemException("Failed to create JAXB context", e);
	     }
	     
	     return reqXmlStr;
	}	

}
