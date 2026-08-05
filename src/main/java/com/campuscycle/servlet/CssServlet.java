package com.campuscycle.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/assets/style.css")
public class CssServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/css");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "public, max-age=3600");

        try (InputStream css = getServletContext().getResourceAsStream("/css/style.css")) {
            if (css == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            css.transferTo(resp.getOutputStream());
        }
    }
}
