package com.CourtControl.model;

public class CustomerModel {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String userName;
    private String password;
    private String dob;
    private String gender;
    private String imageUrl;
    private String role; // New field

    public CustomerModel(int userId, String firstName, String lastName, String userName, String password,
                        String gender, String dob, String number, String email, String imageUrl, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.number = number;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    // Existing getters and setters...

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // New getter and setter for role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Helper method to check if the user is an admin
    public boolean isAdmin() {
        return "admin".equals(role);
    }
}