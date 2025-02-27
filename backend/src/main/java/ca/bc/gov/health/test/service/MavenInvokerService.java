package ca.bc.gov.health.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import static ca.bc.gov.health.test.constants.SimulationConstants.PROVIDER_QUERY_DESC;

public class MavenInvokerService {
    private static final Logger logger = LoggerFactory.getLogger(MavenInvokerService.class);

    public static int runMavenCommand(String simulation, Map<String, String> queryParameters) {
        int exitCode = -1;
        StringBuilder command = new StringBuilder();

        try {
            // String prefix ="v2.gatling."+ RunIdConverter.getRoundStartTime(5) +".`localhost:3000`"; //".`hostname`";
            // String command = "GATLING_PREFIX="+prefix+" mvn gatling:test -Dgatling.simulationClass="+simulation+" "+getParameters(queryParameters);
            if (isValidParameter(simulation)) {
                // set parameters to set in env
                String addParameters = getParameters(queryParameters);
                String description = getDescription(queryParameters);

                command.append("mvn gatling:test -Dgatling.runDescription=")
                        .append(description)
                        .append(" -Dgatling.simulationClass=")
                        .append(sanitize(simulation))
                        .append(" ").append(addParameters);

                ProcessBuilder processBuilder = getProcess(command.toString());
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }

                exitCode = process.waitFor();
                logger.info("\nExited with code : {}", exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    static String getParameters(Map<String, String> queryParameters) {
        StringBuilder parameters = new StringBuilder();
        if (!CollectionUtils.isEmpty(queryParameters)) {
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                if (isValidParameter(entry.getKey()) && isValidParameter(entry.getValue())) {
                    String key = sanitize(entry.getKey());
                    String value = sanitize(entry.getValue());
                    logger.info("Key: {}, Value: {}", key, value);
                    parameters.append("-D").append(key).append("=").append(value).append(" ");
                } else {
                    logger.warn("Invalid parameter: Key: {}, Value: {}", entry.getKey(), entry.getValue());
                }
            }
        }
        return parameters.toString();
    }

    static boolean isValidParameter(String param) {
        return param != null && param.matches("^[a-zA-Z0-9._-]+$");
    }

    private static String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9._-]", "");
    }

    static boolean isValidCommand(String command) {
        return command != null && command.contains("mvn gatling:test -Dgatling.");
    }

    static String getDescription(Map<String, String> queryParameters) {
        return new StringBuilder()
                .append("\"").append(PROVIDER_QUERY_DESC)
                .append(" hasCPN=").append(queryParameters.get("hasCPN"))
                .append(" hasIPC=").append(queryParameters.get("hasIPC"))
                .append(" users=").append(queryParameters.get("users"))
                .append(" records=").append(queryParameters.get("records"))
                .append(" pause=").append(queryParameters.get("pause"))
                .append(" environment=").append(queryParameters.get("environment"))
                .append(" spec=").append(queryParameters.get("spec"))
                .append("\"").toString();
    }

    static ProcessBuilder getProcess(String command) {
        boolean isWindows = false;
        ProcessBuilder processBuilder = new ProcessBuilder();
        logger.info("command : {}", command);
        if (isValidCommand(command)) {
            Map<String, String> env = processBuilder.environment();

            if (env.containsKey("SystemRoot") && env.get("SystemRoot") != null && env.get("SystemRoot").substring(3).equalsIgnoreCase("WINDOWS")) {
                isWindows = true;
            }
            if (isWindows) {
                logger.info("EXECUTING in Windows");
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                logger.info("EXECUTING in Linux");
                processBuilder.command("/bin/bash", "-c", command);
            }
        } else {
            logger.error("Invalid command: {}", command);
        }
        return processBuilder;
    }

}

