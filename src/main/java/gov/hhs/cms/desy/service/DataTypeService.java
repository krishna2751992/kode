/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.DataTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DataTypeService {

	public List<DataTypeDTO> getDataTypes(int duaNum,char encryptionSwitch,int dataSourceID, String userID);
}
