package com.campuscycle.servlet;

import com.campuscycle.dao.ListingFilter;
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

@WebServlet(urlPatterns = {"/listings", "/listing"})
public class ListingServlet extends HttpServlet {
    private final ListingService listingService = new ListingService();
    private static final int PAGE_SIZE = 12;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/listing".equals(path)) {
            showDetail(req, resp);
        } else {
            search(req, resp);
        }
    }

    private void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ListingFilter filter = buildFilter(req);
            User user = SessionUtil.getUser(req);
            Integer userId = user != null ? user.getId() : null;

            int total = listingService.count(filter);
            int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

            req.setAttribute("listings", listingService.search(filter, userId));
            req.setAttribute("categories", listingService.getCategories());
            req.setAttribute("listingTypes", ListingType.values());
            req.setAttribute("total", total);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("currentPage", filter.getOffset() / PAGE_SIZE + 1);
            req.setAttribute("filter", filter);
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load listings.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/listings.jsp").forward(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/listings");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            User user = SessionUtil.getUser(req);
            Integer userId = user != null ? user.getId() : null;

            listingService.view(id);
            var listingOpt = listingService.getById(id, userId);
            if (listingOpt.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/listings");
                return;
            }
            req.setAttribute("listing", listingOpt.get());
            if (user != null) {
                req.setAttribute("myListings", listingService.getActiveUserListings(user.getId()));
            }
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load listing.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/listing-detail.jsp").forward(req, resp);
    }

    private ListingFilter buildFilter(HttpServletRequest req) {
        ListingFilter filter = new ListingFilter();
        filter.setQuery(req.getParameter("q"));
        String categoryId = req.getParameter("categoryId");
        if (categoryId != null && !categoryId.isBlank()) {
            filter.setCategoryId(Integer.parseInt(categoryId));
        }
        String type = req.getParameter("type");
        if (type != null && !type.isBlank()) {
            filter.setListingType(ListingType.fromString(type));
        }
        filter.setFreeOnly("true".equals(req.getParameter("free")));
        filter.setUnder500("true".equals(req.getParameter("under500")));
        filter.setHostel(req.getParameter("hostel"));
        filter.setCourse(req.getParameter("course"));
        filter.setSemester(req.getParameter("semester"));
        filter.setSellerName(req.getParameter("seller"));

        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); } catch (NumberFormatException ignored) {}
        }
        filter.setLimit(PAGE_SIZE);
        filter.setOffset((page - 1) * PAGE_SIZE);
        return filter;
    }
}
