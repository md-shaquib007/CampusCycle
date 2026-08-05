package com.campuscycle.servlet;

import com.campuscycle.service.AdminService;
import com.campuscycle.service.ListingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();
    private final ListingService listingService = new ListingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("pendingUsers", adminService.getPendingUsers());
            req.setAttribute("pendingListings", adminService.getPendingListings());
            req.setAttribute("pendingReports", adminService.getPendingReports());
            req.setAttribute("stats", adminService.getStats());
            req.setAttribute("allStudents", adminService.getAllStudents());
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load admin dashboard.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        try {
            switch (action != null ? action : "") {
                case "verify" -> adminService.verifyUser(Integer.parseInt(req.getParameter("userId")));
                case "suspend" -> adminService.suspendUser(Integer.parseInt(req.getParameter("userId")), true);
                case "unsuspend" -> adminService.suspendUser(Integer.parseInt(req.getParameter("userId")), false);
                case "approveListing" -> listingService.approve(Integer.parseInt(req.getParameter("listingId")));
                case "rejectListing" -> listingService.reject(Integer.parseInt(req.getParameter("listingId")));
                case "resolveReport" -> adminService.resolveReport(
                        Integer.parseInt(req.getParameter("reportId")),
                        req.getParameter("status"),
                        req.getParameter("adminNote"));
                default -> {}
            }
            req.getSession().setAttribute("flash", "Action completed.");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", "Admin action failed.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
