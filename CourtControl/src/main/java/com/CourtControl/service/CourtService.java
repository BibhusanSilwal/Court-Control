package com.CourtControl.service;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.CourtModel;
import com.CourtControl.util.ValidationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourtService {
    private static final String SELECT_ALL_COURTS = """
        SELECT c.court_id, c.courttype, c.court_name, c.status, c.features, c.court_image, ct.courttype, ct.courtprice
        FROM court c
        JOIN courttype ct ON c.courttype = ct.courttype
    """;
    private static final String SELECT_COURT_BY_ID = """
        SELECT c.court_id, c.courttype, c.court_name, c.status, c.features, c.court_image, ct.courttype, ct.courtprice
        FROM court c
        JOIN courttype ct ON c.courttype = ct.courttype
        WHERE c.court_id = ?
    """;
    private static final String INSERT_COURT = "INSERT INTO court (courttype, court_name, status, features, court_image) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_COURT = "UPDATE court SET courttype = ?, court_name = ?, status = ?, features = ?, court_image = ? WHERE court_id = ?";
    private static final String DELETE_COURT = "DELETE FROM court WHERE court_id = ?";

    public List<CourtModel> getAllCourts() {
        List<CourtModel> courts = new ArrayList<>();
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("court_id");
                String courttype = rs.getString("courttype");
                String courtName = rs.getString("court_name");
                String status = rs.getString("status");
                String featuresStr = rs.getString("features");
                List<String> features = featuresStr != null ? Arrays.asList(featuresStr.split(",\\s*")) : new ArrayList<>();
                String imageUrl = rs.getString("court_image");
                double courtprice = rs.getDouble("courtprice");
                CourtModel court = new CourtModel(id, courttype, courtName, status, features, imageUrl);
                court.setCourtprice(courtprice);
                courts.add(court);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courts;
    }

    public CourtModel getCourtById(int id) {
        CourtModel court = null;
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String courttype = rs.getString("courttype");
                String courtName = rs.getString("court_name");
                String status = rs.getString("status");
                String featuresStr = rs.getString("features");
                List<String> features = featuresStr != null ? Arrays.asList(featuresStr.split(",\\s*")) : new ArrayList<>();
                String imageUrl = rs.getString("court_image");
                double courtprice = rs.getDouble("courtprice");
                court = new CourtModel(id, courttype, courtName, status, features, imageUrl);
                court.setCourtprice(courtprice);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return court;
    }

    public boolean addCourt(CourtModel court) {
        if (court == null || ValidationUtil.isNullOrEmpty(court.getCourtName()) || ValidationUtil.isNullOrEmpty(court.getCourttype())) {
            return false; // Validation failed
        }
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURT)) {
            preparedStatement.setString(1, court.getCourttype());
            preparedStatement.setString(2, court.getCourtName());
            preparedStatement.setString(3, court.getStatus());
            preparedStatement.setString(4, String.join(", ", court.getFeatures()));
            preparedStatement.setString(5, court.getImageUrl());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourt(CourtModel court) {
        if (court == null || ValidationUtil.isNullOrEmpty(court.getCourtName()) || court.getId() <= 0 || ValidationUtil.isNullOrEmpty(court.getCourttype())) {
            return false; // Validation failed
        }
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COURT)) {
            preparedStatement.setString(1, court.getCourttype());
            preparedStatement.setString(2, court.getCourtName());
            preparedStatement.setString(3, court.getStatus());
            preparedStatement.setString(4, String.join(", ", court.getFeatures()));
            preparedStatement.setString(5, court.getImageUrl());
            preparedStatement.setInt(6, court.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourt(int id) {
        if (id <= 0) {
            return false; // Validation failed
        }
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COURT)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get courtprice by courttype
    public double getCourtpriceByCourttype(String courttype) {
        double courtprice = 0.0;
        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT courtprice FROM courttype WHERE courttype = ?")) {
            preparedStatement.setString(1, courttype);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                courtprice = rs.getDouble("courtprice");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courtprice;
    }
}