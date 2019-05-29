/**
 * 
 */
package gov.hhs.cms.desy.service.xml.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Jagannathan.Narashim
 *
 */
public class XmlNewsEventDTO {

	private String pn;
	private String sysNewsId;
	private String sysNewsPstDt;
	private String sysNewsStusCd;
	private String sysNewsTxt;
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
	 * @return the sysNewsId
	 */
	@XmlElement(name = "SYS_NEWS_ID")
	public String getSysNewsId() {
		return sysNewsId;
	}
	/**
	 * @param sysNewsId the sysNewsId to set
	 */
	public void setSysNewsId(String sysNewsId) {
		this.sysNewsId = sysNewsId;
	}
	/**
	 * @return the sysNewsPstDt
	 */
	@XmlElement(name = "SYS_NEWS_PST_DT")
	public String getSysNewsPstDt() {
		return sysNewsPstDt;
	}
	/**
	 * @param sysNewsPstDt the sysNewsPstDt to set
	 */
	public void setSysNewsPstDt(String sysNewsPstDt) {
		this.sysNewsPstDt = sysNewsPstDt;
	}
	/**
	 * @return the sysNewsStusCd
	 */
	@XmlElement(name = "SYS_NEWS_STUS_CD")
	public String getSysNewsStusCd() {
		return sysNewsStusCd;
	}
	/**
	 * @param sysNewsStusCd the sysNewsStusCd to set
	 */
	public void setSysNewsStusCd(String sysNewsStusCd) {
		this.sysNewsStusCd = sysNewsStusCd;
	}
	/**
	 * @return the sysNewsTxt
	 */
	@XmlElement(name = "SYS_NEWS_TXT")
	public String getSysNewsTxt() {
		return sysNewsTxt;
	}
	/**
	 * @param sysNewsTxt the sysNewsTxt to set
	 */
	public void setSysNewsTxt(String sysNewsTxt) {
		this.sysNewsTxt = sysNewsTxt;
	}
	
	
}
