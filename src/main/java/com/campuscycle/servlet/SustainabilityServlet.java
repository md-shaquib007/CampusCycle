package com.campuscycle.servlet;

import com.campuscycle.dao.SustainabilityDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sustainability")
public class SustainabilityServlet extends HttpServlet {
    private final SustainabilityDAO sustainabilityDAO = new SustainabilityDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("stats", sustainabilityDAO.getStats());
        } catch (Exception e) {
            getServletContext().log("Sustainability data load failed: " + e.getMessage(), e);
            req.setAttribute("error", "Unable to load sustainability data.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/sustainability.jsp").forward(req, resp);
    }
}
