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
        if ("dbUrl".equals(key) || "db.url".equals(key)) {
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

        // Direct environment variable check (exact key, e.g., neonDbUrl, dbUrl, uploadPath, uploadMaxSize, collegeEmailDomains)
        String directEnv = System.getenv(key);
        if (directEnv != null && !directEnv.isBlank()) {
            return directEnv;
        }

        // Upper-snake case environment variable check (e.g., UPLOAD_PATH, UPLOAD_MAX_SIZE, COLLEGE_EMAIL_DOMAINS)
        String envKey = toEnvironmentKey(key);
        String environmentValue = System.getenv(envKey);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        // Properties file check (try exact key first, then fallback alias)
        String propVal = PROPS.getProperty(key);
        if (propVal != null && !propVal.isBlank()) {
            return propVal;
        }

        String altKey = getAlternateKey(key);
        if (altKey != null) {
            String altEnv = System.getenv(altKey);
            if (altEnv != null && !altEnv.isBlank()) {
                return altEnv;
            }
            String altEnvUpper = System.getenv(toEnvironmentKey(altKey));
            if (altEnvUpper != null && !altEnvUpper.isBlank()) {
                return altEnvUpper;
            }
            return PROPS.getProperty(altKey);
        }

        return null;
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    private static String toEnvironmentKey(String key) {
        // Convert camelCase or dot-separated to UPPER_SNAKE_CASE
        // e.g., uploadPath -> UPLOAD_PATH, upload.maxSize -> UPLOAD_MAX_SIZE
        String regexConverted = key.replaceAll("([a-z])([A-Z])", "$1_$2");
        return regexConverted.replace('.', '_').replace('-', '_').toUpperCase();
    }

    private static String getAlternateKey(String key) {
        switch (key) {
            case "uploadPath": return "upload.path";
            case "upload.path": return "uploadPath";
            case "uploadMaxSize": return "upload.maxSize";
            case "upload.maxSize": return "uploadMaxSize";
            case "collegeEmailDomains": return "college.email.domains";
            case "college.email.domains": return "collegeEmailDomains";
            case "dbUrl": return "db.url";
            case "db.url": return "dbUrl";
            case "dbUsername": return "db.username";
            case "db.username": return "dbUsername";
            case "dbPassword": return "db.password";
            case "db.password": return "dbPassword";
            case "dbDriver": return "db.driver";
            case "db.driver": return "dbDriver";
            default: return null;
        }
    }
}
