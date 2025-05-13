package com.CourtControl.controller;

import com.CourtControl.model.CustomerModel;
import com.CourtControl.model.BookingModel;
import com.CourtControl.service.ProfileService;
import com.CourtControl.service.BookingService;
import com.CourtControl.util.SessionUtil;
import com.CourtControl.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = {"/userprofile", "/userprofile/delete"})
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
            // Fix: Pass userId as int directly
            bookings = bookingService.getUserBookings(user.getUserId());
        } catch (Exception e) {
            request.setAttribute("error", "Failed to load bookings: " + e.getMessage());
            e.printStackTrace();
        }
        request.setAttribute("bookings", bookings);

        request.getRequestDispatcher("WEB-INF/pages/profile.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to update the user's profile or delete a booking.
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
        } else {
            handleUpdateProfile(request, response, user);
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
            // Fix: Pass userId as int directly
            bookings = bookingService.getUserBookings(user.getUserId());
        } catch (Exception e) {
            request.setAttribute("error", message + " Failed to load bookings: " + e.getMessage());
            e.printStackTrace();
        }
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("WEB-INF/pages/profile.jsp").forward(request, response);
    }
}