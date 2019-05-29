/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class MoreInfo implements Serializable {

	 private boolean filter;
	 private boolean search;
	 private boolean sort;
	/**
	 * @return the filter
	 */
	public boolean isFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	/**
	 * @return the search
	 */
	public boolean isSearch() {
		return search;
	}
	/**
	 * @param search the search to set
	 */
	public void setSearch(boolean search) {
		this.search = search;
	}
	/**
	 * @return the sort
	 */
	public boolean isSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(boolean sort) {
		this.sort = sort;
	}
	 	 
}
