package ca.bc.gov.health.test.model;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class ScenarioBundle {

    private int concurrentUsers;
    private ScenarioBuilder scenario;
    private HttpProtocolBuilder httpProtocol;

    public int getConcurrentUsers() {
        return concurrentUsers;
    }
    public void setConcurrentUsers(int concurrentUsers) {
        this.concurrentUsers = concurrentUsers;
    }
    public ScenarioBuilder getScenario() {
        return scenario;
    }
    public void setScenario(ScenarioBuilder scenario) {
        this.scenario = scenario;
    }
    public HttpProtocolBuilder getHttpProtocol() {
        return httpProtocol;
    }
    public void setHttpProtocol(HttpProtocolBuilder httpProtocol) {
        this.httpProtocol = httpProtocol;
    }
    
}
