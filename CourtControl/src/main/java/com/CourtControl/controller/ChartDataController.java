package com.CourtControl.controller;

import com.CourtControl.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/chart-data" })
public class ChartDataController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService;

    public ChartDataController() {
        super();
        this.bookingService = new BookingService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String chartType = request.getParameter("chartType");
            int days = Integer.parseInt(request.getParameter("days"));

            StringBuilder json = new StringBuilder("{");
            if ("bookings".equals(chartType)) {
                Map<String, Integer> bookingsByCourt = bookingService.getBookingsByCourt(days);
                String labels = "[" + bookingsByCourt.keySet().stream()
                    .map(s -> "\"" + s.replace("\"", "\\\"") + "\"")
                    .collect(Collectors.joining(",")) + "]";
                String data = "[" + bookingsByCourt.values().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "]";
                json.append("\"labels\":").append(labels).append(",\"data\":").append(data);
            } else if ("revenue".equals(chartType)) {
                Map<String, Double> dailyRevenue = bookingService.getDailyRevenue(days);
                String labels = "[" + dailyRevenue.keySet().stream()
                    .map(s -> "\"" + s.replace("\"", "\\\"") + "\"")
                    .collect(Collectors.joining(",")) + "]";
                String data = "[" + dailyRevenue.values().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "]";
                json.append("\"labels\":").append(labels).append(",\"data\":").append(data);
            }
            json.append("}");

            response.getWriter().write(json.toString());
        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid days parameter\"}");
        }
    }
}