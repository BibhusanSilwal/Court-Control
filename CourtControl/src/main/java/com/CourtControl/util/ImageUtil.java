package com.CourtControl.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public boolean uploadImage(Part filePart, String applicationPath) {
        String uploadPath = applicationPath + File.separator + "resources" + File.separator + "images";
        File uploadDir = new File(uploadPath);

        // Debug: Print the upload path
        System.out.println("Attempting to upload to: " + uploadPath);

        if (!uploadDir.exists()) {
            System.out.println("Directory does not exist. Attempting to create...");
            boolean created = uploadDir.mkdirs();
            if (!created) {
                System.out.println("Failed to create directory: " + uploadPath);
                return false;
            }
            System.out.println("Directory created successfully.");
        }

        String fileName = getImageNameFromPart(filePart);
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Invalid or empty filename from part.");
            return false;
        }
        System.out.println("Extracted filename: " + fileName);

        try {
            File file = new File(uploadPath + File.separator + fileName);
            filePart.write(file.getAbsolutePath());
            System.out.println("Image saved to: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("IOException during file upload: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getImageNameFromPart(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        System.out.println("Content-Disposition header: " + contentDisposition);
        if (contentDisposition != null) {
            String[] tokens = contentDisposition.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    String fileName = token.substring(token.indexOf("=") + 1).trim().replace("\"", "");
                    System.out.println("Parsed filename: " + fileName);
                    return fileName;
                }
            }
        }
        System.out.println("No filename found in content-disposition.");
        return null;
    }
}