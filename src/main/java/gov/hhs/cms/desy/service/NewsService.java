/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.NewsDTO;


public interface NewsService {
	public List<NewsDTO> getAllNews();
	public String updateNews(NewsDTO newsDTO, String action);
}
