package com.CourtControl.util;

public class ValidationUtil {

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return phone.matches("\\d{10}"); 
    }

    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) return false;
        return username.matches("^[a-zA-Z0-9._-]{3,}$");
    }

    public static boolean isStrongPassword(String password) {
        if (isNullOrEmpty(password)) return false;
        return password.length() >= 6;
    }
}
