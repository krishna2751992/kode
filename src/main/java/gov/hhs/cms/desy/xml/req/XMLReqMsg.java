/**
 * 
 */
package gov.hhs.cms.desy.xml.req;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
This class represents the Desy XML request message sent to M/F through MQ
The following are sample messages generted by an object creted from this class.

This class is built on top of a jdom Document.  It builds a jdom document as
contents are added to the object. 

<xml version="1.0" encoding="UTF-8"?>
<DADSS-REQ>
  <HEADER>
    <FUNCTION>QUERY</FUNCTION>
    <USER>G29Z</USER>
    <ACTION>R</ACTION>
  </HEADER>
  <BODY>
    <EVENT>
      <SP>DASSP022</SP>
    </EVENT>
  </BODY>
</DADSS-REQ>


<?xml version="1.0" encoding="UTF-8"?>
<DADSS-REQ>
  <HEADER>
    <FUNCTION>CODE</FUNCTION>
    <USER>G29Z</USER>
    <ACTION>C</ACTION>
  </HEADER>
  <BODY>
    <EVENT>
      <SP>DASSPA07</SP>
      <CNTRY_CD>TEST</CNTRY_CD>
      <CNTRY_NAME>TEST CNTRY</CNTRY_NAME>
    </EVENT>
  </BODY>
</DADSS-REQ>

<?xml version="1.0" encoding="UTF-8"?>
<DADSS-REQ>
  <HEADER>
    <USER>G29Z</USER>
    <ACTION>D</ACTION>
    <FUNCTION>ORDER</FUNCTION>
  </HEADER>
  <BODY>
    <EVENT>
      <SP>DASSPA23</SP>
      <ORDR_NUM>21</ORDR_NUM>
      <ORDR_STUS_CD>2</ORDR_STUS_CD>
      <CNTCT_USER_ID>C19I</CNTCT_USER_ID>
      <RCPNT_ADR_ID>9121</RCPNT_ADR_ID>
      <CARR_ID>1</CARR_ID>
      <ORDR_ATCHMNT_NUM>1111</ORDR_ATCHMNT_NUM>
      <ORDR_OPRTNL_SW>Y</ORDR_OPRTNL_SW>
      <ORDR_PUF_SW>Y</ORDR_PUF_SW>
      <CARR_AIRBILL_NUM>0</CARR_AIRBILL_NUM>
      <ORDR_LABEL_QTY>10</ORDR_LABEL_QTY>
      <JOB_ACNTG_CD>0</JOB_ACNTG_CD>
      <CARR_ACNTG_NUM>0</CARR_ACNTG_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA11</SP>
      <MDA_TYPE_CD>1</MDA_TYPE_CD>
      <USER_ID>C19I</USER_ID>
      <CREATN_DT>07/29/2005 03:59</CREATN_DT>
      <DSN_NAME>TEST DSN 3</DSN_NAME>
      <TAPE_LRECL_NUM>555</TAPE_LRECL_NUM>
      <TAPE_BLKSIZE_NUM>1111</TAPE_BLKSIZE_NUM>
      <TAPE_RECFM_CD>RF1</TAPE_RECFM_CD>
      <TAPE_LABEL_CD>LB1</TAPE_LABEL_CD>
      <REC_CNT>11111111</REC_CNT>
      <FIL_YR_DT>2001</FIL_YR_DT>
    </EVENT>
    <EVENT>
      <SP>DASSPA15</SP>
      <DUA_NUM>1</DUA_NUM>
      <DATA_DESC_CD>NCH</DATA_DESC_CD>
      <ORGNL_DUA_NUM>1</ORGNL_DUA_NUM>
      <RTRN_DSTRCTN_DT>1900-01-01</RTRN_DSTRCTN_DT>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00000</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00001</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00002</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00003</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00004</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA11</SP>
      <MDA_TYPE_CD>1</MDA_TYPE_CD>
      <USER_ID>C19I</USER_ID>
      <CREATN_DT>07/29/2005 03:59</CREATN_DT>
      <DSN_NAME>TEST DSN 3</DSN_NAME>
      <TAPE_LRECL_NUM>555</TAPE_LRECL_NUM>
      <TAPE_BLKSIZE_NUM>1111</TAPE_BLKSIZE_NUM>
      <TAPE_RECFM_CD>RF1</TAPE_RECFM_CD>
      <TAPE_LABEL_CD>LB1</TAPE_LABEL_CD>
      <REC_CNT>11111111</REC_CNT>
      <FIL_YR_DT>2001</FIL_YR_DT>
    </EVENT>
    <EVENT>
      <SP>DASSPA15</SP>
      <DUA_NUM>1</DUA_NUM>
      <DATA_DESC_CD>NCH</DATA_DESC_CD>
      <ORGNL_DUA_NUM>1</ORGNL_DUA_NUM>
      <RTRN_DSTRCTN_DT>1900-01-01</RTRN_DSTRCTN_DT>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00000</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00001</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00002</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00003</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA40</SP>
      <VOLSER_NUM>NEW_VOLUME00004</VOLSER_NUM>
    </EVENT>
    <EVENT>
      <SP>DASSPA24</SP>
      <USER_ID>H1AS</USER_ID>
      <ORDR_CMT_TXT>test</ORDR_CMT_TXT>
    </EVENT>
    <EVENT>
      <SP>DASSPA26</SP>
      <ORDR_STUS_CD>1</ORDR_STUS_CD>
      <USER_ID>G29Z</USER_ID>
    </EVENT>
  </BODY>
</DADSS-REQ>


 **/

/**
 * @author Jagannathan.Narashim
 *
 */
public class XMLReqMsg extends Document{


	public XMLReqMsg()
	{
		init();
	}

	public XMLReqMsg(String function)
	{
		init();
		setFunction(function);
	}

	public XMLReqMsg(String function, String userId, char action)
	{
		init();
		setFunction(function);
		setUser(userId);
		setAction(action);
	}

	/**
	 * 
	 */
	private void init()
	{
		setRootElement(new Element(XmlMsgConst.REQ_MSG_TAG));
		getRootElement().addContent(new Element(XmlMsgConst.HEADER_TAG));
		getRootElement().addContent(new Element(XmlMsgConst.BODY_TAG));
	}

	public Element getHeaderElement()
	{
		return getRootElement().getChild(XmlMsgConst.HEADER_TAG);
	}

	public Element getBodyElement()
	{
		return getRootElement().getChild(XmlMsgConst.BODY_TAG);
	}

	public void setUser(String userId)
	{
		getHeaderElement().addContent(
			new Element(XmlMsgConst.USER_TAG).setText(userId));
	}

	public String getXML()
	{
		return new XMLOutputter(Format.getCompactFormat().setEncoding(
			"IBM-1140")).outputString(this);
		//		return new XMLOutputter(Format.getPrettyFormat()).outputString(this);
	}

	public String getPrettyXML()
	{
		return new XMLOutputter(Format.getPrettyFormat()
			.setEncoding("IBM-1140")).outputString(this);
		//		return new XMLOutputter(Format.getPrettyFormat()).outputString(this);
	}

	public void addEvent(XMLBean xmlBean) throws Exception
	{
		getBodyElement().addContent(((XMLBean) xmlBean).getXMLElements());
	}

	public void addEvent(XMLEventCollection coll)
	{
		getBodyElement().addContent(coll.getEvents());
	}

	public void addContent(XMLEventCollection coll)
	{
		getBodyElement().addContent(coll.getEvents());
	}

	public void setAction(char action)
	{
		getHeaderElement().addContent(
			new Element(XmlMsgConst.ACTION_TAG).setText(Character
				.toString(action)));
	}

	public void setFunction(String function)
	{
		getHeaderElement().addContent(
			new Element(XmlMsgConst.FUNCTION_TAG).setText(function));
	}
	
	public void addProgramName(String programName)
	{
			getBodyElement().addContent(new Element(XmlMsgConst.PN_TAG).setText(programName));
	}
	public void addProcedureName(String procedureName)
	{
			getBodyElement().addContent(new Element(XmlMsgConst.SP_TAG).setText(procedureName));
	}

	
}
