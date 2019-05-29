package gov.hhs.cms.desy.service.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.jdom2.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import gov.hhs.cms.desy.iib.DsyHttpAsync;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ParseResponseXmlUtil.class})
public class UserDuaImplTest {
	
	@Mock
	private DsyHttpAsync dsyHttpAsyncMock;
	
	@InjectMocks
	private UserDuaServiceImpl testee;
	
	private String requestXml= "<DESY-REQ>\r\n" + 
			"  <HEADER>\r\n" + 
			"    <FUNCTION>QUERY</FUNCTION>\r\n" + 
			"    <USER>NDOM</USER>\r\n" + 
			"    <ACTION>R</ACTION>\r\n" + 
			"  </HEADER>\r\n" + 
			"  <BODY>\r\n" + 
			"    <EVENT>\r\n" + 
			"      <PN>DSYCP014</PN>\r\n" + 
			"      <DATA_RQST_ID>-1</DATA_RQST_ID>\r\n" + 
			"      <DUA_NUM>-1</DUA_NUM>\r\n" + 
			"      <STDY_NAME />\r\n" + 
			"      <USER_ID>NDOM</USER_ID>\r\n" + 
			"      <CMS_PRSN_NAME />\r\n" + 
			"      <CRT_DT_FROM />\r\n" + 
			"      <CRT_DT_TO />\r\n" + 
			"      <REQ_ACT>BOTH</REQ_ACT>\r\n" + 
			"    </EVENT>\r\n" + 
			"  </BODY>\r\n" + 
			"</DESY-REQ>";
	
	private String reponseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<DESY-RESP-MSG>\r\n" + 
			"   <DESY-RESP>\r\n" + 
			"      <RETCODE>0</RETCODE>\r\n" + 
			"      <ERRCODE>0</ERRCODE>\r\n" + 
			"      <ERRTYPE>NONE</ERRTYPE>\r\n" + 
			"      <ERRMSG>DSYCP014 Program Success</ERRMSG>\r\n" + 
			"   </DESY-RESP>\r\n" + 
			"   <DATA-RESP>\r\n" + 
			"      <ROW>\r\n" + 
			"         <SUPER-ID>23514</SUPER-ID>\r\n" + 
			"         <DUA-NB>16838</DUA-NB>\r\n" + 
			"         <CRT-DT>2019-04-29-15.51.30.065809</CRT-DT>\r\n" + 
			"         <DESY-EXPRTN-DT>05/02/2019</DESY-EXPRTN-DT>\r\n" + 
			"         <DATA-DESC>NCH</DATA-DESC>\r\n" + 
			"         <RQST-NM>Test</RQST-NM>\r\n" + 
			"         <USER-ID>NDOM</USER-ID>\r\n" + 
			"         <CMS-PRSN-NAME>JAGANNATHAN NARASHIMMAN</CMS-PRSN-NAME>\r\n" + 
			"         <RECIP-NM />\r\n" + 
			"         <DUA-STUS-CD>O</DUA-STUS-CD>\r\n" + 
			"         <SUPPRESS-COPY-FLG>N</SUPPRESS-COPY-FLG>\r\n" + 
			"         <REQUEST-LIST>\r\n" + 
			"            <REQUEST>\r\n" + 
			"               <DATA-RQST-ID>-1</DATA-RQST-ID>\r\n" + 
			"               <DATA-YEAR />\r\n" + 
			"               <CPLN-STUS>SAVED</CPLN-STUS>\r\n" + 
			"            </REQUEST>\r\n" + 
			"         </REQUEST-LIST>\r\n" + 
			"      </ROW>\r\n" + 
			"   </DATA-RESP>\r\n" + 
			"</DESY-RESP-MSG>";
	
	@Before
	public void setup() throws Exception {	 
		PowerMockito.mockStatic(ParseResponseXmlUtil.class);	
		when(ParseResponseXmlUtil.sendXMLMsg(this.requestXml)).thenReturn(mock(Document.class));
		when(ParseResponseXmlUtil.convertXmlDoc(mock(Document.class))).thenReturn(mock(Iterator.class));
	}
	
	@Test
	public void testGetApprovalRequest() throws Exception {
		
		when(dsyHttpAsyncMock.getResponseFromIIB(anyString())).thenReturn(this.reponseXml);		
		
		testee.getUserDuaList();
		
		verify(dsyHttpAsyncMock, times(1)).getResponseFromIIB(anyString());
		
	}

}
