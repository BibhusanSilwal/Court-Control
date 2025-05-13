package com.CourtControl.controller;

import com.CourtControl.model.BookingModel;
import com.CourtControl.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/dashboard" })
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService;

    public DashboardController() {
        super();
        this.bookingService = new BookingService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("userId") == null) {
            request.setAttribute("error", "Please log in to access the dashboard.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        try {
            // Fetch recent bookings
            List<BookingModel> bookings = bookingService.getRecentBookings(5);
            request.setAttribute("bookings", bookings);

            // Fetch metrics
            int totalBookings = bookingService.getTotalBookings();
            double totalRevenue = bookingService.getTotalRevenue();
            int activeUsers = bookingService.getActiveUsers();
            double avgSession = bookingService.getAverageSessionDuration();

            request.setAttribute("totalBookings", totalBookings);
            request.setAttribute("totalRevenue", String.format("NPR %,d", (int)totalRevenue));
            request.setAttribute("activeUsers", activeUsers);
            request.setAttribute("avgSession", String.format("%.1f hours", avgSession));

            // Fetch initial chart data (default to 7 days)
            Map<String, Integer> bookingsByCourt = bookingService.getBookingsByCourt(7);
            Map<String, Double> dailyRevenue = bookingService.getDailyRevenue(7);

            request.setAttribute("bookingsByCourtLabels", bookingsByCourt.keySet());
            request.setAttribute("bookingsByCourtData", bookingsByCourt.values());
            request.setAttribute("dailyRevenueLabels", dailyRevenue.keySet());
            request.setAttribute("dailyRevenueData", dailyRevenue.values());

            request.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database connection error.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}