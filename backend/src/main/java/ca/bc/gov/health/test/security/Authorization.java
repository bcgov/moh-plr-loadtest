package ca.bc.gov.health.test.security;

import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.health.test.config.SimulationConfig;
import ca.bc.gov.health.test.constants.SimulationConstants;

public class Authorization implements SimulationConstants{

	private static final Logger logger = LoggerFactory.getLogger(Authorization.class);

	private final List<Header> headers = new ArrayList<Header>();
	private final Header headerContent = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
	private final Header headerAccept = new BasicHeader(HttpHeaders.ACCEPT, "application/json");
	final Configuration config = SimulationConfig.getConfig();
	
	CloseableHttpClient client;
	HttpPost httpPost;
	String targetAuthEnvironment;

	public void init(String env) throws Exception{
		headers.add(headerContent);
		headers.add(headerAccept);

		client = HttpClientBuilder.create().setDefaultHeaders(headers).build();
	}


	public String getBearerAuth() throws Exception {
		logger.info("Fetching new token");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		NameValuePair grantType = new BasicNameValuePair("grant_type", config.getString(COMMON_LOGIN_AUTH_GRANT_TYPE));
		NameValuePair clientId = new BasicNameValuePair("client_id", config.getString(COMMON_LOGIN_AUTH_CLIENT_IDS));
		NameValuePair clientSecret = new BasicNameValuePair("client_secret", config.getString(COMMON_LOGIN_AUTH_CLIENT_SECRETS_MRG));

		params.add(grantType);
		params.add(clientId);
		params.add(clientSecret);
		
		final String devAuthUrl = config.getString(COMMON_DEV_LOGIN_AUTH_URL);
		httpPost = new HttpPost(devAuthUrl);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		CloseableHttpResponse response = client.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String authResponse = EntityUtils.toString(entity, "UTF-8");
		logger.debug("Fetched new token {}", authResponse);
		// logger.debug("Fetched new token {} for organization {}", authResponse, orgKey);
		return authResponse;
	}
}


