/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlBody {

	private XmlEvent xmlEvent;

	/**
	 * @return the xmlEvent
	 */
	@XmlElement(name = "EVENT")
	public XmlEvent getXmlEvent() {
		return xmlEvent;
	}

	/**
	 * @param xmlEvent the xmlEvent to set
	 */
	public void setXmlEvent(XmlEvent xmlEvent) {
		this.xmlEvent = xmlEvent;
	}	
	
}
