package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.BarterService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/barter/*")
public class BarterServlet extends HttpServlet {
    private final BarterService barterService = new BarterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            req.setAttribute("proposals", barterService.getForUser(user.getId()));
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load barter proposals.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/barter.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("propose".equals(action)) {
                int targetId = Integer.parseInt(req.getParameter("targetListingId"));
                int offeredId = Integer.parseInt(req.getParameter("offeredListingId"));
                String message = req.getParameter("message");
                String error = barterService.propose(user.getId(), targetId, offeredId, message);
                if (error != null) {
                    req.getSession().setAttribute("flashError", error);
                } else {
                    req.getSession().setAttribute("flash", "Barter proposal sent!");
                }
                resp.sendRedirect(req.getContextPath() + "/listing?id=" + targetId);
            } else if ("accept".equals(action)) {
                int proposalId = Integer.parseInt(req.getParameter("proposalId"));
                String error = barterService.accept(proposalId, user.getId());
                if (error != null) {
                    req.getSession().setAttribute("flashError", error);
                } else {
                    req.getSession().setAttribute("flash", "Barter completed successfully!");
                }
                resp.sendRedirect(req.getContextPath() + "/barter");
            } else if ("reject".equals(action)) {
                int proposalId = Integer.parseInt(req.getParameter("proposalId"));
                barterService.reject(proposalId);
                req.getSession().setAttribute("flash", "Proposal rejected.");
                resp.sendRedirect(req.getContextPath() + "/barter");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", "Action failed.");
            resp.sendRedirect(req.getContextPath() + "/barter");
        }
    }
}
