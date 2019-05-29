/**
 * 
 */
package gov.hhs.cms.desy.service.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.cms.desy.xml.req.XmlMsgConst;
import gov.hhs.cms.desy.exception.SystemException;

/**
 * @author Jagannathan.Narashim
 *
 */
public class ParseResponseXmlUtil {
	private static final Logger log = LoggerFactory.getLogger(ParseResponseXmlUtil.class);
	
	/**
	 * @param reqMsg
	 * @return
	 * @throws Exception
	 */
	public static Document sendXMLMsg(String reqMsg) {
		// Creating an object of SAXBuilder a jdom api to build xml message.
		SAXBuilder sax = new SAXBuilder();
		try {
			return sax.build(new StringReader(reqMsg));
		} catch (JDOMException | IOException e) {
			throw new SystemException("Failed to build xml message ", e);
		}		
	}

	/**
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static HashMap retrieveErrorHeader(Document doc)  {
		HashMap hash = new HashMap();

		String retCode = "999999";
		String errCode = "999999";
		String errType = "NO_TYPE";
		String errMsg = "NON";

		if (doc.getRootElement().getChildren(XmlMsgConst.DESY_RESP) != null) {
			Iterator desyErrorRespItr = doc.getRootElement().getChildren(XmlMsgConst.DESY_RESP).iterator();
			if (desyErrorRespItr.hasNext()) {
				// retriving the values from the iterator object
				Element desyErrorRespEle = (Element) desyErrorRespItr.next();
				retCode = desyErrorRespEle.getChildText(XmlMsgConst.RETCODE_TAG);
				errCode = desyErrorRespEle.getChildText(XmlMsgConst.ERRCODE_TAG);
				errType = desyErrorRespEle.getChildText(XmlMsgConst.ERRTYPE_TAG);
				errMsg = desyErrorRespEle.getChildText(XmlMsgConst.ERRMSG_TAG);
			}
		}
		// saving the values on to hash map
		hash.put(XmlMsgConst.RETCODE_TAG, new Integer(retCode));
		hash.put(XmlMsgConst.ERRCODE_TAG, new Integer(errCode));
		hash.put(XmlMsgConst.ERRTYPE_TAG, errType);
		hash.put(XmlMsgConst.ERRMSG_TAG, errMsg);

		return hash;
	}

	/**
	 * @param hash
	 * @param isUpdateMsg
	 * @throws Exception
	 */
	public static void checkAllSqlMsgCode(HashMap hash, boolean isUpdateMsg) throws Exception {

		// getting error info from xml message
		int errCode = getSqlMsgCode(hash);
		String errType = getErrorType(hash);
		String errMsg = getErrorMsg(hash);

		if ((errCode != 0) && (errCode != 4)) {
			if (errCode == -803)
				throw new Exception();

			if (errCode == 100) {
				if (isUpdateMsg) {
					throw new Exception();
				} else {
					return;
				}
			} else {
				Calendar calendar = new GregorianCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
				String timeID = sdf.format(calendar.getTime());

				log.info("Error identification #: " + timeID + "  Error Type: " + errType + "  Error Code: " + errCode
						+ "  Error Msg: " + errMsg);
				throw new SystemException("Error identification #: " + timeID);
			}
		} // end-if

	}

	/**
	 * @param hash
	 * @return
	 */
	public static int getSqlMsgCode(HashMap hash) {
		if (hash.get(XmlMsgConst.ERRCODE_TAG) != null)
			return ((Integer) hash.get(XmlMsgConst.ERRCODE_TAG)).intValue();
		else
			return 0;
	}

	/**
	 * @param hash
	 * @return
	 */
	public static String getErrorType(HashMap hash) {
		if (hash.get(XmlMsgConst.ERRTYPE_TAG) != null)
			return (hash.get(XmlMsgConst.ERRTYPE_TAG).toString());
		else
			return "";
	}

	/**
	 * @param hash
	 * @return
	 */
	public static String getErrorMsg(HashMap hash) {
		if (hash.get(XmlMsgConst.ERRMSG_TAG) != null)
			return (hash.get(XmlMsgConst.ERRMSG_TAG).toString());
		else
			return "";
	}

	/**
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static Iterator convertXmlDoc(Document doc) {
		Iterator rowIter = null;

		// transforming message into iterator object
		if (doc.getRootElement().getChildren(XmlMsgConst.DATA_RESP) != null) {
			Iterator dataRespItr = doc.getRootElement().getChildren(XmlMsgConst.DATA_RESP).iterator();
			if (dataRespItr.hasNext()) {
				Element dataRespEle = (Element) dataRespItr.next();
				rowIter = dataRespEle.getChildren(XmlMsgConst.MSG_ROW).iterator();
			}
		}

		if (rowIter == null)
			rowIter = new ArrayList<>().iterator();

		return rowIter;
	}
	
	/**
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static Map sendCRUDXmlMsg(Document doc) {
		Map hash = new HashMap();
		//putting return code from RETCODE tag onto hash map
		hash.put(XmlMsgConst.RETCODE_TAG, new Integer(doc.getRootElement()
			.getChildText(XmlMsgConst.RETCODE_TAG)));
		//putting error code from ERRCODE tag onto hash map
		hash.put(XmlMsgConst.ERRCODE_TAG, new Integer(doc.getRootElement()
			.getChildText(XmlMsgConst.ERRCODE_TAG)));
		//putting error type from ERRTYPE tag onto hash map
		hash.put(XmlMsgConst.ERRTYPE_TAG, doc.getRootElement().getChildText(
			XmlMsgConst.ERRTYPE_TAG));
		//putting error msg from ERRMSG tag onto hash map
		hash.put(XmlMsgConst.ERRMSG_TAG, doc.getRootElement().getChildText(
			XmlMsgConst.ERRMSG_TAG));
		return hash;
	}
}
