package com.campuscycle.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        String driver = AppConfig.get("db.driver", "org.postgresql.Driver");
        String url = normalizeJdbcUrl(AppConfig.get("db.url"));
        String username = AppConfig.get("db.username");
        String password = AppConfig.get("db.password", "");
        if (url == null || url.isBlank()) {
            throw new SQLException("neonDbUrl/db.url is not configured");
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL driver not found", e);
        }
        if (username == null || username.isBlank()) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, username, password);
    }

    private static String normalizeJdbcUrl(String url) {
        if (url != null && url.startsWith("postgresql://")) {
            return "jdbc:" + url;
        }
        return url;
    }
}
