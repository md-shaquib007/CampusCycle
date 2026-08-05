package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.AuthService;
import com.campuscycle.service.ListingService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final ListingService listingService = new ListingService();
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            req.setAttribute("myListings", listingService.getUserListings(user.getId()));
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load profile.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        user.setName(req.getParameter("name"));
        user.setDepartment(req.getParameter("department"));
        user.setSemester(req.getParameter("semester"));
        user.setHostel(req.getParameter("hostel"));
        user.setPhone(req.getParameter("phone"));
        try {
            authService.updateProfile(user);
            SessionUtil.setUser(req, user);
            req.getSession().setAttribute("flash", "Profile updated.");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", "Profile update failed.");
        }
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
