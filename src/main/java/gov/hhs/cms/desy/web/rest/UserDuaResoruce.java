/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.UserDuaService;
import gov.hhs.cms.desy.service.dto.DuaDTO;


@RestController
@RequestMapping("/api")
public class UserDuaResoruce {
	private final Logger log = LoggerFactory.getLogger(UserDuaResoruce.class);

	@Inject
	private UserDuaService userDua;

	/**
	 * Retrieves DAU info based on User ID. *
	 * 
	 * @return <List<DuaDTO>
	 * 
	 */

	@GetMapping(value = "/user-dua-detail", produces = "application/json")
	public ResponseEntity<List<DuaDTO>> getUserDuaList() {
		log.info("UserDuaResoruce :: getUserDuaList #");
		List<DuaDTO> duaLst = userDua.getUserDuaList();
		log.info("getUserDuaList : duaLst, {}:", duaLst);
		return new ResponseEntity<>(duaLst, HttpStatus.OK);
	}
}
