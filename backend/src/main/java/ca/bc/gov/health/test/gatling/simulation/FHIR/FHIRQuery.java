package ca.bc.gov.health.test.gatling.simulation.FHIR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.health.test.common.HSAUrlEnum;
import ca.bc.gov.health.test.common.PlrNamingSystemEnum;
import ca.bc.gov.health.test.common.QueryEnum;
import ca.bc.gov.health.test.gatling.simulation.BaseSimulation;
import ca.bc.gov.health.test.model.MessageResponse;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import static io.gatling.javaapi.core.CoreDsl.*;
import io.gatling.javaapi.core.*;

import java.util.List;
import java.util.ArrayList;

public class FHIRQuery extends BaseSimulation {
    private static final Logger logger = LoggerFactory.getLogger(FHIRQuery.class);

    private String ENDPOINT_URL;
    private String env = "";
    String MSG_SPEC = "FHIR";
    boolean hasIPC = false;
    boolean hasCPN = false;
    String type = "";
    protected List<MessageResponse> syncResponses = new ArrayList<MessageResponse>();
    protected List<MessageResponse> asyncResponses = new ArrayList<MessageResponse>();

    protected final int msToSec = 1000;

    private final String SIMULATION_TITLE = "RESTFUL GET - FHIR Query for Providers";

    // private final String MSG_TYPE = "FHIR RESTFUL GET";

    // Initialization Method - runs before even the @before method
    {
        logger.info("Starting simulation {}", SIMULATION_TITLE);

        try {
            try {

                env = System.getProperty("environment");
                MSG_SPEC = System.getProperty("spec");
                totalUsers = Integer.getInteger("users", 1);
                totalRecords = Integer.getInteger("records", 0);
                pauseDuration = Integer.getInteger("pause", 1);
                hasIPC = Boolean.getBoolean("hasIPC");
                if (hasIPC)
                    type = "IPC";
                else
                    type = "CPN";
                logger.info("Total Records: {}", totalRecords);
                logger.info("Total Users: {}", totalUsers);
                logger.info("Pause Duration : {}", pauseDuration);
                logger.info("Type IPC: {}", hasIPC);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error getting parameters: {}", e);
            }

            if (totalRecords > 0) {

                ENDPOINT_URL = HSAUrlEnum.findByIdentifierType("PROVIDER_ID").url()
                        + PlrNamingSystemEnum.findByIdentifierType(type).namingSystemURL() + "|";
                System.out.println("ENDPOINT_URL: " + ENDPOINT_URL);
                auth.init(env);
                pauseDuration = 1;
                calculateIterations();
                invoke();
            } else {
                logger.error("Number of Records is 0. Exiting Simulation");
            }

        } catch (Exception e) {
            logger.error("Error intializing simulation: {}", e);
        }

    }

    public void invoke() {
        FeederBuilder<Object> feeder = setFeeder(getQuery(), MSG_SPEC);
        ChainBuilder chainBuilder = buildFHIRChain(feeder, SIMULATION_TITLE, ENDPOINT_URL);
        HttpProtocolBuilder httpProtocol = setBaseHttpProtocol(env);
        ScenarioBuilder scenario = scenario(SIMULATION_TITLE).exec(chainBuilder);
        setUp(scenario.injectOpen(atOnceUsers(concurrentUsers))).protocols(httpProtocol);
    }

    // @Override
    public void after() {
        // commonPostJobTasks();
    }

    public String getQuery() {
        String sql = "";
        try {
            sql = String.format(QueryEnum.findByIdentifier("PROVIDER_ID").query(), type);
            System.out.println("sql: " + sql);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getQuery()", e);
        }
        logger.info(sql);
        return sql;

    }

}
