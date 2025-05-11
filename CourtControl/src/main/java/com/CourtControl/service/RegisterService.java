package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.CustomerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterService {

    public CustomerModel registerUser(CustomerModel user) {
        String sql = "INSERT INTO user (first_name, last_name, username, password, gender, dob, phone_number, email, image_url, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getDob());
            stmt.setString(7, user.getNumber());
            stmt.setString(8, user.getEmail());
            stmt.setString(9, user.getImageUrl());
            // Set role: "admin" if username is "admin", otherwise "user"
            String role = "admin".equals(user.getUserName()) ? "admin" : "user";
            stmt.setString(10, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the auto-incremented user_id
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userId = rs.getInt(1);
                        return new CustomerModel(
                            userId,
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUserName(),
                            user.getPassword(),
                            user.getGender(),
                            user.getDob(),
                            user.getNumber(),
                            user.getEmail(),
                            user.getImageUrl(),
                            role
                        );
                    }
                }
            }
            return null; // Insertion succeeded but no user_id retrieved
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); 
            return null;
        }
    }
}