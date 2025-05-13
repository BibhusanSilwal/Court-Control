package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.CustomerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterService {

    public CustomerModel registerUser(CustomerModel user) {
        String sql = "INSERT INTO user (first_name, last_name, username, password, gender, dob, phone_number, email, image_url, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getDob());
            stmt.setString(7, user.getNumber());
            stmt.setString(8, user.getEmail());
            stmt.setString(9, user.getImageUrl());
            String role = "admin".equals(user.getUserName()) ? "admin" : "user";
            stmt.setString(10, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
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
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // New method to get all users
    public List<CustomerModel> getAllUsers() {
        List<CustomerModel> userList = new ArrayList<>();
        String sql = "SELECT user_id, first_name, last_name, username, password, gender, dob, phone_number, email, image_url, role " +
                     "FROM user";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CustomerModel user = new CustomerModel(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("gender"),
                    rs.getString("dob"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("image_url"),
                    rs.getString("role")
                );
                userList.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // New method to update user role
    public boolean updateUserRole(int userId, String newRole) {
        String sql = "UPDATE user SET role = ? WHERE user_id = ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRole);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}