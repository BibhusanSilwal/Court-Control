package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.CustomerModel;
import com.CourtControl.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {

    public CustomerModel loginUser(String username, String inputPassword) throws Exception {
        String sql = "SELECT * FROM User WHERE username = ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (PasswordUtil.validate(storedPassword, inputPassword)) {
                        return new CustomerModel(
                            rs.getInt("user_id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("username"),
                            storedPassword,
                            rs.getString("gender"),
                            rs.getString("dob"),
                            rs.getString("number"),
                            rs.getString("email"),
                            rs.getString("img"),
                            rs.getString("role") // Fetch role
                        );
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); 
        }

        return null; // If not found or password mismatch
    }

    public CustomerModel getUserById(int userId) {
        String sql = "SELECT * FROM User WHERE user_id = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerModel(
                        rs.getInt("user_id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("gender"),
                        rs.getString("dob"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("img"),
                        rs.getString("role") // Fetch role
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}