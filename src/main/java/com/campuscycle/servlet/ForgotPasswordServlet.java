package com.campuscycle.servlet;

import com.campuscycle.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/forgot-password", "/reset-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/reset-password".equals(req.getServletPath())) {
            req.setAttribute("token", req.getParameter("token"));
            req.getRequestDispatcher("/WEB-INF/jsp/reset-password.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/forgot-password.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/reset-password".equals(path)) {
            String token = req.getParameter("token");
            String password = req.getParameter("password");
            try {
                String error = authService.resetPassword(token, password);
                if (error != null) {
                    req.setAttribute("error", error);
                    req.setAttribute("token", token);
                    req.getRequestDispatcher("/WEB-INF/jsp/reset-password.jsp").forward(req, resp);
                    return;
                }
                req.setAttribute("success", "Password reset successful. You can now login.");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("error", "Reset failed.");
                req.getRequestDispatcher("/WEB-INF/jsp/reset-password.jsp").forward(req, resp);
            }
        } else {
            String email = req.getParameter("email");
            try {
                String token = authService.requestPasswordReset(email);
                req.setAttribute("success", "Reset token generated (demo): " + token);
                req.setAttribute("resetLink", req.getContextPath() + "/reset-password?token=" + token);
            } catch (Exception e) {
                req.setAttribute("error", "Request failed.");
            }
            req.getRequestDispatcher("/WEB-INF/jsp/forgot-password.jsp").forward(req, resp);
        }
    }
}
