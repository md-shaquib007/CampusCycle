package com.campuscycle.servlet;

import com.campuscycle.util.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT to_regclass('public.users'), "
                             + "to_regclass('public.listings'), "
                             + "to_regclass('public.transactions')");
             ResultSet result = statement.executeQuery()) {
            if (!result.next() || result.getString(1) == null
                    || result.getString(2) == null || result.getString(3) == null) {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                resp.getWriter().write("{\"status\":\"degraded\",\"database\":\"ok\",\"schema\":\"missing\"}");
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"ok\",\"database\":\"ok\",\"schema\":\"ok\"}");
        } catch (Exception e) {
            getServletContext().log("Database health check failed: " + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("{\"status\":\"degraded\",\"database\":\"unavailable\"}");
        }
    }
}
