package com.CourtControl.model;

import java.util.List;

public class CourtModel {
    private int id;
    private String courttype; // Foreign key referencing courttype(courttype), now a String
    private String courtName;
    private String status;
    private List<String> features;
    private String imageUrl;
    // Add these fields for display purposes
    private String courttypeName; // Same as courttype, but kept for consistency
    private double courtprice;    // Fetched from courttype table

    // Constructor
    public CourtModel() {}

    public CourtModel(int id, String courttype, String courtName, String status, List<String> features, String imageUrl) {
        this.id = id;
        this.courttype = courttype;
        this.courtName = courtName;
        this.status = status;
        this.features = features;
        this.imageUrl = imageUrl;
        this.courttypeName = courttype; // Set courttypeName to courttype
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourttype() { return courttype; }
    public void setCourttype(String courttype) { this.courttype = courttype; }

    public String getCourtName() { return courtName; }
    public void setCourtName(String courtName) { this.courtName = courtName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCourttypeName() { return courttypeName; }
    public void setCourttypeName(String courttypeName) { this.courttypeName = courttypeName; }

    public double getCourtprice() { return courtprice; }
    public void setCourtprice(double courtprice) { this.courtprice = courtprice; }
}