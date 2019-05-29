/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.Map;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.service.CancelRequestService;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlCancelRequestBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlCancelRequestDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlCancelRequestEventDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

@Named
public class CancelRequestServiceImpl extends HelperService implements CancelRequestService {
	private final Logger log = LoggerFactory.getLogger(CancelRequestServiceImpl.class);

	/**
	 * Cancel user request
	 */
	@Override
	public String cancelRequestUpdate(String requestId) {
		log.info(" CancelRequest :: cancelRequestUpdate #");
		String requestXML = getRequestXML(requestId, this.getCurrentUserId());

		log.info("requestXML, {}:", requestXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);

		log.info("Response XML for cancel request, {}:", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

		log.info("Error code value in Request id response XML, {}:", requestIdMap.get(XmlMsgConst.ERRCODE_TAG));

		String retCode = requestIdMap.get(XmlMsgConst.RETCODE_TAG).toString();
		String errorCode = requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString();

		return errorCode;
	}

	/**
	 * @param requestId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private String getRequestXML(String requestId, String userId) {

		JAXBContext cp033 = null;
		Marshaller marCp033 = null;
		String xmlStr1 = "";

		XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
		xmlHeaderDTO.setAction(UPDATE);
		xmlHeaderDTO.setUserId(userId.toUpperCase());
		xmlHeaderDTO.setFunction(XmlMsgConst.FUNCTION_UPDATE);

		XmlCancelRequestEventDTO xmlCancelRequestEventDTO = new XmlCancelRequestEventDTO();
		xmlCancelRequestEventDTO.setRequestId(requestId);
		xmlCancelRequestEventDTO.setUserId(userId);
		xmlCancelRequestEventDTO.setPn("DSYCP018");

		XmlCancelRequestBodyDTO xmlCancelRequestBodyDTO = new XmlCancelRequestBodyDTO();
		xmlCancelRequestBodyDTO.setXmlCancelRequestEventDTO(xmlCancelRequestEventDTO);

		XmlCancelRequestDTO xmlCancelRequestDTO = new XmlCancelRequestDTO();
		xmlCancelRequestDTO.setXmlHeaderDTO(xmlHeaderDTO);
		xmlCancelRequestDTO.setXmlCancelRequestBodyDTO(xmlCancelRequestBodyDTO);

		try {
			// create JAXB context and instantiate marshaller
			cp033 = JAXBContext.newInstance(XmlCancelRequestDTO.class);
			marCp033 = cp033.createMarshaller();
			marCp033.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			StringWriter sw1 = new StringWriter();
			marCp033.marshal(xmlCancelRequestDTO, sw1);
			xmlStr1 = sw1.toString();
		} catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
		}

		return xmlStr1;
	}

}
