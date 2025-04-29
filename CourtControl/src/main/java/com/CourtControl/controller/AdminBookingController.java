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
        List<BookingModel> bookings = bookingService.getRecentBookings(10);
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/pages/admin/adminbooking.jsp").forward(request, response);
    }
}