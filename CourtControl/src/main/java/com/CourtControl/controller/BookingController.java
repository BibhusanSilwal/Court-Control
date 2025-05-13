package com.CourtControl.controller;

import com.CourtControl.service.BookingService;
import com.CourtControl.service.CourtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;

@WebServlet(asyncSupported = true, urlPatterns = { "/booking" })
public class BookingController extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    private final BookingService bookingService;
    private final CourtService courtService;

    public BookingController() {
        super();
        this.bookingService = new BookingService();
        this.courtService = new CourtService();
    }

    /**
     * Displays the booking page with fetched courts
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userId") == null) {
            request.setAttribute("error", "Please log in to book a court.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        // Fetch all courts
        request.setAttribute("courts", courtService.getAllCourts());

        // Optional: Fetch time slots if your BookingService provides them
        // request.setAttribute("timeSlots", bookingService.getAvailableTimeSlots());

        request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
    }

    /**
     * Handles the booking form submission
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userId") == null) {
            request.setAttribute("error", "Please log in to make a booking.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        // Retrieve userId and convert to int
        String userIdStr = session.getAttribute("userId").toString();
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format. Please log in again.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        // Retrieve form data
        String courtId = request.getParameter("courtId");
        String courtName = request.getParameter("courtName");
        String bookingDate = request.getParameter("bookingDate");
        String timeSlot = request.getParameter("timeSlot");

        // Validate inputs
        if (courtId == null || bookingDate == null || timeSlot == null ||
            courtId.trim().isEmpty() || bookingDate.trim().isEmpty() || timeSlot.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
            return;
        }

        try {
            // Check for booking conflicts
            if (!bookingService.isCourtAvailable(courtId, bookingDate, timeSlot)) {
                request.setAttribute("error", "The court is not available on " + bookingDate + " from " + timeSlot + ". Please choose a different time or date.");
                request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
                return;
            }

            // Save booking with userId as int
            bookingService.saveBooking(courtId, bookingDate, timeSlot, userId);

            // Set success message
            request.setAttribute("success", "Booking confirmed for Court " + courtName + " on " + bookingDate + " at " + timeSlot);

            // Forward back to booking page
            request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date or time format. Please try again.");
            request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
        } catch (RuntimeException e) {
            request.setAttribute("error", "An error occurred while processing your booking: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database connection error. Please try again later.");
            request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
        }
    }
}