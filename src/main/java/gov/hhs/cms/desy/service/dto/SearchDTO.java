/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDTO implements Serializable{
	private static final long serialVersionUID = 5580688825225557385L;
	
	private List<FilterDTO> filters;
	private List<FinderFileDTO> finderFiles;
	/**
	 * @return the filters
	 */
	public List<FilterDTO> getFilters() {
		return filters;
	}
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(List<FilterDTO> filters) {
		this.filters = filters;
	}
	/**
	 * @return the finderFiles
	 */
	public List<FinderFileDTO> getFinderFiles() {
		return finderFiles;
	}
	/**
	 * @param finderFiles the finderFiles to set
	 */
	public void setFinderFiles(List<FinderFileDTO> finderFiles) {
		this.finderFiles = finderFiles;
	}
	
	/**
	 * @param list
	 */
	public void removeFilter(FilterDTO fil) {
		
		if (this.filters != null)
		{
			for(int i= 0;i<this.filters.size();i++)
			{
				FilterDTO filter=this.filters.get(i);
				if((filter.getColumnID()== fil.getColumnID()) &&
				   (filter.getGroupID()==fil.getGroupID()) &&
				   (filter.getOperator().equalsIgnoreCase(fil.getOperator())) &&
				   (filter.getValue().equalsIgnoreCase(fil.getValue())) )
				  {
				  	 this.filters.remove(i); 
				  	 if(fil.getValue().equalsIgnoreCase("USER INPUT FILE"))
				  	 {
						removeInputFile(fil);
				  	 }
				  	 
				  } 
			}
					
		}

	}
	private void removeInputFile(FilterDTO fil) {
		
		if (this.finderFiles != null)
		{
			for(int i= 0;i<this.finderFiles.size();i++)
			{
				FinderFileDTO file=this.finderFiles.get(i);
				if((file.getFinderColumn()!=null)&&
				   (file.getFinderColumn().getColumnID()== fil.getColumnID()) &&
				   (file.getGroupID()==fil.getGroupID()))
				  {
					 this.finderFiles.remove(i); 
					 
				  } 
			}
					
		}
	}
	
}
