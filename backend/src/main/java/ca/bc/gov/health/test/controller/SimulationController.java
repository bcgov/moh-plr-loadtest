package ca.bc.gov.health.test.controller;

import ca.bc.gov.health.test.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class SimulationController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    SimulationService service;

    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck(@RequestParam Map<String, String> queryParameters) {
        logger.info("Health Check - Status OK");
        return ResponseEntity.ok("{\"Health Check - Status OK\"}");
    }

    @PostMapping("/providerByIdFHIR")
    public ResponseEntity<String> providerByIdFHIR(@RequestParam Map<String, String> queryParameters) {
        int status = service.process(queryParameters, "ca.bc.gov.health.test.gatling.simulation.FHIR.FHIRQuery");
        return ResponseEntity.ok("{\"status\":\"providers processed\", \"code\":" + status + "}");
    }
    
}
