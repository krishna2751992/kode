/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.ColumnRulesDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface ColumnRulesService {
	public ColumnRulesDTO getColumnRules(int dataSourceID, int dataTypeID, int columnID,String userID);
}
