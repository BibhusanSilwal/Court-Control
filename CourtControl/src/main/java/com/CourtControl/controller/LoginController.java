package com.CourtControl.controller;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.service.LoginService;
import com.CourtControl.util.CookieUtil;
import com.CourtControl.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final LoginService loginService;

    public LoginController() {
        super();
        this.loginService = new LoginService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        CustomerModel user = null; // Initialize to null
        try {
            user = loginService.loginUser(username, password);
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
            request.setAttribute("errorMessage", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            return; // Exit the method after forwarding
        }

        if (user != null) {
            // Set session attributes
            SessionUtil.setAttribute(request, "user", user);
            SessionUtil.setAttribute(request, "userId", String.valueOf(user.getUserId()));

            // Set role cookie
            boolean isAdmin = user.isAdmin();
            if (isAdmin) {
                CookieUtil.addCookie(response, "role", "admin", 5 * 24 * 60 * 60); // 5 days
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                CookieUtil.addCookie(response, "role", "user", 5 * 24 * 60 * 60); // 5 days
                response.sendRedirect(request.getContextPath() + "/booking");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
        }
    }
}