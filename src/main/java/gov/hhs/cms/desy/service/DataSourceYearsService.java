/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.DataSourceYearsDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DataSourceYearsService {
	public List<DataSourceYearsDTO> getDataSourceYears(int duaNum,int dataSourceID,int dataTypeID,String stateCode, String userID);
	public List<DataSourceYearsDTO> getDataSourceYearsWithNoStateCode(int duaNum,int dataSourceID,int dataTypeID, String userID);
}
