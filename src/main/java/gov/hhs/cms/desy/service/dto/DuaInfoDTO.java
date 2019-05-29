package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DuaInfoDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -57431260811947197L;
	
	private DuaDTO duaDTO;
	private List<DataSourceDTO> dataSourceDTOLst;
	private List<RecipientDTO> recipientsDTOLst;
	
	/**
	 * @return the duaDTO
	 */
	public DuaDTO getDuaDTO() {
		return duaDTO;
	}
	/**
	 * @param duaDTO the duaDTO to set
	 */
	public void setDuaDTO(DuaDTO duaDTO) {
		this.duaDTO = duaDTO;
	}
	/**
	 * @return the dataSourceDTOLst
	 */
	public List<DataSourceDTO> getDataSourceDTOLst() {
		return dataSourceDTOLst;
	}
	/**
	 * @param dataSourceDTOLst the dataSourceDTOLst to set
	 */
	public void setDataSourceDTOLst(List<DataSourceDTO> dataSourceDTOLst) {
		this.dataSourceDTOLst = dataSourceDTOLst;
	}
	/**
	 * @return the recipientsDTOLst
	 */
	public List<RecipientDTO> getRecipientsDTOLst() {
		return recipientsDTOLst;
	}
	/**
	 * @param recipientsDTOLst the recipientsDTOLst to set
	 */
	public void setRecipientsDTOLst(List<RecipientDTO> recipientsDTOLst) {
		this.recipientsDTOLst = recipientsDTOLst;
	}	
	
}