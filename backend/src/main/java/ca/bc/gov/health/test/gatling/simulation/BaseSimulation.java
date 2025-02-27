package ca.bc.gov.health.test.gatling.simulation;

import static ca.bc.gov.health.test.constants.SimulationConstants.*;
import static io.gatling.javaapi.core.CoreDsl.bodyString;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.repeat;
import static io.gatling.javaapi.core.CoreDsl.responseTimeInMillis;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import static io.gatling.javaapi.jdbc.JdbcDsl.jdbcFeeder;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import org.apache.http.HttpHeaders;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.sql.Timestamp;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.health.test.model.MessageResponse;
import ca.bc.gov.health.test.security.RequestProcessor;


public class BaseSimulation extends Simulation implements SimulationRemote {

    private static final Logger logger = LoggerFactory.getLogger(BaseSimulation.class);
    protected final int msToSec = 1000;

    protected RequestProcessor auth = new RequestProcessor();
    protected Integer dbRecordsCount = 0;

    protected String dbURL = "";
    protected String dbUser = "";
    protected String dbPwd = "";


    protected String env = "";
    protected Integer totalRecords = -1;
    protected Integer totalUsers = 1;
    protected Integer numberOfRecords = -1;
    protected Integer pauseDuration = 0;
    protected Integer concurrentUsers = 1;
    protected Integer currentiterations = 0;
    protected String MSG_SPEC = "";

    protected List<MessageResponse> syncResponses = new ArrayList<MessageResponse>();
    protected List<MessageResponse> asyncResponses = new ArrayList<MessageResponse>();

    protected String msgCode = ""; //to allow outside access from recursion node traversal

    protected HttpProtocolBuilder setBaseHttpProtocol(String env) {
        String rootUrl;
        String hostHeader;
        rootUrl = config.getString(env + DASH + PLR_API_HOST_URL);
        hostHeader = config.getString(env + DASH + PLR_HEADER_API_HOST);
        HashMap<String, String> headers = new HashMap<String, String>();

        // headers.put(HttpHeaders.ACCEPT, "application/fhir+json;fhirVersion=4.0;BCPLRVersion=1");
        // headers.put("userID", "rsaravan@IDIR");
        // headers.put("PLR_ROLE", "REG_ADMIN");
        // headers.put("OrganizationId", "00002855");
        headers.put(HttpHeaders.ACCEPT, config.getString(env + DASH + HEADER_CONTENT_TYPE));
        headers.put("userID", config.getString(env + DASH + HEADER_USER_ID));
        headers.put("PLR_ROLE", config.getString(env + DASH + HEADER_PLR_ROLE));
        headers.put("OrganizationId", config.getString(env + DASH + HEADER_ORG_ID));
        headers.put("Host", hostHeader);
        return http.baseUrl(rootUrl).headers(headers);
    }

    protected ChainBuilder buildFHIRChain(FeederBuilder<Object> feeder, String scenarioName, String endpoint, String env) {
        String token = checkAccessToken(env);
        return repeat(currentiterations, "n")
                .on(feed(feeder)
                        .exec(session -> {
                            Session newSession = session.set("headerAuthorization", ("Bearer " + token));
                            newSession = newSession.set("headerSender", "MOH");
                            newSession = newSession.set("headerFrom", "PLRTestHarness"); //are these dynamic? We didn't put the headers column in the messages table, we need to put that back if so
                            newSession = newSession.set("messageSendtime", new Timestamp(System.currentTimeMillis()));
                            return newSession;
                        })
                        .exec(http(scenarioName).get(endpoint + "#{PROVIDER_CHID}")
                                .header("Authorization", session -> session.get("headerAuthorization"))
                                .check(status().saveAs("httpStatus"))
                                .check(bodyString().saveAs("GDResponse"))
                                .check(responseTimeInMillis().saveAs("GDResponseTime")))
                        .exec(session -> {
                            Timestamp messageReceivedTime = new Timestamp(System.currentTimeMillis());
                            Timestamp messageSendTime = session.get("messageSendtime");
                            String response = session.get("GDResponse");
                            String status = session.getString("httpStatus");
                            Integer responseTime = session.get("GDResponseTime");
                            addResponseData(1, response, responseTime, messageSendTime, messageReceivedTime, status, false);
                            return session;
                        })
                        .pause(Duration.ofMillis(pauseDuration))
                );
    }

    //Currently rounds down, cutting off trailing records selected to be run
    private int getIterations(int recordCount, int userCount) {
        if (userCount >= recordCount) {
            logger.info("getRepeatNumber:{}", recordCount);
            return recordCount;
        }
        logger.info("getRepeatNumber:{}", recordCount / userCount);
        return recordCount / userCount;
    }

    private int getConcurrentUsers(int recordCount, int userCount) {
        if (userCount >= recordCount) {
            return recordCount;
        }
        return userCount;
    }

    public void calculateIterations() {

        concurrentUsers = getConcurrentUsers(totalRecords, totalUsers);
        currentiterations = getIterations(totalRecords, concurrentUsers);
        numberOfRecords = concurrentUsers * currentiterations;
        logger.info("concurrentUsers= %d, numberOfRecords= %d, currentiterations=%d. ", concurrentUsers, numberOfRecords, currentiterations);
    }

    protected String checkAccessToken(String env) {
        Long currentTime = System.currentTimeMillis();

        String token = "";
        try {

            String authResponse = auth.getBearerAuth(env);
            JSONObject json = new JSONObject(authResponse);
            String accessToken = json.getString("access_token");
            Long tokenExpiryTime = Long.valueOf(json.getInt("expires_in")) * msToSec;
            Long newExpiry = currentTime + tokenExpiryTime;
            token = accessToken;
            logger.debug("Token expires: {}", newExpiry);
            logger.info("Token expires: {}", newExpiry);
        } catch (Exception e) {
            logger.error("Error fetching token!", e);
        }
        return token;
    }

    protected void addResponseData(int msgId, String response, Integer responseTime, Timestamp messageSendTime, Timestamp messageReceivedTime, String status, boolean asyncMsg) {

        MessageResponse responseItem = new MessageResponse();
        responseItem.setMessageReceived(messageReceivedTime);
        responseItem.setMessageSent(messageSendTime);
        responseItem.setMsResponseTime(responseTime);
        responseItem.setResponse(response);
        responseItem.setStatusCode(status);
        responseItem.setMsgId(msgId);
        String URL = new JSONObject(response).getJSONArray("entry").getJSONObject(0).getString("fullUrl");
        logger.info(URL);
        if (!asyncMsg) {
            syncResponses.add(responseItem);
        } else {

            if (MSG_SPEC.equals(FHIR)) {
                JSONObject responseJson = new JSONObject(response);

                String responseCode = responseJson.getJSONArray("entry").getJSONObject(1)
                        .getJSONObject("resource").getJSONArray("issue")
                        .getJSONObject(0).getJSONObject("details")
                        .getJSONArray("coding").getJSONObject(0).getString("code");
                // if(responseCode.equals("BCHCIM.RP.0.0007")){ //or other good ones
                if (responseCode.equals("GRS.SYS.UNK.QRY.0.0.7067")) {
                    String fullUrl = responseJson.getJSONArray("entry").getJSONObject(1).getString("fullUrl");
                    String[] fullUrlbits = fullUrl.split(":");
                    String responseGuid = fullUrlbits[fullUrlbits.length - 1].replace(DASH, "");
                    responseItem.setResponseGuid(responseGuid);
                }
            } else { //HL7v3
                //TODO
            }
            syncResponses.add(responseItem);
            asyncResponses.add(responseItem);
            //start monitoring filesystem if not already
        }
        logger.debug("Response updated {}: {}", msgId, responseTime);
    }

    protected int calculateNumberOfRecords(int dbRecords, int configuredRecords) {
        logger.info("{} messages in db selection, {} messages configured", dbRecords, configuredRecords);
        int numRecords = 0;
        if (configuredRecords < 0) {
            numRecords = dbRecords;
        }
        if (configuredRecords > 0 && configuredRecords <= dbRecords) {
            numRecords = configuredRecords;
        } else if (configuredRecords > 0 && configuredRecords > dbRecords) {
            numRecords = dbRecords;
        }
        logger.info("Number of records: " + numRecords);
        return numRecords;
    }


    public void commonPostJobTasks() {
        logger.info("Simulation Finished");
    }


    //Override in each sub method
    protected String getResponseCode(MessageResponse response) {
        return "";
    }

    protected String getResponseCodeFHIR(MessageResponse messageResponse) {
        if (!messageResponse.getStatusCode().equals("200") && (messageResponse.getResponse() == null || messageResponse.getResponse().isEmpty())) {
            logger.error("No response, returning http code");
            return messageResponse.getStatusCode();
        }
        JSONObject responseJson = new JSONObject(messageResponse.getResponse());
        String responseCode = responseJson.getJSONArray("entry").getJSONObject(1)
                .getJSONObject("resource").getJSONArray("issue")
                .getJSONObject(0).getJSONObject("details")
                .getJSONArray("coding").getJSONObject(0).getString("code");
        return responseCode;
    }


    protected FeederBuilder<Object> setFeeder(String sql, String spec, String env) {
        if (numberOfRecords == null) {
            numberOfRecords = -1;
        }
        logger.info("@@@@@@ ENV : {}", env);

        dbURL = config.getString(env + DASH + PLR_DB_URL);
        dbUser = config.getString(env + DASH + PLR_DB_USER);
        dbPwd = config.getString(env + DASH + PLR_DB_PASSWORD);
        if (numberOfRecords > 0) {
            sql += "   FETCH FIRST " + numberOfRecords.toString() + " ROWS ONLY ";
        }
        logger.info(sql);
        FeederBuilder<Object> feeder = jdbcFeeder(dbURL, dbUser, dbPwd, sql).queue();
        dbRecordsCount = calculateNumberOfRecords(feeder.recordsCount(), numberOfRecords);
        logger.info("Feeder Records Count: " + feeder.recordsCount());
        logger.info("DB Records Count: " + dbRecordsCount);
        return feeder;
    }

}
