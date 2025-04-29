package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.FeedbackModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FeedbackService {

    public FeedbackModel sendFeedback(FeedbackModel feedback) {
        String sql = "INSERT INTO contact (user_id, message, submission_date, contact_status, contact_name, contact_email) " +
                     "VALUES (?, ?, CURDATE(), ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set user_id: if userId is 0, set to NULL; otherwise, use the userId
        	System.out.println("HI");
            if (feedback.getUserId() == 0) {
                stmt.setObject(1, null);
            } else {
                stmt.setInt(1, feedback.getUserId());
            }
            stmt.setString(2, feedback.getMessage());
            stmt.setString(3, "PENDING"); // contact_status, required
            stmt.setString(4, feedback.getName());
            stmt.setString(5, feedback.getEmail());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the auto-incremented contact_id
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int contactId = rs.getInt(1);
                        return new FeedbackModel(
                            contactId,
                            feedback.getUserId(),
                            feedback.getName(),
                            feedback.getEmail(),
                            feedback.getMessage()
                        );
                    }
                }
            }
            return null; // Insertion succeeded but no contact_id retrieved
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Replace with SLF4J logging in production
            return null;
        }
    }
}