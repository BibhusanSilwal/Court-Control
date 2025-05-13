package com.CourtControl.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.CourtControl.config.DbConfig;

public class ProfileService {

    /**
     * Updates a user's profile in the database.
     * @param userId The ID of the user to update
     * @param username The updated username (can be null or empty)
     * @param email The updated email (can be null or empty)
     * @param number The updated phone number (can be null or empty)
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException 
     */
    public boolean updateProfile(int userId, String username, String email, String number) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE user SET username = ?, email = ?, phone_number = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            // Establish database connection
            conn = DbConfig.getDbConnection();

            // Prepare the SQL statement
            pstmt = conn.prepareStatement(sql);
            // Set parameters, handling null or empty strings
            pstmt.setString(1, (username != null && !username.trim().isEmpty()) ? username : null);
            pstmt.setString(2, (email != null && !email.trim().isEmpty()) ? email : null);
            pstmt.setString(3, (number != null && !number.trim().isEmpty()) ? number : null);
            pstmt.setInt(4, userId);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0; // True if at least one row was updated

        } catch (SQLException e) {
            // Re-throw the exception to be handled by the controller
            throw e;
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return success;
    }

    /**
     * Updates a user's password in the database.
     * @param userId The ID of the user to update
     * @param newPassword The new encrypted password (salt:hash format)
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws ClassNotFoundException 
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE user SET password = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            // Establish database connection
            conn = DbConfig.getDbConnection();

            // Prepare the SQL statement
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0; // True if at least one row was updated

        } catch (SQLException e) {
            throw e;
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return success;
    }
}