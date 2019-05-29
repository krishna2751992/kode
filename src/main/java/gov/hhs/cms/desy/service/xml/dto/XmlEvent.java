package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

public class XmlEvent {

	
	private String pn;
	private XmlDataRespDTO xmlDataRespDTO;
	
	/**
	 * @return the pn
	 */
	@XmlElement(name = "PN")
	public String getPn() {
		return pn;
	}

	/**
	 * @param pn the pn to set
	 */
	public void setPn(String pn) {
		this.pn = pn;
	}

	/**
	 * @return the xmlDataRespDTO
	 */
	@XmlElement(name = "DATA_RESP")
	public XmlDataRespDTO getXmlDataRespDTO() {
		return xmlDataRespDTO;
	}

	/**
	 * @param xmlDataRespDTO the xmlDataRespDTO to set
	 */
	public void setXmlDataRespDTO(XmlDataRespDTO xmlDataRespDTO) {
		this.xmlDataRespDTO = xmlDataRespDTO;
	}
	
	

}
