package com.campuscycle.filter;

import com.campuscycle.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/home", "/login", "/register", "/forgot-password", "/reset-password",
            "/listings", "/listing", "/sustainability", "/health", "/assets/", "/css/", "/uploads/", "/error"
    );

    private static final Set<String> PROTECTED_PREFIXES = Set.of(
            "/dashboard", "/profile", "/listing/create", "/wishlist",
            "/barter", "/chat", "/report", "/logout"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (isPublic(path) || path.startsWith("/admin")) {
            chain.doFilter(request, response);
            return;
        }

        if (isProtected(path) && !SessionUtil.isLoggedIn(req)) {
            res.sendRedirect(req.getContextPath() + "/login?redirect=" + path);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        if (PUBLIC_PATHS.contains(path)) return true;
        for (String prefix : PUBLIC_PATHS) {
            if (prefix.endsWith("/") && path.startsWith(prefix)) return true;
        }
        return false;
    }

    private boolean isProtected(String path) {
        for (String prefix : PROTECTED_PREFIXES) {
            if (path.equals(prefix) || path.startsWith(prefix + "/")) return true;
        }
        return false;
    }
}
