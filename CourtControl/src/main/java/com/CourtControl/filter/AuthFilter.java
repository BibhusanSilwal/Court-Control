package com.CourtControl.filter;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.service.LoginService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = "/admin/*")
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

        HttpSession session = httpRequest.getSession(false);
        String loginURI = httpRequest.getContextPath() + "/login";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);

        // Check if the user is authenticated via session
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
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/booking");
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