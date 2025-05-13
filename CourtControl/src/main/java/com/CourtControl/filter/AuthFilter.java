package com.CourtControl.filter;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.service.LoginService;
import com.CourtControl.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/logout"})
public class AuthFilter implements Filter {

    private final LoginService loginService;

    public AuthFilter() {
        this.loginService = new LoginService();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String loginURI = contextPath + "/login";
        String logoutURI = contextPath + "/logout";
        String logoutJspURI = contextPath + "/logout.jsp";
        boolean isLoginRequest = requestURI.equals(loginURI);
        boolean isLogoutRequest = requestURI.equals(logoutURI);
        boolean isLogoutJspRequest = requestURI.equals(logoutJspURI);

        if (isLogoutRequest) {
            // Handle logout
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // Clear the role cookie
            CookieUtil.removeCookie(httpResponse, "role");
            // Forward to logout.jsp
            httpRequest.getRequestDispatcher("/WEB-INF/pages/logout.jsp").forward(httpRequest, httpResponse);
            return;
        }

        if (isLogoutJspRequest) {
            // Allow access to logout.jsp without authentication
            httpRequest.getRequestDispatcher("/WEB-INF/pages/logout.jsp").forward(httpRequest, httpResponse);
            return;
        }

        // Check if the user is authenticated via session
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = (session != null && session.getAttribute("user") != null && session.getAttribute("userId") != null);

        // Check the role cookie for admin access
        boolean isAdmin = false;
        if (isAuthenticated) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("role".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                        isAdmin = true;
                        break;
                    }
                }
            }
        }

        if (isAuthenticated && isAdmin) {
            // User is authenticated and has admin role, allow access
            chain.doFilter(request, response);
        } else if (isAuthenticated && !isAdmin) {
            // User is authenticated but not an admin, redirect to /booking
            httpResponse.sendRedirect(contextPath + "/booking");
        } else if (isLoginRequest) {
            // Allow login page access
            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to login
            httpRequest.setAttribute("errorMessage", "Please log in to access this page.");
            httpRequest.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}