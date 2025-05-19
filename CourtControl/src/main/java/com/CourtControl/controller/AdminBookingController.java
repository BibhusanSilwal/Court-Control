package com.CourtControl.controller;

import com.CourtControl.model.BookingModel;
import com.CourtControl.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/bookings"})
public class AdminBookingController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService;

    public AdminBookingController() {
        super();
        this.bookingService = new BookingService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<BookingModel> bookings;
        try {
            bookings = bookingService.searchBookings(query, 10);
            request.setAttribute("bookings", bookings);
            request.setAttribute("searchQuery", query); // Preserve search query for the view
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        request.getRequestDispatcher("/WEB-INF/pages/admin/adminbooking.jsp").forward(request, response);
    }
}