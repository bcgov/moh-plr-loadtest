package ca.bc.gov.health.plr.testharness.requestbuilder.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.health.plr.testharness.requestbuilder.code.GatlingEndpoints;
import ca.bc.gov.health.plr.testharness.requestbuilder.code.MessageType;
import ca.bc.gov.health.plr.testharness.requestbuilder.dto.MessageTypeModel;

import static ca.bc.gov.health.plr.testharness.requestbuilder.code.MessageSpec.FHIR;

@Service
public class GatlingTriggerService {

    private static final Logger logger = LoggerFactory.getLogger(GatlingTriggerService.class);
    protected CloseableHttpClient client;

    @Value("${api.url}")
    private String apiUrl;

    public boolean triggerGatling(MessageTypeModel model) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

        client = HttpClientBuilder.create().build();

        URIBuilder uriBuilder = new URIBuilder();
            
        logger.info("GatlingTriggerService with: " + model.getMessageTypeName());
                
        switch(model.getMessageTypeName()){
            //For future reference: Implement endpoints for different messages
            case MessageType.QUERY_PROVIDER_BY_IDENTIFIER:                    
                uriBuilder = new URIBuilder(apiUrl + GatlingEndpoints.QUERYPROVIDERBYIDENTIFIERFHIR);

                uriBuilder.addParameter("hasCPN", String.valueOf(model.getHasIdentifierCPN()));
                uriBuilder.addParameter("hasIPC", String.valueOf(model.getHasIdentifierIPC()));

                break;
        }

        uriBuilder.addParameter("environment", String.valueOf(model.getEnvironmentName()));
        uriBuilder.addParameter("users", String.valueOf(model.getNumberOfUsers()));
        uriBuilder.addParameter("records", String.valueOf(model.getNumberOfRecords()));
        uriBuilder.addParameter("pause", String.valueOf(model.getPauseDuration()));
        uriBuilder.addParameter("spec", FHIR);
        
        URI uri = uriBuilder.build();
        // Create an HttpGet request with the URI
        HttpPost httpPost = new HttpPost(uri);
    

        logger.info("Hitting Gatling Endpoint : " + httpPost.getURI());
        httpPost.setURI(uri);
        try {

            CloseableHttpResponse response = client.execute(httpPost);
        
            logger.info("Completed call for Endpoint : " + httpPost.getURI());

            String jsonResponseBody = EntityUtils.toString(response.getEntity());
            JsonNode parsedJsonResponse = new ObjectMapper().readTree(jsonResponseBody);

            //check if api was successful. expecting code == 0
            return (parsedJsonResponse.has("code") && parsedJsonResponse.get("code").asInt() == 0);

        } catch (Exception e) {
            throw e;
        }
    }
    
}
