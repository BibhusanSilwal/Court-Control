package com.CourtControl.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordUtil {

    // Generate random salt
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        StringBuilder salt = new StringBuilder();
        for (byte b : saltBytes) {
            salt.append(String.format("%02x", b));
        }
        return salt.toString();
    }

    // Encrypt using SHA-256
    public static String encrypt(String salt, String password) throws Exception {
        String saltedPassword = salt + password;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(saltedPassword.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    // Compare two passwords
    public static boolean doPasswordsMatch(String p1, String p2) {
        return p1 != null && p1.equals(p2);
    }
    public static boolean validate(String storedEncryptedPassword, String inputPassword) throws Exception {
      
        if (storedEncryptedPassword == null || !storedEncryptedPassword.contains(":")) return false;
        
        String[] parts = storedEncryptedPassword.split(":");
        if (parts.length != 2) return false;

        String salt = parts[0];
        String storedHash = parts[1];

        String inputHash = encrypt(salt, inputPassword);

        return storedHash.equals(inputHash);
    }

}
