package com.CourtControl.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;

/**
 * Utility class for handling image file uploads.
 */
public class ImageUtil {

    /**
     * Extracts the file name from the given Part object and adds a timestamp to ensure uniqueness.
     */
    public String getImageNameFromPart(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");

        String imageName = null;
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                break;
            }
        }

        if (imageName == null || imageName.isEmpty()) {
            imageName = "download.png";
        }

        // Add timestamp to ensure uniqueness
        String timestamp = String.valueOf(System.currentTimeMillis());
        int lastDotIndex = imageName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            String namePart = imageName.substring(0, lastDotIndex);
            String extension = imageName.substring(lastDotIndex);
            imageName = namePart + "_" + timestamp + extension;
        } else {
            imageName = imageName + "_" + timestamp + ".png";
        }

        return imageName;
    }

    /**
     * Uploads the image file to the resources/images directory in the web application.
     */
    public boolean uploadImage(Part part, String rootPath) {
        String savePath = rootPath + File.separator + "resources" + File.separator + "images";
        File fileSaveDir = new File(savePath);

        // Ensure the directory exists
        if (!fileSaveDir.exists()) {
            if (!fileSaveDir.mkdirs()) {
                return false; // Failed to create the directory
            }
        }

        try {
            String imageName = getImageNameFromPart(part);
            String filePath = savePath + File.separator + imageName;
            part.write(filePath);
            return true; // Upload successful
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            return false; // Upload failed
        }
    }
}