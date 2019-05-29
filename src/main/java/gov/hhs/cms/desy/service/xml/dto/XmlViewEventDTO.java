/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlViewEventDTO {

	private String pn;
	private String custmViewId;
	private String viewName;
	private String metaDsId;
	private String dataObjId;
	private String userNb;
	
	private XmlViewEleIdDTO xmlViewEleIdDTO;

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
	 * @return the custmViewId
	 */
	@XmlElement(name = "RQST_CSTM_VW_ID")
	public String getCustmViewId() {
		return custmViewId;
	}

	/**
	 * @param custmViewId the custmViewId to set
	 */
	public void setCustmViewId(String custmViewId) {
		this.custmViewId = custmViewId;
	}

	/**
	 * @return the viewName
	 */
	@XmlElement(name = "RQST_VW_NAME")
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return the metaDsId
	 */
	@XmlElement(name = "META_DS_ID")
	public String getMetaDsId() {
		return metaDsId;
	}

	/**
	 * @param metaDsId the metaDsId to set
	 */
	public void setMetaDsId(String metaDsId) {
		this.metaDsId = metaDsId;
	}

	/**
	 * @return the dataObjId
	 */
	@XmlElement(name = "DATA_OBJ_ID")
	public String getDataObjId() {
		return dataObjId;
	}

	/**
	 * @param dataObjId the dataObjId to set
	 */
	public void setDataObjId(String dataObjId) {
		this.dataObjId = dataObjId;
	}

	/**
	 * @return the userNb
	 */
	@XmlElement(name = "USER_NB")
	public String getUserNb() {
		return userNb;
	}

	/**
	 * @param userNb the userNb to set
	 */
	public void setUserNb(String userNb) {
		this.userNb = userNb;
	}

	/**
	 * @return the xmlViewEleIdDTO
	 */
	@XmlElement(name = "ELE")
	public XmlViewEleIdDTO getXmlViewEleIdDTO() {
		return xmlViewEleIdDTO;
	}

	/**
	 * @param xmlViewEleIdDTO the xmlViewEleIdDTO to set
	 */
	public void setXmlViewEleIdDTO(XmlViewEleIdDTO xmlViewEleIdDTO) {
		this.xmlViewEleIdDTO = xmlViewEleIdDTO;
	}	
	
}
