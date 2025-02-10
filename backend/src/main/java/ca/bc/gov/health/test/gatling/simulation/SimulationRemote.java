package ca.bc.gov.health.test.gatling.simulation;

import ca.bc.gov.health.test.config.SimulationConfig;
import org.apache.commons.configuration2.Configuration;

public interface SimulationRemote {
    Configuration config = SimulationConfig.getConfig();
}
