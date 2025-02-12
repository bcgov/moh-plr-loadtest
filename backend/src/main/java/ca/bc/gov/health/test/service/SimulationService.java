package ca.bc.gov.health.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    public int process(Map<String, String> queryParameters, String simulation){
        logger.info("Starting providerByIdFHIR Simulation");
        return MavenInvokerService.runMavenCommand(simulation, queryParameters);
    }

}
