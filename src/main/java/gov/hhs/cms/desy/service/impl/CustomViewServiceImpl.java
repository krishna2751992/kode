/**
 * 
 */
package gov.hhs.cms.desy.service.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.exception.SystemException;
import gov.hhs.cms.desy.service.CustomViewService;
import gov.hhs.cms.desy.service.ErrorMsgService;
import gov.hhs.cms.desy.service.dto.ViewDTO;
import gov.hhs.cms.desy.service.util.ParseResponseXmlUtil;
import gov.hhs.cms.desy.service.xml.dto.XmlHeaderDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlViewBodyDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlViewDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlViewEleIdDTO;
import gov.hhs.cms.desy.service.xml.dto.XmlViewEventDTO;
import gov.hhs.cms.desy.web.rest.errors.ErrorConstants;
import gov.hhs.cms.desy.xml.req.Db2Crud;
import gov.hhs.cms.desy.xml.req.XmlMsgConst;

@Named
public class CustomViewServiceImpl extends HelperService implements CustomViewService {

	private final Logger log = LoggerFactory.getLogger(CustomViewServiceImpl.class);

	@Inject
	private ErrorMsgService errorMsg;

	@Override
	public ViewDTO updateCustomView(ViewDTO viewDTO) {
		log.info("CustomViewServiceImpl :: updateCustomView #");
		String requestXML = getRequestXML(viewDTO, this.getCurrentUserId());
		log.info("requestXML, {}:", requestXML);

		String iibResponse = dsyHttpAsync.getResponseFromIIB(requestXML);
		log.info("Response XML for login user saved and submitted request(s), {}:", iibResponse);

		Document xmlDoc = ParseResponseXmlUtil.sendXMLMsg(iibResponse);

		Map requestIdMap = ParseResponseXmlUtil.sendCRUDXmlMsg(xmlDoc);

		if (requestIdMap.get(XmlMsgConst.ERRCODE_TAG) != null && (Integer
				.parseInt(requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString()) > ErrorConstants.ERROR_CODE_0)) {
			log.info("Error code value in Request id response XML, {}:", requestIdMap.get(XmlMsgConst.ERRCODE_TAG));
			log.info("Error Message, {}:",
					errorMsg.getErrorMessage(requestIdMap.get(XmlMsgConst.ERRCODE_TAG).toString()));
		}

		// retrieves the superID from xml
		String viewID = requestIdMap.get(XmlMsgConst.RETCODE_TAG).toString();

		viewDTO.setViewID(Integer.parseInt(viewID));

		return viewDTO;
	}

	private String getRequestXML(ViewDTO viewDTO, String userId) {
		JAXBContext cp033 = null;
		Marshaller marCp033 = null;
		String xmlStr1 = "";

		XmlHeaderDTO xmlHeaderDTO = new XmlHeaderDTO();
		String action = (viewDTO.getViewID() > 0) ? String.valueOf(Db2Crud.UPDATE) : String.valueOf(Db2Crud.CREATE);

		xmlHeaderDTO.setAction(action);
		xmlHeaderDTO.setUserId(userId.toUpperCase());
		xmlHeaderDTO.setFunction(XmlMsgConst.FUNCTION_UPDATE);

		XmlViewEleIdDTO xmlViewEleIdDTO = new XmlViewEleIdDTO();
		xmlViewEleIdDTO.setElmIdLst(getElementIds(viewDTO));

		XmlViewEventDTO xmlViewEventDTO = new XmlViewEventDTO();
		xmlViewEventDTO.setCustmViewId(String.valueOf(viewDTO.getViewID()));
		xmlViewEventDTO.setDataObjId(String.valueOf(viewDTO.getDataTypeID()));
		xmlViewEventDTO.setMetaDsId(String.valueOf(viewDTO.getDataSourceID()));
		xmlViewEventDTO.setPn("DSYCP033");
		xmlViewEventDTO.setUserNb(String.valueOf(viewDTO.getUserNum()));
		xmlViewEventDTO.setViewName(viewDTO.getViewName());
		xmlViewEventDTO.setXmlViewEleIdDTO(xmlViewEleIdDTO);

		XmlViewBodyDTO xmlViewBodyDTO = new XmlViewBodyDTO();
		xmlViewBodyDTO.setXmlViewEventDTO(xmlViewEventDTO);

		XmlViewDTO xmlCustomViewDTO = new XmlViewDTO();

		xmlCustomViewDTO.setXmlHeaderDTO(xmlHeaderDTO);
		xmlCustomViewDTO.setXmlViewBodyDTO(xmlViewBodyDTO);

		try {
			// create JAXB context and instantiate marshaller
			cp033 = JAXBContext.newInstance(XmlViewDTO.class);
			marCp033 = cp033.createMarshaller();
			marCp033.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			StringWriter sw1 = new StringWriter();
			marCp033.marshal(xmlCustomViewDTO, sw1);
			xmlStr1 = sw1.toString();

		} catch (JAXBException e) {
			throw new SystemException("Failed to create JAXB context", e);
		}

		return xmlStr1;
	}

	private List<String> getElementIds(ViewDTO viewDTO) {
		return viewDTO.getColumnsDTO().stream().map(colmDto -> String.valueOf(colmDto.getColumnID()))
				.collect(Collectors.toList());
	}

}
