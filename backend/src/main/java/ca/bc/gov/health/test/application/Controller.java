package ca.bc.gov.health.test.application;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
   
    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck(@RequestParam Map<String, String> queryParameters){
        logger.info("Health Check - Status OK");
        return ResponseEntity.ok("{\"Health Check - Status OK\"}");
    }

    @GetMapping("/getFHIRQuery")
    public ResponseEntity<String> getDemoFHIR(@RequestParam Map<String, String> queryParameters){
   		int status = process(queryParameters, "ca.bc.gov.health.test.gatling.simulation.FHIR.FHIRQuery");
        return ResponseEntity.ok("{\"status\":\"providers processed\", \"code\":" + status + "}");
    }
    @PostMapping("/providerByIdFHIR")
    public ResponseEntity<String> providerByIdFHIR(@RequestParam Map<String, String> queryParameters){
   		int status = process(queryParameters, "ca.bc.gov.health.test.gatling.simulation.FHIR.FHIRQuery");
        return ResponseEntity.ok("{\"status\":\"providers processed\", \"code\":" + status + "}");

    }
    
	public int process(Map<String, String> queryParameters, String simulation){
		logger.info("Starting getFHIRQuery Simulation");
        return MavenInvoker.runMavenCommand(simulation, queryParameters);
	}

	

  
}
