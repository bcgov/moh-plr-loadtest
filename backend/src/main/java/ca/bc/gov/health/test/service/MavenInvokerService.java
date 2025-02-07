package ca.bc.gov.health.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class MavenInvokerService {
    private static final Logger logger = LoggerFactory.getLogger(MavenInvokerService.class);

    public static int runMavenCommand(String simulation, Map<String, String> queryParameters) {
        int exitCode = -1;
        try {
            // String prefix ="v2.gatling."+ RunIdConverter.getRoundStartTime(5) +".`localhost:3000`"; //".`hostname`";
            // String command = "GATLING_PREFIX="+prefix+" mvn gatling:test -Dgatling.simulationClass="+simulation+" "+getParameters(queryParameters);
            String command = "mvn gatling:test -Dgatling.simulationClass=" + simulation + " " + getParameters(queryParameters);
            //getParameters(queryParameters);
            ProcessBuilder processBuilder = getProcess(command);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }

            exitCode = process.waitFor();
            logger.info("\nExited with code : {}", exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    static String getParameters(Map<String, String> queryParameters) {
        String parameters = "";
        if (!CollectionUtils.isEmpty(queryParameters)) {
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                logger.info("Key: {}, Value: {}", entry.getKey(), entry.getValue());
                parameters += "-D" + entry.getKey() + "=" + entry.getValue() + " ";
            }
        }
        return parameters;
    }

    static ProcessBuilder getProcess(String command) {
        boolean isWindows = false;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (command != null) {
            Map<String, String> env = processBuilder.environment();

            if (env.containsKey("SystemRoot") && env.get("SystemRoot") != null
                    && env.get("SystemRoot").substring(3).equalsIgnoreCase("WINDOWS")) {
                isWindows = true;
            }
            if (isWindows) {
                logger.info("EXECUTING in Windows");
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                logger.info("EXECUTING in Linux");
                processBuilder.command("/bin/bash", "-c", command);
            }
        }
        return processBuilder;
    }

}

