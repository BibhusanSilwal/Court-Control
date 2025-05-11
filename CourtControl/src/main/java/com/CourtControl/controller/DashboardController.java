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

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/dashboard" })
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService;

    public DashboardController() {
        super();
        this.bookingService = new BookingService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null || session.getAttribute("userId") == null) {
//            request.setAttribute("error", "Please log in to access the dashboard.");
//            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
//            return;
//        }

        // Fetch recent bookings
        List<BookingModel> bookings;
		try {
			bookings = bookingService.getRecentBookings(5);
			request.setAttribute("bookings", bookings);
			request.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}