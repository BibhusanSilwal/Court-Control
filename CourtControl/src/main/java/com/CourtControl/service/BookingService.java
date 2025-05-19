package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.BookingModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BookingService {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Checks if a court is available for a given date and time slot.
     */
    public boolean isCourtAvailable(String courtId, String bookingDate, String timeSlot) throws ParseException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Booking WHERE court_id = ? AND booking_date = ? " +
                     "AND (start_time < ? AND end_time > ?)";

        String[] times = timeSlot.split(" - ");
        if (times.length != 2) {
            throw new ParseException("Invalid time slot format: " + timeSlot, 0);
        }
        Date startTime = TIME_FORMAT.parse(times[0]);
        Date endTime = TIME_FORMAT.parse(times[1]);

        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        Date bookingDateParsed = DATE_FORMAT.parse(bookingDate);
        String startDateTimeStr = DATE_FORMAT.format(bookingDateParsed) + " " + times[0];
        String endDateTimeStr = DATE_FORMAT.format(bookingDateParsed) + " " + times[1];
        Date startDateTime = datetimeFormat.parse(startDateTimeStr);
        Date endDateTime = datetimeFormat.parse(endDateTimeStr);

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(courtId));
            stmt.setDate(2, new java.sql.Date(bookingDateParsed.getTime()));
            stmt.setTimestamp(3, new java.sql.Timestamp(endDateTime.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(startDateTime.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while checking court availability", e);
        }
    }

    /**
     * Saves a new booking to the database.
     */
    public void saveBooking(String courtId, String bookingDate, String timeSlot, int userId) throws ParseException, ClassNotFoundException {
        String sql = "INSERT INTO Booking (user_id, court_id, booking_date, start_time, end_time, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        String[] times = timeSlot.split(" - ");
        if (times.length != 2) {
            throw new ParseException("Invalid time slot format: " + timeSlot, 0);
        }
        Date startTime = TIME_FORMAT.parse(times[0]);
        Date endTime = TIME_FORMAT.parse(times[1]);
        
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        Date bookingDateParsed = DATE_FORMAT.parse(bookingDate);
        String startDateTimeStr = DATE_FORMAT.format(bookingDateParsed) + " " + times[0];
        String endDateTimeStr = DATE_FORMAT.format(bookingDateParsed) + " " + times[1];
        Date startDateTime = datetimeFormat.parse(startDateTimeStr);
        Date endDateTime = datetimeFormat.parse(endDateTimeStr);

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, Integer.parseInt(courtId));
            stmt.setDate(3, new java.sql.Date(bookingDateParsed.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(startDateTime.getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(endDateTime.getTime()));
            stmt.setString(6, "Confirmed");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while saving booking", e);
        }
    }

    /**
     * Retrieves the most recent bookings, limited by the specified number.
     */
    public List<BookingModel> getRecentBookings(int limit) throws ClassNotFoundException {
        return searchBookings(null, limit);
    }

    /**
     * Searches bookings based on a query string, limited by the specified number.
     */
    public List<BookingModel> searchBookings(String query, int limit) throws ClassNotFoundException {
        List<BookingModel> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, u.first_name, u.last_name, u.phone_number AS customer_phone, c.courttype AS court_name, ct.courtprice, " +
                     "b.booking_date, b.start_time, b.end_time, b.status " +
                     "FROM Booking b " +
                     "LEFT JOIN User u ON b.user_id = u.user_id " +
                     "LEFT JOIN Court c ON b.court_id = c.court_id " +
                     "LEFT JOIN CourtType ct ON c.courttype = ct.courttype " +
                     (query != null && !query.trim().isEmpty() ?
                     "WHERE u.first_name LIKE ? OR u.last_name LIKE ? OR u.phone_number LIKE ? OR c.courttype LIKE ? OR b.status LIKE ? " : "") +
                     "ORDER BY b.booking_date DESC, b.start_time DESC " +
                     "LIMIT ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (query != null && !query.trim().isEmpty()) {
                String searchPattern = "%" + query.trim() + "%";
                for (int i = 0; i < 5; i++) {
                    stmt.setString(paramIndex++, searchPattern);
                }
            }
            stmt.setInt(paramIndex, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

                while (rs.next()) {
                    String customerName = rs.getString("first_name") + " " + rs.getString("last_name");
                    String customerPhone = rs.getString("customer_phone");
                    if (customerPhone != null && !customerPhone.startsWith("+977")) {
                        customerPhone = "+977 " + customerPhone;
                    }
                    String courtName = rs.getString("court_name");
                    String bookingDate = dateFormatter.format(rs.getDate("booking_date"));
                    String startTime = timeFormatter.format(rs.getTimestamp("start_time"));
                    String endTime = timeFormatter.format(rs.getTimestamp("end_time"));
                    String timeSlot = startTime + " - " + endTime;
                    String status = rs.getString("status");
                    String price = "NPR " + rs.getInt("courtprice");

                    BookingModel booking = new BookingModel(
                        rs.getInt("booking_id"),
                        customerName,
                        customerPhone,
                        courtName,
                        bookingDate,
                        timeSlot,
                        status,
                        price
                    );
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving bookings", e);
        }
        return bookings;
    }

    /**
     * Retrieves bookings for a specific user.
     */
    public List<BookingModel> getUserBookings(int userId) throws ClassNotFoundException {
        List<BookingModel> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, u.first_name, u.last_name, u.phone_number AS customer_phone, c.courttype AS court_name, ct.courtprice, " +
                     "b.booking_date, b.start_time, b.end_time, b.status " +
                     "FROM Booking b " +
                     "LEFT JOIN User u ON b.user_id = u.user_id " +
                     "LEFT JOIN Court c ON b.court_id = c.court_id " +
                     "LEFT JOIN CourtType ct ON c.courttype = ct.courttype " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.booking_date DESC, b.start_time DESC";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

                while (rs.next()) {
                    String customerName = rs.getString("first_name") + " " + rs.getString("last_name");
                    String customerPhone = rs.getString("customer_phone");
                    if (customerPhone != null && !customerPhone.startsWith("+977")) {
                        customerPhone = "+977 " + customerPhone;
                    }
                    String courtName = rs.getString("court_name");
                    String bookingDate = dateFormatter.format(rs.getDate("booking_date"));
                    String startTime = timeFormatter.format(rs.getTimestamp("start_time"));
                    String endTime = timeFormatter.format(rs.getTimestamp("end_time"));
                    String timeSlot = startTime + " - " + endTime;
                    String status = rs.getString("status");
                    String price = "NPR " + rs.getInt("courtprice");

                    BookingModel booking = new BookingModel(
                        rs.getInt("booking_id"),
                        customerName,
                        customerPhone,
                        courtName,
                        bookingDate,
                        timeSlot,
                        status,
                        price
                    );
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user bookings", e);
        }
        return bookings;
    }

    /**
     * Deletes a booking by its ID if it belongs to the specified user.
     */
    public boolean deleteBooking(int bookingId, int userId) throws ClassNotFoundException {
        String checkSql = "SELECT COUNT(*) FROM Booking WHERE booking_id = ? AND user_id = ?";
        String deleteSql = "DELETE FROM Booking WHERE booking_id = ? AND user_id = ?";

        try (Connection conn = DbConfig.getDbConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookingId);
                checkStmt.setInt(2, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, bookingId);
                deleteStmt.setInt(2, userId);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking", e);
        }
    }

    /**
     * Gets the total number of bookings.
     */
    public int getTotalBookings() throws ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM Booking";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving total bookings", e);
        }
    }

    /**
     * Gets the total revenue from all bookings.
     */
    public double getTotalRevenue() throws ClassNotFoundException {
        String sql = "SELECT SUM(ct.courtprice) AS total_revenue " +
                     "FROM Booking b " +
                     "JOIN Court c ON b.court_id = c.court_id " +
                     "JOIN CourtType ct ON c.courttype = ct.courttype";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getDouble("total_revenue");
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving total revenue", e);
        }
    }

    /**
     * Gets the number of active users (users with at least one booking).
     */
    public int getActiveUsers() throws ClassNotFoundException {
        String sql = "SELECT COUNT(DISTINCT user_id) FROM Booking";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving active users", e);
        }
    }

    /**
     * Gets the average session duration in hours.
     */
    public double getAverageSessionDuration() throws ClassNotFoundException {
        String sql = "SELECT AVG(TIMESTAMPDIFF(MINUTE, start_time, end_time)) / 60 AS avg_duration " +
                     "FROM Booking";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getDouble("avg_duration");
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving average session duration", e);
        }
    }

    /**
     * Retrieves bookings count by court for a given time range (in days).
     */
    public Map<String, Integer> getBookingsByCourt(int days) throws ClassNotFoundException {
        Map<String, Integer> bookingsByCourt = new TreeMap<>();
        String sql = "SELECT c.courttype AS court_name, COUNT(b.booking_id) AS booking_count " +
                     "FROM Booking b " +
                     "JOIN Court c ON b.court_id = c.court_id " +
                     "WHERE b.booking_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                     "GROUP BY c.courttype";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, days);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String courtName = rs.getString("court_name");
                    int bookingCount = rs.getInt("booking_count");
                    bookingsByCourt.put(courtName, bookingCount);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bookings by court", e);
        }

        return bookingsByCourt;
    }

    /**
     * Retrieves daily revenue for a given time range (in days).
     */
    public Map<String, Double> getDailyRevenue(int days) throws ClassNotFoundException {
        Map<String, Double> dailyRevenue = new TreeMap<>();
        String sql = "SELECT DATE(b.booking_date) AS booking_day, SUM(ct.courtprice) AS daily_revenue " +
                     "FROM Booking b " +
                     "JOIN Court c ON b.court_id = c.court_id " +
                     "JOIN CourtType ct ON c.courttype = ct.courttype " +
                     "WHERE b.booking_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                     "GROUP BY DATE(b.booking_date) " +
                     "ORDER BY booking_day";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, days);
            try (ResultSet rs = stmt.executeQuery()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                while (rs.next()) {
                    Date bookingDay = rs.getDate("booking_day");
                    double revenue = rs.getDouble("daily_revenue");
                    String dayStr = dateFormatter.format(bookingDay);
                    dailyRevenue.put(dayStr, revenue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching daily revenue", e);
        }

        return dailyRevenue;
    }
}