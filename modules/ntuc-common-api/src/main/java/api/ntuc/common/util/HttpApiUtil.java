package api.ntuc.common.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * @author fandifadillah
 *
 */

public class HttpApiUtil {
	private static Log log = LogFactoryUtil.getLog(HttpApiUtil.class);

	public static Object request(String apiUrl, String httpMethod, String requestBody, String clientId,
			String clientSecret, String secretKey) {

		try {
			HttpResponse httpResponse = null;
			if (httpMethod.equalsIgnoreCase(Http.Method.GET.name()) && Validator.isBlank(requestBody)) {
				httpResponse = httpGet(apiUrl, clientId, clientSecret);
			} else if (httpMethod.equalsIgnoreCase(Http.Method.GET.name()) && !Validator.isBlank(requestBody)) {
				httpResponse = httpGet(apiUrl, requestBody, clientId, clientSecret);
				return parseToJsonObject(httpResponse, true, secretKey);
			} else if (httpMethod.equalsIgnoreCase(Http.Method.POST.name())) {
				httpResponse = httpPost(apiUrl, requestBody, clientId, clientSecret);
			} else if (httpMethod.equalsIgnoreCase(Http.Method.PUT.name())) {
				httpResponse = httpPut(apiUrl, requestBody, clientId, clientSecret);
			} else if (httpMethod.equalsIgnoreCase(Http.Method.DELETE.name())) {
				httpResponse = httpDelete(apiUrl, clientId, clientSecret);
			}
			return parseToJsonObject(httpResponse);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	private static HttpResponse httpGet(String apiUrl, String clientId, String clientSecret) throws Exception {

		HttpGet http = new HttpGet(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);

		HttpClient httpClient = createHttpClient(apiUrl);

		return httpClient.execute(http);
	}

	public static Object httpGet(String apiUrl, Map<String, String> headers) throws Exception {

		HttpGet http = new HttpGet(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		for (Map.Entry<String, String> header : headers.entrySet()) {
			http.setHeader(header.getKey(), header.getValue());
		}

		HttpClient httpClient = createHttpClient(apiUrl);
		HttpResponse response = httpClient.execute(http);
		return parseToJsonObject(response);
	}

	private static HttpResponse httpGet(String apiUrl, String requestBody, String clientId, String clientSecret)
			throws Exception {

		HttpGetWithEntity http = new HttpGetWithEntity(apiUrl);
		http.setHeader("Content-Type", "text/plain");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);
		StringEntity postingString = new StringEntity(requestBody, ContentType.DEFAULT_TEXT);
		http.setEntity(postingString);
		HttpClient httpClient = createHttpClient(apiUrl);

		return httpClient.execute(http);
	}

	private static HttpResponse httpPost(String apiUrl, String requestBody, String clientId, String clientSecret)
			throws Exception {

		HttpPost http = new HttpPost(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);

		StringEntity postingString = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		http.setEntity(postingString);

		HttpClient httpClient = createHttpClient(apiUrl);

		return httpClient.execute(http);
	}

	private static HttpResponse httpPut(String apiUrl, String requestBody, String clientId, String clientSecret)
			throws Exception {

		HttpPut http = new HttpPut(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);

		StringEntity postingString = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		http.setEntity(postingString);

		HttpClient httpClient = createHttpClient(apiUrl);

		return httpClient.execute(http);
	}

	private static HttpResponse httpDelete(String apiUrl, String clientId, String clientSecret) throws Exception {

		HttpDelete http = new HttpDelete(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);

		HttpClient httpClient = createHttpClient(apiUrl);

		return httpClient.execute(http);
	}

	public static Object httpPostWithHeader(String apiUrl, SortedMap<String, String> headers, String requestBody,
			String clientId, String clientSecret) throws Exception {

		HttpPost http = new HttpPost(apiUrl);
		http.setHeader("Content-Type", "application/json");
		http.setHeader("Accept", "application/json");

		http.setHeader("client_id", clientId);
		http.setHeader("client_secret", clientSecret);

		for (SortedMap.Entry<String, String> hdr : headers.entrySet()) {
			http.setHeader(hdr.getKey(), hdr.getValue());
		}

		StringEntity postingString = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		http.setEntity(postingString);

		HttpClient httpClient = createHttpClient(apiUrl);

		return parseToJsonObject(httpClient.execute(http));
	}

	public static Object httpPostOauthToken(String apiUrl, String requestBody) throws Exception {

		HttpPost http = new HttpPost(apiUrl);
		http.setHeader("Content-Type", "application/x-www-form-urlencoded");

		StringEntity postingString = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		http.setEntity(postingString);

		HttpClient httpClient = createHttpClient(apiUrl);

		return parseToJsonObject(httpClient.execute(http));
	}

	protected static HttpClient createHttpClient(String apiUrl) throws Exception {

		HttpClient httpClient = null;
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();

		if (apiUrl.indexOf("https:") > -1) {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {

					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, null, null,
					new NoopHostnameVerifier());

			clientBuilder.setSSLSocketFactory(sslsf);
		}

		httpClient = clientBuilder.build();

		return httpClient;
	}

	protected static Object parseToJsonObject(HttpResponse httpResponse) {

		Object obj = null;

		try {
			if (httpResponse != null) {
				HttpEntity entity = httpResponse.getEntity();
				String jsonData = EntityUtils.toString(entity);

				if (jsonData.startsWith("[")) {
					obj = JSONFactoryUtil.createJSONArray(jsonData);
				} else {
					obj = JSONFactoryUtil.createJSONObject(jsonData);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());

		}

		return obj;
	}

	protected static Object parseToJsonObject(HttpResponse httpResponse, boolean encrypted, String secretKey) {

		Object obj = null;
		try {
			if (httpResponse != null) {
				HttpEntity entity = httpResponse.getEntity();
				String encryptedjsonData = EntityUtils.toString(entity);

				String jsonData = AESEncryptUtil.decrypt(encryptedjsonData, secretKey);

				if (jsonData.startsWith("[")) {
					obj = JSONFactoryUtil.createJSONArray(jsonData);
				} else {
					obj = JSONFactoryUtil.createJSONObject(jsonData);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return obj;
	}
}
