/**
 *
 */
package gov.hhs.cms.desy.iib;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Jagannathan.Narashim
 *
 */
@Named
public class DsyHttpAsync {

	private final Logger log = LoggerFactory.getLogger(DsyHttpAsync.class);

	@Value("${iibEnvKey}")
	private String iibHeaderKey;
	
	@Value("${iibEnvValue}")
	private String iibHeaderValue;
	
	@Value("${desyiib}")
	private String iiburl;
	
	/**
	 * @param url
	 * @param msgBody
	 * @throws InterruptedException | ExecutionException | IOException
	 */
	public String getResponseFromIIB(String msgReq) {
		log.info("DsyHttpAsync :: getResponseFromIIB #");

		String msgResponse = "";
		try (AsyncHttpClient c = asyncHttpClient()) {
			RequestBuilder requestBuilder = new RequestBuilder();
			requestBuilder.addHeader(iibHeaderKey, iibHeaderValue);
			requestBuilder.setBody(msgReq);
			requestBuilder.setUrl(String.format(iiburl));
			requestBuilder.setMethod("POST");
			msgResponse = c.executeRequest(requestBuilder).toCompletableFuture().thenApply(Response::getResponseBody)
					.get();
		} catch (InterruptedException | ExecutionException | IOException e) {
			log.error("Error occurred while retrieve response from MQ :", e);
			Thread.currentThread().interrupt();
		}
		return msgResponse;
	}

}
