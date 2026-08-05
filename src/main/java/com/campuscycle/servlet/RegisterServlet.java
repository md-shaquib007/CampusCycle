package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setEmail(req.getParameter("email"));
        user.setCollege(req.getParameter("college"));
        user.setDepartment(req.getParameter("department"));
        user.setSemester(req.getParameter("semester"));
        user.setHostel(req.getParameter("hostel"));
        user.setPhone(req.getParameter("phone"));

        String password = req.getParameter("password");

        try {
            String error = authService.register(user, password);
            if (error != null) {
                req.setAttribute("error", error);
                req.setAttribute("user", user);
                req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("success", "Registration successful! Please wait for admin verification before logging in.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Registration failed. Please try again.");
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}
