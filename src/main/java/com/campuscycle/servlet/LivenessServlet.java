package com.campuscycle.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Render liveness probe. This must not depend on Neon so a database outage
 * does not cause Render to restart a healthy Tomcat process in a loop.
 */
@WebServlet("/health/live")
public class LivenessServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\":\"ok\",\"service\":\"alive\"}");
    }
}
