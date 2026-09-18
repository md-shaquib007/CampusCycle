package com.campuscycle.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        String driver = AppConfig.get("dbDriver", "org.postgresql.Driver");
        String configuredUrl = cleanConfiguredUrl(AppConfig.get("dbUrl"));
        String url = normalizeJdbcUrl(configuredUrl);
        String username = AppConfig.get("dbUsername");
        String password = AppConfig.get("dbPassword", "");
        if (url == null || url.isBlank()) {
            throw new SQLException("neonDbUrl/dbUrl is not configured");
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL driver not found", e);
        }
        if (username == null || username.isBlank()
                || (configuredUrl != null && configuredUrl.startsWith("postgresql://"))) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, username, password);
    }

    static String normalizeJdbcUrl(String url) {
        if (url == null || !url.startsWith("postgresql://")) {
            return url;
        }

        try {
            URI uri = URI.create(url);
            StringBuilder jdbc = new StringBuilder("jdbc:postgresql://");
            jdbc.append(uri.getHost());
            if (uri.getPort() != -1) {
                jdbc.append(':').append(uri.getPort());
            }
            if (uri.getRawPath() != null) {
                jdbc.append(uri.getRawPath());
            }

            String query = uri.getRawQuery();
            String userInfo = uri.getRawUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                int separator = userInfo.indexOf(':');
                if (separator > 0) {
                    String user = decode(userInfo.substring(0, separator));
                    String password = decode(userInfo.substring(separator + 1));
                    query = appendQuery(query, "user=" + encode(user));
                    query = appendQuery(query, "password=" + encode(password));
                }
            }
            if (query != null && !query.isBlank()) {
                jdbc.append('?').append(query);
            }
            return jdbc.toString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid PostgreSQL connection URL", e);
        }
    }

    private static String cleanConfiguredUrl(String url) {
        if (url == null) {
            return null;
        }
        String cleaned = url.trim();
        if (cleaned.startsWith("neonDbUrl=")) {
            cleaned = cleaned.substring("neonDbUrl=".length()).trim();
        }
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        return cleaned;
    }

    private static String appendQuery(String query, String parameter) {
        return query == null || query.isBlank() ? parameter : query + "&" + parameter;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
