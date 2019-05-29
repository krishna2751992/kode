/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DataTypeRuleDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6680553592741651732L;

	private int dataTypeId;
	private boolean mustSearch;
	private boolean yearEnabled;
	private boolean canSearch;
	private boolean singleHICNEnabled;
	private boolean zipPlus4Enabled;
	private boolean headerEnabled;
	private List<OutputTypeDTO> outputTypes;
	private String recFormat;
	private int maxConditionSet;
	private List<ColumnRulesDTO> columnRules;
	
	/**
	 * @return
	 */
	public boolean isCanSearch() {
		return canSearch;
	}


	/**
	 * @return the columnRules
	 */
	public List<ColumnRulesDTO> getColumnRules() {
		return columnRules;
	}



	/**
	 * @return
	 */
	public int getDataTypeId() {
		return dataTypeId;
	}

	/**
	 * @return
	 */
	public boolean isHeaderEnabled() {
		return headerEnabled;
	}

	/**
	 * @return
	 */
	public int getMaxConditionSet() {
		return maxConditionSet;
	}

	/**
	 * @return
	 */
	public boolean isMustSearch() {
		return mustSearch;
	}

	public void removeOutputType(OutputTypeDTO output) {
		
		if (this.outputTypes != null)
		{
			for(int i= 0;i<this.outputTypes.size();i++)
			{
				OutputTypeDTO outputType =(OutputTypeDTO) this.outputTypes.get(i);
				if(outputType.getViewID().equalsIgnoreCase(output.getViewID()))
				  {
				  	 this.outputTypes.remove(i); 
				  } 
			}
					
		}

	}


	/**
	 * @return the outputTypes
	 */
	public List<OutputTypeDTO> getOutputTypes() {
		return outputTypes;
	}

	/**
	 * @return
	 */
	public String getRecFormat() {
		return recFormat;
	}

	/**
	 * @return
	 */
	public boolean isSingleHICNEnabled() {
		return singleHICNEnabled;
	}

	/**
	 * @return
	 */
	public boolean isYearEnabled() {
		return yearEnabled;
	}

	/**
	 * @return
	 */
	public boolean isZipPlus4Enabled() {
		return zipPlus4Enabled;
	}

	/**
	 * @param b
	 */
	public void setCanSearch(boolean b) {
		canSearch = b;
	}

	/**
	 * @param list
	 */
	public void addColumnRules(ColumnRulesDTO rules) {
		if (this.columnRules == null)
				this.columnRules = new ArrayList();
		if (rules != null)
				this.columnRules.add(rules);
	}


	/**
	 * @param columnRules the columnRules to set
	 */
	public void setColumnRules(List<ColumnRulesDTO> columnRules) {
		this.columnRules = columnRules;
	}


	/**
	 * @param i
	 */
	public void setDataTypeId(int i) {
		dataTypeId = i;
	}

	/**
	 * @param b
	 */
	public void setHeaderEnabled(boolean b) {
		headerEnabled = b;
	}

	/**
	 * @param i
	 */
	public void setMaxConditionSet(int i) {
		maxConditionSet = i;
	}

	/**
	 * @param b
	 */
	public void setMustSearch(boolean b) {
		mustSearch = b;
	}

	/**
	 * @param list
	 */
	public void addOutputType(OutputTypeDTO outputType) {
		if (this.outputTypes == null)
				this.outputTypes = new ArrayList();
		if (outputType != null)
				this.outputTypes.add(outputType);
	}

	/**
	 * @param outputTypes the outputTypes to set
	 */
	public void setOutputTypes(List<OutputTypeDTO> outputTypes) {
		this.outputTypes = outputTypes;
	}


	/**
	 * @param string
	 */
	public void setRecFormat(String string) {
		recFormat = string;
	}

	/**
	 * @param b
	 */
	public void setSingleHICNEnabled(boolean b) {
		singleHICNEnabled = b;
	}

	/**
	 * @param b
	 */
	public void setYearEnabled(boolean b) {
		yearEnabled = b;
	}

	/**
	 * @param b
	 */
	public void setZipPlus4Enabled(boolean b) {
		zipPlus4Enabled = b;
	}

	
}
