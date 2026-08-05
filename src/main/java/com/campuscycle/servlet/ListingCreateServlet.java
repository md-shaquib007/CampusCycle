package com.campuscycle.servlet;

import com.campuscycle.model.ListingType;
import com.campuscycle.model.User;
import com.campuscycle.service.ListingService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/listing/create")
public class ListingCreateServlet extends HttpServlet {
    private final ListingService listingService = new ListingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("categories", listingService.getCategories());
            req.setAttribute("listingTypes", ListingType.values());
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load form data.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/listing-create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            String error = listingService.createFromRequest(req, getServletContext(), user.getId());
            if (error != null) {
                req.setAttribute("error", error);
                req.setAttribute("categories", listingService.getCategories());
                req.setAttribute("listingTypes", ListingType.values());
                req.getRequestDispatcher("/WEB-INF/jsp/listing-create.jsp").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("flash", "Listing submitted! It will appear after admin approval.");
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to create listing.");
            req.getRequestDispatcher("/WEB-INF/jsp/listing-create.jsp").forward(req, resp);
        }
    }
}
