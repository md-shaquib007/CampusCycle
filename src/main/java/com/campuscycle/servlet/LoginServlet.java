package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.AuthService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Optional<User> userOpt = authService.login(email, password);
            if (userOpt.isEmpty()) {
                req.setAttribute("error", "Invalid credentials or account not verified.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                return;
            }
            SessionUtil.setUser(req, userOpt.get());
            String redirect = req.getParameter("redirect");
            if (redirect != null && !redirect.isBlank() && redirect.startsWith("/") && !redirect.startsWith("//")) {
                resp.sendRedirect(req.getContextPath() + redirect);
            } else if (userOpt.get().isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Login failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
