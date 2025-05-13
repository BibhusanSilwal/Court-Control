package com.CourtControl.controller;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.service.RegisterService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/users", "/admin/update-role" })
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RegisterService registerService = new RegisterService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all users
        List<CustomerModel> userList = registerService.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/WEB-INF/pages/admin/users.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        if ("/admin/update-role".equals(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String newRole = request.getParameter("role");
                if (!"admin".equals(newRole) && !"user".equals(newRole)) {
                    request.setAttribute("errorMessage", "Invalid role specified.");
                } else {
                    boolean success = registerService.updateUserRole(userId, newRole);
                    if (success) {
                        request.setAttribute("successMessage", "User role updated successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update user role.");
                    }
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid user ID.");
            }
        }

        // Refresh the user list after update
        List<CustomerModel> userList = registerService.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/WEB-INF/pages/admin/users.jsp").forward(request, response);
    }
}