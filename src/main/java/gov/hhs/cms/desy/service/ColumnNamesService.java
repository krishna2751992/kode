/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.ColumnDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface ColumnNamesService {
	public List<ColumnDTO> getColumnDTO(String userId, String datasourceId, String dataTypeId) ;
}
