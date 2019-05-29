/**
 * 
 */
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

/**
 * @author Jagannathan.Narashim
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParseResponseXmlUtil.class})
public class DenialReasonCodesServiceImplTest {

	@Mock
	private DsyHttpAsync dsyHttpAsyncMock;
	
	@InjectMocks
	private DenialReasonCodesServiceImpl denialReasonCodesServiceImpl;
	

	
	private String requestXML = "<?xml version=\"1.0\" encoding=\"IBM-1140\"?>\r\n" + 
				"<DESY-REQ>\r\n" + 
				"  <HEADER>\r\n" + 
				"    <FUNCTION>QUERY</FUNCTION>\r\n" + 
				"    <USER>NDOM</USER>\r\n" + 
				"    <ACTION>R</ACTION>\r\n" + 
				"  </HEADER>\r\n" + 
				"  <BODY>\r\n" + 
				"    <EVENT>\r\n" + 
				"      <PN>DSYCP026</PN>\r\n" + 
				"      <TYPE>D</TYPE>\r\n" + 
				"    </EVENT>\r\n" + 
				"  </BODY>\r\n" + 
				"</DESY-REQ>";
	
	
	private String responseXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<DESY-RESP-MSG>\r\n" + 
				"   <DESY-RESP>\r\n" + 
				"      <RETCODE>0</RETCODE>\r\n" + 
				"      <ERRCODE>0</ERRCODE>\r\n" + 
				"      <ERRTYPE>NONE</ERRTYPE>\r\n" + 
				"      <ERRMSG>DSYCP026 Program Success</ERRMSG>\r\n" + 
				"   </DESY-RESP>\r\n" + 
				"   <DATA-RESP>\r\n" + 
				"      <ROW>\r\n" + 
				"         <APRVL-STUS>200</APRVL-STUS>\r\n" + 
				"         <APRVL-DCRN>DENIED</APRVL-DCRN>\r\n" + 
				"      </ROW>\r\n" + 
				"      <ROW>\r\n" + 
				"         <APRVL-STUS>300</APRVL-STUS>\r\n" + 
				"         <APRVL-DCRN>APPROVED</APRVL-DCRN>\r\n" + 
				"      </ROW>\r\n" + 
				"      <ROW>\r\n" + 
				"         <APRVL-STUS>400</APRVL-STUS>\r\n" + 
				"         <APRVL-DCRN>PENDING</APRVL-DCRN>\r\n" + 
				"      </ROW>\r\n" + 
				"      <ROW>\r\n" + 
				"         <APRVL-STUS>410</APRVL-STUS>\r\n" + 
				"         <APRVL-DCRN>IN REVIEW</APRVL-DCRN>\r\n" + 
				"      </ROW>\r\n" + 
				"      <ROW>\r\n" + 
				"         <APRVL-STUS>420</APRVL-STUS>\r\n" + 
				"         <APRVL-DCRN>CANCELLED</APRVL-DCRN>\r\n" + 
				"      </ROW>\r\n" + 
				"   </DATA-RESP>\r\n" + 
				"</DESY-RESP-MSG>";
	
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(ParseResponseXmlUtil.class);
		when(ParseResponseXmlUtil.sendXMLMsg(this.requestXML)).thenReturn(mock(Document.class));
		when(ParseResponseXmlUtil.convertXmlDoc(mock(Document.class))).thenReturn(mock(Iterator.class));
	}
	
	@Test
	public void testGetDenialReasonCodes() {
		
		when(dsyHttpAsyncMock.getResponseFromIIB(anyString())).thenReturn(this.responseXML);
		
		denialReasonCodesServiceImpl.getDenialReasonCodes("NDOM");
		
		verify(dsyHttpAsyncMock, times(1)).getResponseFromIIB(anyString());
	}
	
}
