/**
 *
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.BusinessException;
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.UserRoleService;
import gov.hhs.cms.desy.service.dto.RoleDTO;
import gov.hhs.cms.desy.service.dto.UserProfileDTO;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.util.StringUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUserProfileDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlUserProfileReqDTO;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.XMLQuery;
import gov.hhs.cms.desy.xml.req.XMLReqMsg;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class UserRoleServiceImpl implements UserRoleService{

	private final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;

	/*
	 * This method retrives list of available user roles from database.
     * It is being used to populate the drop down list with list of
 	 * available user roles so that administrator can assign specific role to the user
 	 *
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.UserRole#getUserRoles(java.lang.String)
	 */
	@Override
	public List<RoleDTO> getUserRoles(String userId) {
		log.info("UserRoleImpl :: getUserRoles");

		String reqXML = requestXml(userId);
		log.info("Request XML for get user roles :" + reqXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);

		log.info("Response XML for get user roles  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<RoleDTO> roleDTOLst = createRoleLst(xmlDocItr);

		return roleDTOLst;
	}

	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<RoleDTO> createRoleLst(Iterator itr) {
		List<RoleDTO> roleDTOLst = new ArrayList<RoleDTO>();
		RoleDTO role;
		Element ele = null;
		while (itr != null && itr.hasNext())
		{
			ele = (Element) itr.next();
			role = new RoleDTO();
			//retrieve each role id and description from XML and associate with role object
			role.setRoleID(Integer.parseInt(ele.getChildText("PRSN-ROLE-NUM")));
			role.setDescription(ele.getChildText("DATA-ROLE-DESC"));
			//adding role object to ArrayList
			roleDTOLst.add(role);
		}

		return roleDTOLst;
	}

	/**
	 * @param userId
	 * @return
	 */
	private String requestXml(String userId) {
		userId=userId.toUpperCase();
        // creating message for DSYCP023
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP023");
		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	@Override
	public boolean insertUserRoleReq(String userId, UserProfileDTO userProfileDTO) {

		performUserRoleReq(userId, userProfileDTO, String.valueOf(Db2Crud.CREATE));
		return true;
	}

	@Override
	public boolean updateUserRoleReq(String userId, UserProfileDTO userProfileDTO) {

		performUserRoleReq(userId, userProfileDTO, String.valueOf(Db2Crud.UPDATE));
		return true;
	}

	@Override
	public boolean deleteUserRoleReq(String userId, UserProfileDTO userProfileDTO) {

		performUserRoleReq(userId, userProfileDTO, String.valueOf(Db2Crud.DELETE));
		return true;
	}


	private void performUserRoleReq(String userId, UserProfileDTO userProfileDTO, String action) {

		String requestXML = getUserRoleReqXML(userId, userProfileDTO, action);

		requestXML = requestXML.replaceAll("UTF-8", "IBM-1140");

		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);
		
		log.info("Response XML for login user saved and submitted request(s)  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

	    log.info("Error code value in user profile response XML :" + requestIdMap.get(XmlMsgConst.ERRCODE_TAG));

	}

	/**
	 * @param userID
	 * @param userProfileDTO
	 * @param action
	 * @return
	 * @throws Exception
	 */
	private String getUserRoleReqXML(String userID, UserProfileDTO userProfileDTO, String action) {
	    JAXBContext context = null;
	    Marshaller m = null;
	    String xmlStr = "";
	    
        //converting user id to uppercase as user information is stored in DESY and DADSS database as uppercase
		 userID=userID.toUpperCase();
	     XmlUserProfileDTO xmlUserProfileDTO =  new XmlUserProfileDTO();
	     xmlUserProfileDTO.setPn("DSYCP034");
	     xmlUserProfileDTO.setUserId(userProfileDTO.getUserDTO().getUserId());
	     xmlUserProfileDTO.setRoleId(String.valueOf(userProfileDTO.getRoleDTO().getRoleID()));

	     XmlUserProfileReqDTO xmlUserProfileReqDTO = new XmlUserProfileReqDTO();

	     XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
	     xmlHeaderDTO.setAction(action);
	     xmlHeaderDTO.setFunction("USER");
	     xmlHeaderDTO.setUserId(userID);

	     xmlUserProfileReqDTO.setXmlUserProfileDTO(xmlUserProfileDTO);
	     xmlUserProfileReqDTO.setXmlHeaderDTO(xmlHeaderDTO);

	     try {
			 // create JAXB context and instantiate marshaller
		     context = JAXBContext.newInstance(XmlUserProfileReqDTO.class);
		     m = context.createMarshaller();
		     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		     StringWriter sw = new StringWriter();	     
		     m.marshal(xmlUserProfileReqDTO, sw);
		     xmlStr = sw.toString();	    	 
	     }catch (JAXBException e) {
				throw new SystemException("Failed to create JAXB context", e);
	     }


	     log.info("XML String \n" + xmlStr);

	     return xmlStr;
	}

}
