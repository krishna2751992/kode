/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.DataSourceDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DataSourceService {
	public List<DataSourceDTO> getDataSources(int duaNum,String userID) ;
}
