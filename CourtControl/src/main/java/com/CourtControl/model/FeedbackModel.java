package com.CourtControl.model;

public class FeedbackModel {
    private int contactId;
    private int userId; // Add userId field
    private String name;
    private String email;
    private String message;

    public FeedbackModel(int contactId, int userId, String name, String email, String message) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    // Getters and setters
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}