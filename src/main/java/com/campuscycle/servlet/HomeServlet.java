package com.campuscycle.servlet;

import com.campuscycle.dao.ListingFilter;
import com.campuscycle.dao.SustainabilityDAO;
import com.campuscycle.model.ListingType;
import com.campuscycle.model.SustainabilityStats;
import com.campuscycle.model.User;
import com.campuscycle.service.ListingService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private final ListingService listingService = new ListingService();
    private final SustainabilityDAO sustainabilityDAO = new SustainabilityDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ListingFilter filter = new ListingFilter();
            filter.setLimit(8);
            filter.setOffset(0);

            User user = SessionUtil.getUser(req);
            Integer userId = user != null ? user.getId() : null;

            req.setAttribute("listings", listingService.search(filter, userId));
            req.setAttribute("categories", listingService.getCategories());
            SustainabilityStats stats = sustainabilityDAO.getStats();
            req.setAttribute("stats", stats);
            req.setAttribute("listingTypes", ListingType.values());
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load marketplace data. Please check database connection.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
