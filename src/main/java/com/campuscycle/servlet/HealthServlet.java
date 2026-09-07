package com.campuscycle.servlet;

import com.campuscycle.util.DBConnection;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/health"})
public class HealthServlet extends HttpServlet {

    private static final long START_TIME = System.currentTimeMillis();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "CampusCycle Marketplace");
        health.put("timestamp", Instant.now().toString());
        health.put("uptimeMs", System.currentTimeMillis() - START_TIME);

        Map<String, Object> details = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                details.put("database", Map.of("status", "UP", "database", conn.getMetaData().getDatabaseProductName()));
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            details.put("database", Map.of("status", "DOWN", "error", e.getMessage()));
        }

        Runtime runtime = Runtime.getRuntime();
        details.put("memory", Map.of(
                "totalMB", runtime.totalMemory() / (1024 * 1024),
                "freeMB", runtime.freeMemory() / (1024 * 1024),
                "usedMB", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024),
                "maxMB", runtime.maxMemory() / (1024 * 1024)
        ));

        health.put("components", details);

        resp.setContentType("application/json");
        resp.setStatus("UP".equals(health.get("status")) ? 200 : 503);
        resp.getWriter().write(gson.toJson(health));
    }
}
