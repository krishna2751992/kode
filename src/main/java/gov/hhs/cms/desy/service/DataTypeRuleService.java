/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.DataTypeRuleDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DataTypeRuleService {
	public DataTypeRuleDTO getDataTypeRule(int dataSourceID, int dataTypeID,String userID);

}
