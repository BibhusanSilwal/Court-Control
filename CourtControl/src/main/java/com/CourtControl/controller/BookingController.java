package com.CourtControl.controller;

import com.CourtControl.service.BookingService;
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

    public BookingController() {
        super();
        this.bookingService = new BookingService();
    }

    /**
     * Displays the booking page
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userId") == null) {
            request.setAttribute("error", "Please log in to book a court.");
            request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }
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

        // Retrieve userId
        String userId = session.getAttribute("userId").toString();


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

        // Retrieve membership_id (placeholder)
        Integer membershipId = null;
        // TODO: Fetch membership_id from Membership table if needed

        try {
            // Check for booking conflicts
            if (!bookingService.isCourtAvailable(courtId, bookingDate, timeSlot)) {
                request.setAttribute("error", "This court is already booked for the selected time slot.");
                request.getRequestDispatcher("WEB-INF/pages/booking.jsp").forward(request, response);
                return;
            }

            // Save booking
            bookingService.saveBooking(courtId, bookingDate, timeSlot, userId, membershipId);


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
        }
    }
}