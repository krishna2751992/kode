/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.UserRoleService;
import gov.hhs.cms.desy.service.dto.RoleDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {
	private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);
	
	@Inject
	private UserRoleService userRole;
	
	/**
	 * This method retrieves list of available user roles from database.
	 * It is being used to populate the drop down list with list of 
	 * available user roles so that administrator can assign specific role to the user
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping(value = "/user-roles",  produces = "application/json")
	public ResponseEntity<List<RoleDTO>> getUserRoles(HttpServletRequest request) {
		log.info("UserRoleResource :: getUserRoles #");
		List<RoleDTO> roleDTOLst = new ArrayList<RoleDTO>();

		roleDTOLst = userRole.getUserRoles(request.getSession().getAttribute("userId").toString());
		log.info("getUserRoles : roleDTOLst :" + roleDTOLst);

		return new ResponseEntity<>(roleDTOLst, HttpStatus.OK);
	}

}
