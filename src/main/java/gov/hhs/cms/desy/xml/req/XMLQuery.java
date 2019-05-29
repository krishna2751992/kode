/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XMLQuery implements XMLEventCollection{


	private Element element = new Element(XmlMsgConst.EVENT_TAG);

	public XMLQuery()
	{
		super();
	}

	public XMLQuery(String spName)
	{
		this.setProc(spName);
	}

	public List getEvents()
	{
		ArrayList list = new ArrayList();
		list.add(element);
		return list;
	}

	public void addParm(XMLReqParm parm)
	{
		element
			.addContent(new Element(XmlMsgConst.PARM_TAG + parm.getOrderId())
				.setText(parm.getParm()));
	}

	public void addParmName(XMLReqParm parm)
	{
		element.addContent(new Element(parm.getParmName()).setText(parm
			.getParm()));
	}

	public void setProc(String procName)
	{
		element.addContent(new Element(XmlMsgConst.PN_TAG).setText(procName));
	}

}
