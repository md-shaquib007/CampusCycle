package com.campuscycle.servlet;

import com.campuscycle.model.User;
import com.campuscycle.service.ChatService;
import com.campuscycle.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chat/*")
public class ChatServlet extends HttpServlet {
    private final ChatService chatService = new ChatService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            req.setAttribute("requests", chatService.getForUser(user.getId()));
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load chat requests.");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/chat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("send".equals(action)) {
                int listingId = Integer.parseInt(req.getParameter("listingId"));
                String message = req.getParameter("message");
                String error = chatService.sendRequest(user.getId(), listingId, message);
                if (error != null) {
                    req.getSession().setAttribute("flashError", error);
                } else {
                    req.getSession().setAttribute("flash", "Chat request sent!");
                }
                resp.sendRedirect(req.getContextPath() + "/listing?id=" + listingId);
            } else if ("respond".equals(action)) {
                int requestId = Integer.parseInt(req.getParameter("requestId"));
                boolean accept = "accept".equals(req.getParameter("decision"));
                chatService.respond(requestId, user.getId(), accept);
                req.getSession().setAttribute("flash", accept ? "Chat request accepted." : "Chat request rejected.");
                resp.sendRedirect(req.getContextPath() + "/chat");
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/chat");
        }
    }
}
