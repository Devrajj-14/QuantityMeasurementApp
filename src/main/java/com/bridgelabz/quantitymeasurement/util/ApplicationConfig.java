package com.bridgelabz.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and exposes application configuration from application.properties.
 * Supports system property overrides (e.g., -Drepository.type=cache).
 */
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final String CONFIG_FILE = "application.properties";

    private final Properties properties;

    public ApplicationConfig() {
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.warn("Configuration file '{}' not found on classpath. Using defaults.", CONFIG_FILE);
                setDefaults();
                return;
            }
            properties.load(input);
            logger.info("Loaded configuration from '{}'", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Error loading configuration file: {}", e.getMessage());
            setDefaults();
        }
    }

    private void setDefaults() {
        properties.setProperty("repository.type", "cache");
        properties.setProperty("db.url", "jdbc:h2:mem:quantitydb");
        properties.setProperty("db.username", "sa");
        properties.setProperty("db.password", "");
        properties.setProperty("db.pool.size", "5");
    }

    /**
     * Get a property value, with system property override support.
     */
    public String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public String getRepositoryType() {
        return getProperty("repository.type", "cache");
    }

    public String getDbUrl() {
        return getProperty("db.url", "jdbc:h2:mem:quantitydb");
    }

    public String getDbUsername() {
        return getProperty("db.username", "sa");
    }

    public String getDbPassword() {
        return getProperty("db.password", "");
    }

    public int getPoolSize() {
        try {
            return Integer.parseInt(getProperty("db.pool.size", "5"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid pool size in config, using default: 5");
            return 5;
        }
    }
}
