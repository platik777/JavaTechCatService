package ru.platik777.apiservice.security;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String PROPERTIES_FILE = "config.properties";
    private static Properties properties;

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties = new Properties();
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String getSecretKey() {
        return properties.getProperty("secret_key");
    }
}