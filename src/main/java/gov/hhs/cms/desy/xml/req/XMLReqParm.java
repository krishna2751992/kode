/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

import java.io.Serializable;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XMLReqParm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3341433506574556318L;
	
	private int orderId;
	private String parmName;
	private boolean inParm;
	private int parmType;
	private String parm;

	public XMLReqParm(int orderId, boolean inParm, int parmType, String parm)
	{
		this.orderId = orderId;
		this.inParm = inParm;
		this.parmType = parmType;
		this.parm = XMLIllegalCharConverter.replaceIllegalCharacters(parm);
	}

	public XMLReqParm(String parmeterName, boolean inParm, int parmType,
		String parm)
	{
		this.parmName = parmeterName;
		this.inParm = inParm;
		this.parmType = parmType;
		this.parm = XMLIllegalCharConverter.replaceIllegalCharacters(parm);
	}

	/**
	 * @return Returns the orderId.
	 */
	public int getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId The orderId to set.
	 */
	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * @return Returns the parm.
	 */
	public String getParm()
	{
		return parm;
	}

	/**
	 * @param parm The parm to set.
	 */
	public void setParm(String parm)
	{
		this.parm = parm;
	}

	/**
	 * @return Returns the parmType.
	 */
	public int getParmType()
	{
		return parmType;
	}

	/**
	 * @param parmType The parmType to set.
	 */
	public void setParmType(int parmType)
	{
		this.parmType = parmType;
	}

	/**
	 * @return Returns the inParm.
	 */
	public boolean isInParm()
	{
		return inParm;
	}

	/**
	 * @param inParm The inParm to set.
	 */
	public void setInParm(boolean inParm)
	{
		this.inParm = inParm;
	}

	/**
	 * @return Returns the parmName.
	 */
	public String getParmName()
	{
		return parmName;
	}

	/**
	 * @param parmName The parmName to set.
	 */
	public void setParmName(String parmName)
	{
		this.parmName = parmName;
	}

}
