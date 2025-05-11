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

public class BookingService {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Checks if a court is available for the specified date and time slot.
     * @throws ClassNotFoundException 
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
     * @throws ClassNotFoundException 
     */
    public void saveBooking(String courtId, String bookingDate, String timeSlot, String userId) throws ParseException, ClassNotFoundException {
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
            stmt.setInt(1, Integer.parseInt(userId));
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
     * Retrieves recent bookings with a specified limit.
     * @throws ClassNotFoundException 
     */
    public List<BookingModel> getRecentBookings(int limit) throws ClassNotFoundException {
        List<BookingModel> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, u.first_name, u.last_name, u.phone_number AS customer_phone, c.courttype AS court_name, ct.courtprice, " +
                     "b.booking_date, b.start_time, b.end_time, b.status " +
                     "FROM Booking b " +
                     "LEFT JOIN User u ON b.user_id = u.user_id " +
                     "LEFT JOIN Court c ON b.court_id = c.court_id " +
                     "LEFT JOIN CourtType ct ON c.courttype = ct.courttype " +
                     "ORDER BY b.booking_date DESC, b.start_time DESC " +
                     "LIMIT ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
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
                    Date startTime = rs.getTimestamp("start_time");
                    Date endTime = rs.getTimestamp("end_time");
                    String timeSlot = timeFormatter.format(startTime) + " - " + timeFormatter.format(endTime);
                    String status = rs.getString("status");
                    double priceValue = rs.getDouble("courtprice");
                    String price = "NPR " + (int)priceValue;

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
            System.out.println("Database error fetching recent bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Retrieves bookings for a specific user.
     * @param userId The ID of the user
     * @return List of BookingModel objects for the user
     * @throws ClassNotFoundException 
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
                    Date startTime = rs.getTimestamp("start_time");
                    Date endTime = rs.getTimestamp("end_time");
                    String timeSlot = timeFormatter.format(startTime) + " - " + timeFormatter.format(endTime);
                    String status = rs.getString("status");
                    double priceValue = rs.getDouble("courtprice");
                    String price = "NPR " + (int)priceValue;

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
            System.out.println("Database error fetching user bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Deletes a booking from the database if it belongs to the specified user.
     * @param bookingId The ID of the booking to delete
     * @param userId The ID of the user attempting to delete the booking
     * @return true if the booking was deleted, false if it doesn't exist or belongs to another user
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException 
     */
    public boolean deleteBooking(int bookingId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Booking WHERE booking_id = ? AND user_id = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}