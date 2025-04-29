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

    public boolean isCourtAvailable(String courtId, String bookingDate, String timeSlot) throws ParseException {
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
            e.printStackTrace();
            throw new RuntimeException("Database error while checking court availability", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC driver not found", e);
        }
    }

    public void saveBooking(String courtId, String bookingDate, String timeSlot, String userId, Integer membershipId) throws ParseException {
        String sql = "INSERT INTO Booking (user_id, court_id, membership_id, booking_date, start_time, end_time, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
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
            if (membershipId != null) {
                stmt.setInt(3, membershipId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setDate(4, new java.sql.Date(bookingDateParsed.getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(startDateTime.getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(endDateTime.getTime()));
            stmt.setString(7, "Confirmed");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error while saving booking", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC driver not found", e);
        }
    }

    public List<BookingModel> getRecentBookings(int limit) {
        List<BookingModel> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, u.firstname, u.lastname, u.number AS customer_phone, c.type AS court_name, ct.court_price, " +
                     "b.booking_date, b.start_time, b.end_time, b.status " +
                     "FROM Booking b " +
                     "LEFT JOIN User u ON b.user_id = u.user_id " +
                     "LEFT JOIN Court c ON b.court_id = c.court_id " +
                     "LEFT JOIN CourtType ct ON c.type = ct.type " +
                     "ORDER BY b.booking_date DESC, b.start_time DESC " +
                     "LIMIT ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

                while (rs.next()) {
                    String customerName = rs.getString("firstname") + " " + rs.getString("lastname");
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
                    double priceValue = rs.getDouble("court_price");
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
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
}