package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.ListingService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final ListingService listingService = new ListingService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            int listingId = Integer.parseInt(req.getParameter("listingId"));
            boolean added = listingService.toggleWishlist(user.getId(), listingId);
            req.getSession().setAttribute("flash", added ? "Added to wishlist!" : "Removed from wishlist.");
            String redirect = req.getParameter("redirect");
            if (redirect != null && redirect.startsWith("/")) {
                resp.sendRedirect(req.getContextPath() + redirect);
            } else {
                resp.sendRedirect(req.getContextPath() + "/listing?id=" + listingId);
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/listings");
        }
    }
}
