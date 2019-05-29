/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Jagannathan.Narashim
 *
 */
public class NewsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1921820279468034965L;
	private int id;
	@NotNull
	@SafeHtml
	private String desc;	
	@NotNull
	@SafeHtml
	private String postDate;
	private boolean isActive;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * @return the postDate
	 */
	public String getPostDate() {
		return postDate;
	}
	/**
	 * @param postDate the postDate to set
	 */
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	/**
	 * @param isActive The isActive to set.
	 */
	public void setActive(String act)
	{
		if (act.equals("O") || act.equalsIgnoreCase("OPEN"))
			this.isActive = true;
		else
			this.isActive = false;
	}	
	
}
