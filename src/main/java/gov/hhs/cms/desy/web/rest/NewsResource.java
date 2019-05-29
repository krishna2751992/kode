/**
 * 
 */
package gov.hhs.cms.desy.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.hhs.cms.desy.service.ErrorMsgService;
import gov.hhs.cms.desy.service.NewsService;
import gov.hhs.cms.desy.service.dto.NewsDTO;
import gov.hhs.cms.desy.web.rest.errors.ErrorConstants;


@RestController
@RequestMapping("/api")
public class NewsResource {
	private final Logger log = LoggerFactory.getLogger(NewsResource.class);
	
	@Inject
	private NewsService news;
	
	@Inject
	private ErrorMsgService errorMsgService;
	
	@GetMapping("/get-all-news")	
	public ResponseEntity<List<NewsDTO>> getAllNews() {
		log.info("NewsResource :: getAllNews:");
		List<NewsDTO> newsDTOLst = news.getAllNews();
		log.info("getAllNews: newsDTOLst, {}:", newsDTOLst);
		return new ResponseEntity<>(newsDTOLst, HttpStatus.OK);
	}
	
	@PostMapping(value = "/add-news", consumes = "application/json", produces = "application/json")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> createNews(@RequestBody @Valid NewsDTO newsDTO) {
		log.info("NewsResource :: createNews:");
		log.info("Active value :" + newsDTO.isActive());
		String errorCd = news.updateNews(newsDTO, "C");
		log.info("NewsResource :: insertNews, {}:", errorCd);
		
		if(Integer.parseInt(errorCd) > ErrorConstants.ERROR_CODE_0) {
			String errorMsg = errorMsgService.getErrorMessage(errorCd);
			log.info("errorMsg, {}:", errorMsg);
		}
		return new ResponseEntity<>(errorCd, HttpStatus.OK);
	}	
	
	@PutMapping("/update-news")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> updateNews(@RequestBody @Valid NewsDTO newsDTO, HttpServletRequest request) {
		log.info("NewsResource :: insertNews:");
		
		String errorCd = news.updateNews(newsDTO, "U");
		log.info("NewsResource :: insertNews:, {}", errorCd);
		
		if(Integer.parseInt(errorCd) > ErrorConstants.ERROR_CODE_0) {
			String errorMsg = errorMsgService.getErrorMessage(errorCd);
			log.info("errorMsg, {}:", errorMsg);
		}
		return new ResponseEntity<>(errorCd, HttpStatus.OK);
	}
	
}
