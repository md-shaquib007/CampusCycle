package com.campuscycle.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load db.properties: " + e.getMessage());
        }
    }

    private AppConfig() {}

    public static String get(String key) {
        if ("db.url".equals(key)) {
            String neonUrl = System.getenv("neonDbUrl");
            if (neonUrl == null || neonUrl.isBlank()) {
                neonUrl = System.getenv("NEON_DB_URL");
            }
            if (neonUrl == null || neonUrl.isBlank()) {
                neonUrl = PROPS.getProperty("neonDbUrl");
            }
            if (neonUrl != null && !neonUrl.isBlank()) {
                return neonUrl;
            }
        }
        String environmentValue = System.getenv(toEnvironmentKey(key));
        return environmentValue != null && !environmentValue.isBlank()
                ? environmentValue
                : PROPS.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    private static String toEnvironmentKey(String key) {
        return key.replace('.', '_').replace('-', '_').toUpperCase();
    }
}
