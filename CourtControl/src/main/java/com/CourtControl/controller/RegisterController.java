package com.CourtControl.controller;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.service.RegisterService;
import com.CourtControl.util.CookieUtil;
import com.CourtControl.util.ImageUtil;
import com.CourtControl.util.PasswordUtil;
import com.CourtControl.util.SessionUtil;
import com.CourtControl.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

@WebServlet(asyncSupported = true, urlPatterns = {"/register"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10, // 10MB
                 maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageUtil imageUtil = new ImageUtil();
    private final RegisterService registerService = new RegisterService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Validate form inputs
            String validationMessage = validateRegistrationForm(request);
            if (validationMessage != null) {
                handleError(request, response, validationMessage);
                return;
            }

            // Extract customer model
            CustomerModel customerModel = extractCustomerModel(request);
            CustomerModel registeredUser = registerService.registerUser(customerModel);

            if (registeredUser == null) {
                handleError(request, response, "Could not register your account. Please try again later!");
            } else {
                // Upload image after successful registration
                if (uploadImage(request)) {
                    // Set user and userId in session
                    SessionUtil.setAttribute(request, "user", registeredUser);
                    SessionUtil.setAttribute(request, "userId", String.valueOf(registeredUser.getUserId()));

                    // Set role cookie
                    String role = registeredUser.getRole();
                    CookieUtil.addCookie(response, "role", role, 5 * 24 * 60 * 60); // 5 days

                    // Redirect based on role
                    if ("admin".equals(role)) {
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/booking");
                    }
                } else {
                    handleError(request, response, "Could not upload the image. Please try again later!");
                }
            }
        } catch (Exception e) {
            handleError(request, response, "An unexpected error occurred. Please try again later!");
            e.printStackTrace();
        }
    }

    private String validateRegistrationForm(HttpServletRequest request) throws IOException, ServletException {
        String firstName = request.getParameter("fName");
        String lastName = request.getParameter("lName");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String number = request.getParameter("number");
        String email = request.getParameter("email");

        StringBuilder errorMsg = new StringBuilder();

        if (ValidationUtil.isNullOrEmpty(firstName)) errorMsg.append("First name is required. ");
        if (ValidationUtil.isNullOrEmpty(lastName)) errorMsg.append("Last name is required. ");
        if (ValidationUtil.isNullOrEmpty(userName)) errorMsg.append("Username is required. ");
        if (ValidationUtil.isNullOrEmpty(password)) errorMsg.append("Password is required. ");
        if (!ValidationUtil.isStrongPassword(password)) errorMsg.append("Password must be at least 6 characters long. ");
        if (ValidationUtil.isNullOrEmpty(gender)) errorMsg.append("Gender is required. ");
        if (ValidationUtil.isNullOrEmpty(dob)) errorMsg.append("Date of birth is required. ");
        if (!ValidationUtil.isValidPhoneNumber(number)) errorMsg.append("Invalid phone number. ");
        if (!ValidationUtil.isValidEmail(email)) errorMsg.append("Invalid email format. ");

        Part image = request.getPart("img");
        if (image == null || image.getSize() <= 0) {
            errorMsg.append("Profile image is required. ");
        }

        return errorMsg.length() > 0 ? errorMsg.toString() : null;
    }

    private CustomerModel extractCustomerModel(HttpServletRequest request) throws Exception {
        String firstName = request.getParameter("fName");
        String lastName = request.getParameter("lName");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String number = request.getParameter("number");
        String email = request.getParameter("email");

        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encrypt(salt, password);
        String storedPassword = salt + ":" + encryptedPassword;

        Part image = request.getPart("img");
        String imageUrl = imageUtil.getImageNameFromPart(image);

        return new CustomerModel(0, firstName, lastName, userName, storedPassword, gender, dob, number, email, imageUrl, null);
    }

    private boolean uploadImage(HttpServletRequest request) throws IOException, ServletException {
        Part image = request.getPart("img");
        return imageUtil.uploadImage(image, request.getServletContext().getRealPath("/"));
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("fName", request.getParameter("fName"));
        request.setAttribute("lName", request.getParameter("lName"));
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("dob", request.getParameter("dob"));
        request.setAttribute("gender", request.getParameter("gender"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("number", request.getParameter("number"));
        request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
    }
}