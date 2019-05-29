/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DataSourceRulesDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -544367783693964913L;
	
	private int dataSourceId;
	private boolean selectState;
	private DataTypeRuleDTO dataTypeRuleDTO;
	
	/**
	 * @return the dataSourceID
	 */
	public int getDataSourceId() {
		return dataSourceId;
	}
	/**
	 * @param dataSourceID the dataSourceID to set
	 */
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	/**
	 * @return the selectState
	 */
	public boolean isSelectState() {
		return selectState;
	}
	/**
	 * @param selectState the selectState to set
	 */
	public void setSelectState(boolean selectState) {
		this.selectState = selectState;
	}
	/**
	 * @return the dataTypeRuleDTO
	 */
	public DataTypeRuleDTO getDataTypeRuleDTO() {
		return dataTypeRuleDTO;
	}
	/**
	 * @param dataTypeRuleDTO the dataTypeRuleDTO to set
	 */
	public void setDataTypeRuleDTO(DataTypeRuleDTO dataTypeRuleDTO) {
		this.dataTypeRuleDTO = dataTypeRuleDTO;
	}	
	
	/**
	 * @param stateCode
	 * @return
	 */
	public boolean checkMustSearch(String stateCode)
	{
		boolean checkResult=false;
		if(dataTypeRuleDTO!=null)
		{
			boolean mustSearch=dataTypeRuleDTO.isMustSearch();
			if((selectState==true && stateCode !=null && stateCode.equalsIgnoreCase("00"))
			    || (selectState==false && mustSearch==true))
			{
				checkResult=true;
			}else
			{
				checkResult=false;
			}
			
		}
		return checkResult;
	}
}
