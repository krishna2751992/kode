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
import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.UserProfileService;
import gov.hhs.cms.desy.service.dto.RoleDTO;
import gov.hhs.cms.desy.service.dto.UserDTO;
import gov.hhs.cms.desy.service.dto.UserProfileDTO;
import gov.hhs.cms.desy.service.util.DBStringConverter;
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
public class UserProfileServiceImpl implements UserProfileService {

	private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

	@Inject
	private DsyHttpAsync dsyHttpAsync;


	/*
	 * This method retrieves list of user (user profiles) based on user id
	 * and user name selected by user in the front end which is
	 * being passed as searchUser object to this method
	 * It is being used on Manage user screens to search for a specific user.
	 *
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.UserProfile#getUserProfile(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override	
	public List<UserProfileDTO> getUserProfileDetails(String loginUserId) {
		log.info("UserProfileImpl :: getUserProfile");

		String reqXML = getUserProfileReqeustXml(loginUserId);

		log.info("Request XML for get Userprofile :" + reqXML);
		
		String iibResponse = dsyHttpAsync.getResponseFromIIB(reqXML);
		
		log.info("Response XML for get Userprofile  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		List<UserProfileDTO> userProfileDTOLst = createUserProfile(xmlDocItr);

		return userProfileDTOLst;
	}

	/**
	 * @param itr
	 * @return
	 * @throws Exception
	 */
	private List<UserProfileDTO> createUserProfile(Iterator itr) {

		List<UserProfileDTO> userProfileDTOLst = new ArrayList<UserProfileDTO>();
		UserProfileDTO profile;
		Element ele = null;
		RoleDTO role;
		UserDTO user;

		while (itr != null && itr.hasNext()) {
			ele = (Element) itr.next();
			profile = new UserProfileDTO();
			user = new UserDTO();

			user.setUserId(ele.getChildText("USER-ID"));
			user.setUserName(ele.getChildText("CMS-PRSN-NAME"));
			user.setEmail(ele.getChildText("CMS-EMAIL-NAME"));
//			DBStringConverter.checkDBIntegerNull converts NUL values to null
			String userNumber = DBStringConverter.checkDBIntegerNull(ele.getChildText("USER-NB"));
			if (userNumber != null && ele.getChildText("USER-NB").length() > 0)
				user.setUserNum(Integer.parseInt(userNumber));
			else
				user.setUserNum(0);

			role = new RoleDTO();
			// DBStringConverter.checkDBIntegerNull converts NUL values to null
			String roleNumber = DBStringConverter.checkDBIntegerNull(ele.getChildText("PRSN-ROLE-NUM"));
			if (roleNumber != null && ele.getChildText("PRSN-ROLE-NUM").length() > 0)
				role.setRoleID(Integer.parseInt(roleNumber));
			else
				role.setRoleID(0);
			// DBStringConverter.checkDBIntegerNull converts NUL values to null
			String roleDesc = DBStringConverter.checkDBCharNull(ele.getChildText("DATA-ROLE-DESC"));
			role.setDescription(roleDesc);

			profile.setUserDTO(user);
			profile.setRoleDTO(role);

			userProfileDTOLst.add(profile);
		}

		return userProfileDTOLst; // returning profile object to calling program
	}

	/**
	 * @param loginUserId
	 * @param srchProflUserId
	 * @param srchProflUserName
	 * @return
	 */
	private String requestXml(String loginUserId, String srchProflUserId, String srchProflUserName) {

		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, loginUserId.toUpperCase(), Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP022");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP022_USER_ID, true, Types.CHAR, srchProflUserId));
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP022_USER_NAME, true, Types.CHAR,
				Db2Crud.replaceSpaceWithTilda(srchProflUserName)));
		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

		
	/* This method is being used to retrieve User profile based on user id. 
	 * It is being executed from the front end during user login
	 * 
	 * (non-Javadoc)
	 * @see gov.hhs.cms.desy.service.UserProfile#getUserProfile(gov.hhs.cms.desy.service.dto.UserDTO)
	 */
	@Override
	public UserProfileDTO getUserProfile(String loginUserId, String srchProflUserId, String srchProflUserName) {
		log.info("UserProfileImpl :: getUserProfile #");

		String requestXML = requestXml(loginUserId, srchProflUserId, srchProflUserName);

		log.info("Request XML for get Userprofile :" + requestXML);
		
		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);
		
		log.info("Response XML for get Userprofile  :" + iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Iterator xmlDocItr = ParseResponseXmlUtil.convertXmlDoc(xmlDoc);

		UserProfileDTO userProfileDTO = convertUserProfile(xmlDocItr);

		return userProfileDTO;
	}

	private String getUserProfileReqeustXml(String userId) {
		// Creating message for DSYCP029
		XMLReqMsg reqMsg = new XMLReqMsg(XmlMsgConst.FUNCTION_QUERY, userId, Db2Crud.READ);
		XMLQuery query = new XMLQuery("DSYCP029");
		query.addParmName(new XMLReqParm(ReqDBTableParams.DSYCP029_USER_ID, true, Types.CHAR, userId.toUpperCase()));
		reqMsg.addContent(query);

		return reqMsg.getPrettyXML();
	}

	private UserProfileDTO convertUserProfile(Iterator iter) {

		Element ele = null;
		UserProfileDTO profile = null;

		int counter = 0;
		while (iter != null && iter.hasNext()) {
			ele = (Element) iter.next();
			if (counter++ == 0) {
				// passing the response xml to createProfile function to create UserProfiel
				// object
				profile = createProfile(ele);

			}
		}

		// If system did not retrieved the user profile for a specific user. It redirect
		// user to error page
		if (profile == null)
			throw new SystemException("User ID does not have a role defined.");
		return profile; // returning the profile object to calling program
	}

	/**
	 * This method is being called by getProfile method to create User profile based
	 * on xml retrived from back end
	 *
	 * @param user
	 * @param ele
	 * @return
	 */
	private UserProfileDTO createProfile(Element ele) {
		UserDTO user = new UserDTO();
		UserProfileDTO profile = new UserProfileDTO();
		user = new UserDTO();

		user.setUserId(ele.getChildText("USER-ID"));
		user.setUserName(ele.getChildText("CMS-PRSN-NAME"));		

		RoleDTO role = new RoleDTO();
		// DBStringConverter.checkDBIntegerNull converts NUL values to null
		String roleNumber = DBStringConverter.checkDBIntegerNull(ele.getChildText("PRSN-ROLE-NUM"));
		if (roleNumber != null && ele.getChildText("PRSN-ROLE-NUM").length() > 0)
			role.setRoleID(Integer.parseInt(roleNumber));
		else
			role.setRoleID(0);
		// DBStringConverter.checkDBIntegerNull converts NUL values to null
		String roleDesc = DBStringConverter.checkDBCharNull(ele.getChildText("DATA-ROLE-DESC"));
		role.setDescription(roleDesc);

		profile.setUserDTO(user);
		profile.setRoleDTO(role);

		return profile; // returning profile object to calling program
	}

}
