package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.FeedbackModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    public FeedbackModel sendFeedback(FeedbackModel feedback) {
        String sql = "INSERT INTO contact (user_id, feedback_text, submitted_at, contact_status, contact_name, contact_email) " +
                     "VALUES (?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set user_id: if userId is 0, set to NULL; otherwise, use the userId
            if (feedback.getUserId() == 0) {
                pstmt.setObject(1, null);
            } else {
                pstmt.setInt(1, feedback.getUserId());
            }
            pstmt.setString(2, feedback.getMessage());
            pstmt.setString(3, "PENDING"); // contact_status
            pstmt.setString(4, feedback.getContactName());
            pstmt.setString(5, feedback.getContactEmail());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the auto-incremented contact_id
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int contactId = rs.getInt(1);
                        return new FeedbackModel(
                            contactId,
                            feedback.getUserId(),
                            feedback.getContactName(),
                            feedback.getContactEmail(),
                            feedback.getMessage(),
                            new Timestamp(System.currentTimeMillis()), // Approximate submitted_at with current time
                            "PENDING" // Default contact_status
                        );
                    }
                }
            }
            return null; // Insertion succeeded but no contact_id retrieved
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<FeedbackModel> getAllFeedback() {
        List<FeedbackModel> feedbackList = new ArrayList<>();
        String sql = "SELECT contact_id, user_id, contact_name, contact_email, feedback_text, submitted_at, contact_status " +
                     "FROM contact WHERE contact_status = 'PENDING'";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                FeedbackModel feedback = new FeedbackModel(
                    rs.getInt("contact_id"),
                    rs.getInt("user_id") != 0 ? rs.getInt("user_id") : 0,
                    rs.getString("contact_name"),
                    rs.getString("contact_email"),
                    rs.getString("feedback_text"),
                    rs.getTimestamp("submitted_at"),
                    rs.getString("contact_status")
                );
                feedbackList.add(feedback);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
}