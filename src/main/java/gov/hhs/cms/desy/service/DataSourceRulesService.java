/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.DataSourceRulesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DataSourceRulesService {
	public DataSourceRulesDTO getDataSourceRules(int dataSourceID,String userID);	
}
