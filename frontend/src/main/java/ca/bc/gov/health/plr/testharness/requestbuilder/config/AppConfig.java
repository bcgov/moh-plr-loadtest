package ca.bc.gov.health.plr.testharness.requestbuilder.config;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;

/**
 * @author CGI
 * */
public class AppConfig {
    private static final Configuration config;

    static {
        try {
            config = initConfig();
        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    /**
     * initializes the configuration from three sources - application properties, env variables and system properties
     *
     * @return Configuration
     * @throws ConfigurationException when any error occurs initializing the config.
     */
    private static Configuration initConfig() throws ConfigurationException {
        Configuration configProperties =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(
                                new Parameters()
                                        .properties()
                                        .setLocationStrategy(new ClasspathLocationStrategy())
                                        .setFileName("application.properties"))
                        .getConfiguration();

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(configProperties);
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new EnvironmentConfiguration());
        return config;

    }

}
