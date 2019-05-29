/**
 * 
 */
package gov.hhs.cms.desy.service;

import java.util.List;

import gov.hhs.cms.desy.service.dto.OutputTypeDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface OutputTypesService {

	public List<OutputTypeDTO> getOutputTypes(String userId);
}
