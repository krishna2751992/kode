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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.FinderFileService;
import gov.hhs.cms.desy.service.dto.FinderFileDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
@RestController
@RequestMapping("/api")
public class FinderFileResource {
	private final Logger log = LoggerFactory.getLogger(FinderFileResource.class);
	
	@Inject
	private FinderFileService finderFile;
	
	@GetMapping(value = "/check-finder-file")
	public ResponseEntity<Boolean> isFinderFileExist(@RequestBody FinderFileDTO finderFileDTO, HttpServletRequest request) {
		log.info("FinderFileResource :: isFinderFileExist #");
		Boolean isFileExist = false;
		
		log.info(" isFinderFileExist - fileName :" + finderFileDTO.getFileName());
		
		isFileExist = Boolean.valueOf(finderFile.isFinderFileExist(finderFileDTO.getFileName(), request.getSession().getAttribute("userId").toString()));
		log.info("isFinderFileExist : isFileExist :" + isFileExist);
		
		return new ResponseEntity<Boolean>(isFileExist, HttpStatus.OK);
	}
}
