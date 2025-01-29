package ca.bc.gov.health.test.config;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;

public class SimulationConfig {
    
    private static final Configuration config;

    static {
        try {
            config = initConfig();
        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Configuration getConfig(){
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
