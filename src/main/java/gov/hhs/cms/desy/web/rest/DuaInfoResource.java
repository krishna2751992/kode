/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.DuaInfoService;
import gov.hhs.cms.desy.service.dto.DuaInfoDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class DuaInfoResource {

	private final Logger log = LoggerFactory.getLogger(DuaInfoResource.class);
	
	@Inject
	private DuaInfoService duaInfo;	
	
	@GetMapping(value = "/get-dua-info/{duaNum}",  produces = "application/json")
	public ResponseEntity<DuaInfoDTO> getDuaInfo(@PathVariable int duaNum, HttpServletRequest request){
		log.info("DuaInfoResource :: getDuaInfo:");
		DuaInfoDTO duaInfoDTO = new DuaInfoDTO();
		
		duaInfoDTO = duaInfo.getDua(duaNum, request.getSession().getAttribute("userId").toString());
		log.info("getDuaInfo: duaInfoDTO :" + duaInfoDTO);
		
		return new ResponseEntity<>(duaInfoDTO, HttpStatus.OK);
	}
}
