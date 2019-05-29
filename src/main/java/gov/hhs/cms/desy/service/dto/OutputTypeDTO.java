/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutputTypeDTO implements Serializable{
	private static final long serialVersionUID = 8492286590788623614L;
	@SafeHtml
	private String viewID;
	@SafeHtml
	private String description;
	@JsonIgnore
	private char viewType; // S - System View(Bef-Puf View) U - User Defined view
	
	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getViewID() {
		return viewID;
	}


	/**
	 * @return
	 */
	public int getViewIDInt() {
		if(viewID!=null)
		{
			if(viewID.startsWith("V")) 
			{
				if (viewID.substring(1).trim().equalsIgnoreCase("")) {    
				return 0;
				} else if(viewID.trim().equalsIgnoreCase("V2")) {    
			    return -1;
			    } else { 
				return Integer.parseInt(viewID.substring(1));
				}
			}
			else if( viewID.startsWith("C")) 
			{
				if(Character.toString(viewID.charAt(viewID.length()-1)).equalsIgnoreCase("U") ||
					Character.toString(viewID.charAt(viewID.length()-1)).equalsIgnoreCase("S"))
				{
					if (viewID.substring(1,viewID.length()-1).trim().equalsIgnoreCase("")) {    
						return 0;
					} else { 
						return Integer.parseInt(viewID.substring(1,viewID.length()-1));
					}            
				}
				else
				{
					if (viewID.substring(1).trim().equalsIgnoreCase("")) {    
						return 0;
					} else { 
						return Integer.parseInt(viewID.substring(1));
	          		}
				}
			
			}
			else
				if (viewID.substring(0).trim().equalsIgnoreCase(""))     
					return 0;
				else	
					return Integer.parseInt(viewID);
		}
		else
		{
			return -2;
		}
	}
	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param i
	 */
	public void setViewID(String i) {
		if(!(i.startsWith("V")) && !(i.startsWith("C")))
		{
		 viewID = "V"+i;
		}
		else
		{
		 viewID=i;
		}
	}

	public void setViewID(String i, String customView) {
		viewID = customView+i;
	}

	public void setViewID(String i, String customView,int userNum) {
		if(customView.startsWith("C"))
		{
			if(userNum>0)
			{
				viewType='U';  //User defined view
			    viewID = customView+i+'U';
			}else if (userNum==0)
			{
				viewType='S';  //Bef Puf view
			    viewID = customView+i+'S';
			}
		}
		else
		{
			viewID = customView+i;
		}
	}

	/**
	 * @return
	 */
	public char getViewType() {
		return viewType;
	}

	/**
	 * @param c
	 */
	public void setViewType(char c) {
		viewType = c;
	}
	
}
