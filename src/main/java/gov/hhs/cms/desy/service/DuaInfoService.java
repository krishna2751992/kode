/**
 * 
 */
package gov.hhs.cms.desy.service;

import gov.hhs.cms.desy.service.dto.DuaInfoDTO;

/**
 * @author Jagannathan.Narashim
 *
 */
public interface DuaInfoService {

	public DuaInfoDTO getDua(int duaNum,String userId);
}
