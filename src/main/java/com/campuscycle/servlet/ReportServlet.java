package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.ReportService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private final ReportService reportService = new ReportService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            Integer listingId = parseIntOrNull(req.getParameter("listingId"));
            Integer reportedUserId = parseIntOrNull(req.getParameter("reportedUserId"));
            String reason = req.getParameter("reason");
            String description = req.getParameter("description");

            String error = reportService.submit(user.getId(), listingId, reportedUserId, reason, description);
            if (error != null) {
                req.getSession().setAttribute("flashError", error);
            } else {
                req.getSession().setAttribute("flash", "Report submitted. Admin will review it.");
            }
            if (listingId != null) {
                resp.sendRedirect(req.getContextPath() + "/listing?id=" + listingId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        return Integer.parseInt(value);
    }
}
