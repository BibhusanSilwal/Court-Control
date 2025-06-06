package com.CourtControl.controller;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.model.BookingModel;
import com.CourtControl.service.ProfileService;
import com.CourtControl.service.BookingService;
import com.CourtControl.util.SessionUtil;
import com.CourtControl.util.ValidationUtil;
import com.CourtControl.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = {"/userprofile", "/userprofile/delete", "/userprofile/changepassword"})
public class UserprofileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ProfileService profileService = new ProfileService();
    private final BookingService bookingService = new BookingService();

    /**
     * Handles GET requests to display the profile edit form and booking history.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user from session
        CustomerModel user = (CustomerModel) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Set user attributes for the JSP form
        request.setAttribute("username", user.getUserName());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("number", user.getNumber());

        // Load user's bookings
        List<BookingModel> bookings = new ArrayList<>();
        try {
            bookings = bookingService.getUserBookings(user.getUserId());
        } catch (Exception e) {
            request.setAttribute("error", "Failed to load bookings: " + e.getMessage());
            e.printStackTrace();
        }
        request.setAttribute("bookings", bookings);

        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp").forward(request, response); // Absolute path
    }

    /**
     * Handles POST requests to update the user's profile, delete a booking, or change password.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user from session
        CustomerModel user = (CustomerModel) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        if ("/userprofile/delete".equals(path)) {
            handleDeleteBooking(request, response, user);
        } else if ("/userprofile/changepassword".equals(path)) {
            handleChangePassword(request, response, user);
        } else {
            handleUpdateProfile(request, response, user);
        }
    }

    /**
     * Handles password change requests with improved error handling.
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, CustomerModel user)
            throws ServletException, IOException {
        try {
            // Get password inputs
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // Validate all fields are provided
            if (ValidationUtil.isNullOrEmpty(currentPassword) || 
                ValidationUtil.isNullOrEmpty(newPassword) || 
                ValidationUtil.isNullOrEmpty(confirmPassword)) {
                handlePasswordError(request, response, "All password fields are required.", user);
                return;
            }

            // Validate current password
            if (!PasswordUtil.validate(user.getPassword(), currentPassword)) {
                handlePasswordError(request, response, "The current password you entered is incorrect. Please try again.", user);
                return;
            }

            // Validate new password matches confirmation
            if (!newPassword.equals(confirmPassword)) {
                handlePasswordError(request, response, "The new password and confirmation do not match. Please ensure they are the same.", user);
                return;
            }

            // Basic password strength check
            if (newPassword.length() < 6) {
                handlePasswordError(request, response, "New password must be at least 6 characters long.", user);
                return;
            }

            // Encrypt new password
            String newSalt = PasswordUtil.generateSalt();
            String newEncryptedPassword = PasswordUtil.encrypt(newSalt, newPassword);
            String newStoredPassword = newSalt + ":" + newEncryptedPassword;

            // Update password in database
            boolean updated = profileService.updatePassword(user.getUserId(), newStoredPassword);
            if (updated) {
                // Update session with new password
                user.setPassword(newStoredPassword);
                SessionUtil.setAttribute(request, "user", user);
                response.sendRedirect(request.getContextPath() + "/userprofile?success=Password+changed+successfully");
            } else {
                handlePasswordError(request, response, "Could not change password. Please try again later.", user);
            }
        } catch (Exception e) {
            handlePasswordError(request, response, "An unexpected error occurred while changing the password: " + e.getMessage(), user);
            e.printStackTrace();
        }
    }

    /**
     * Handles profile update requests.
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, CustomerModel user)
            throws ServletException, IOException {
        try {
            // Validate form inputs
            String validationMessage = validateProfileForm(request);
            if (validationMessage != null) {
                handleError(request, response, validationMessage, user);
                return;
            }

            // Extract updated profile details
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String number = request.getParameter("number");

            // Update profile using ProfileService
            boolean updated = profileService.updateProfile(
                user.getUserId(), username, email, number
            );

            if (updated) {
                // Update session with new user details
                user.setUserName(username);
                user.setEmail(email);
                user.setNumber(number);
                SessionUtil.setAttribute(request, "user", user);

                // Redirect to profile page with success message
                response.sendRedirect(request.getContextPath() + "/userprofile?success=Profile+updated+successfully");
            } else {
                handleError(request, response, "Could not update profile. User may not exist or no changes were made.", user);
            }
        } catch (SQLException e) {
            String message = e.getMessage().contains("Duplicate entry") 
                ? "Username or email already exists."
                : "Database error occurred. Please try again later.";
            handleError(request, response, message, user);
            e.printStackTrace();
        } catch (Exception e) {
            handleError(request, response, "An unexpected error occurred. Please try again later!", user);
            e.printStackTrace();
        }
    }

    /**
     * Handles booking deletion requests.
     */
    private void handleDeleteBooking(HttpServletRequest request, HttpServletResponse response, CustomerModel user)
            throws IOException {
        try {
            String bookingIdStr = request.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/userprofile?error=Invalid+booking+ID");
                return;
            }

            int bookingId = Integer.parseInt(bookingIdStr);
            boolean deleted = bookingService.deleteBooking(bookingId, user.getUserId());

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/userprofile?successMessage=Booking+deleted+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/userprofile?error=Could+not+delete+booking.+It+may+not+exist+or+you+lack+permission");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/userprofile?error=Invalid+booking+ID");
            e.printStackTrace();
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/userprofile?error=An+unexpected+error+occurred");
            e.printStackTrace();
        }
    }

    /**
     * Validates the profile update form inputs.
     */
    private String validateProfileForm(HttpServletRequest request) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String number = request.getParameter("number");

        StringBuilder errorMsg = new StringBuilder();

        if (ValidationUtil.isNullOrEmpty(username)) {
            errorMsg.append("Username is required. ");
        }
        if (email != null && !email.trim().isEmpty() && !ValidationUtil.isValidEmail(email)) {
            errorMsg.append("Invalid email format. ");
        }
        if (number != null && !number.trim().isEmpty() && !ValidationUtil.isValidPhoneNumber(number)) {
            errorMsg.append("Invalid phone number. ");
        }

        return errorMsg.length() > 0 ? errorMsg.toString() : null;
    }

    /**
     * Handles errors by setting error message and forwarding to profile JSP.
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message, CustomerModel user)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("number", request.getParameter("number"));
        // Reload bookings for error case
        List<BookingModel> bookings = new ArrayList<>();
        try {
            bookings = bookingService.getUserBookings(user.getUserId());
        } catch (Exception e) {
            request.setAttribute("error", message + " Failed to load bookings: " + e.getMessage());
            e.printStackTrace();
        }
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp").forward(request, response); // Absolute path
    }

    /**
     * Handles password change errors by setting error message and forwarding to profile JSP.
     */
    private void handlePasswordError(HttpServletRequest request, HttpServletResponse response, String message, CustomerModel user)
            throws ServletException, IOException {
        request.setAttribute("passwordError", message);
        // Reload user details and bookings
        request.setAttribute("username", user.getUserName());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("number", user.getNumber());
        List<BookingModel> bookings = new ArrayList<>();
        try {
            bookings = bookingService.getUserBookings(user.getUserId());
        } catch (Exception e) {
            request.setAttribute("passwordError", message + " Failed to load bookings: " + e.getMessage());
            e.printStackTrace();
        }
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp").forward(request, response); // Absolute path
    }
}