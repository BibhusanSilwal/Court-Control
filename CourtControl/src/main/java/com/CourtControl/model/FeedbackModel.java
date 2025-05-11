package com.CourtControl.model;

import java.util.Date;

public class FeedbackModel {
    private int contactId;
    private int userId;
    private String contactName;
    private String contactEmail;
    private String feedbackText; // Maps to feedback_text in the database
    private Date submissionDate; // Maps to submitted_at
    private String contactStatus;

    // Additional fields for JSP compatibility
    private String userName; // Alias for contactName
    private String message;  // Alias for feedbackText (for sendFeedback compatibility)

    // Constructor for retrieving feedback from database
    public FeedbackModel(int contactId, int userId, String contactName, String contactEmail, 
                         String feedbackText, Date submissionDate, String contactStatus) {
        this.contactId = contactId;
        this.userId = userId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.feedbackText = feedbackText;
        this.submissionDate = submissionDate;
        this.contactStatus = contactStatus;
        this.userName = contactName; // Sync with JSP
        this.message = feedbackText; // Sync with sendFeedback
    }

    // Constructor for creating feedback before insertion
    public FeedbackModel(int userId, String contactName, String contactEmail, String message) {
        this.userId = userId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.message = message;
        this.feedbackText = message; // Sync with feedbackText
        this.userName = contactName; // Sync with JSP
    }

    // Getters and Setters
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
        this.userName = contactName; // Sync with userName
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
        this.message = feedbackText; // Sync with message
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.contactName = userName; // Sync with contactName
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.feedbackText = message; // Sync with feedbackText
    }
}