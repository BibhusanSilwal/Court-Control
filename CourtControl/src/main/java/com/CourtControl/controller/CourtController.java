package com.CourtControl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.CourtControl.config.DbConfig;
import com.CourtControl.model.CourtModel;
import com.CourtControl.service.CourtService;
import com.CourtControl.util.ImageUtil;
import com.CourtControl.util.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(
    asyncSupported = true,
    urlPatterns = {
        "/admin/courts",
        "/admin/addcourt",
        "/admin/saveCourt",
        "/admin/editCourt",
        "/admin/updateCourt",
        "/admin/deleteCourt"
    }
)
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class CourtController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourtService courtService;
    private static final String UPLOAD_DIR = "resources/images";

    @Override
    public void init() throws ServletException {
        courtService = new CourtService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/admin/courts":
                listCourts(request, response);
                break;
            case "/admin/addcourt":
                showAddCourtForm(request, response);
                break;
            case "/admin/editCourt":
                showEditCourtForm(request, response);
                break;
            case "/admin/deleteCourt":
                deleteCourt(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/admin/saveCourt":
                saveCourt(request, response);
                break;
            case "/admin/updateCourt":
                updateCourt(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }
    }

    private void listCourts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CourtModel> courtList = courtService.getAllCourts();
        request.setAttribute("courtList", courtList);
        request.getRequestDispatcher("/WEB-INF/pages/admin/courts.jsp").forward(request, response);
    }

    private void showAddCourtForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all courttypes for the dropdown
        List<String> courttypes = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT courttype, courtprice FROM courttype");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                courttypes.add(rs.getString("courttype") + " (NPR " + rs.getDouble("courtprice") + "/hour)");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        request.setAttribute("courttypes", courttypes);
        request.getRequestDispatcher("/WEB-INF/pages/admin/addcourt.jsp").forward(request, response);
    }

    private void showEditCourtForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        CourtModel court = courtService.getCourtById(id);
        if (court == null) {
            request.setAttribute("error", "Court not found.");
            listCourts(request, response);
            return;
        }
        // Fetch all courttypes for the dropdown
        List<String> courttypes = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT courttype, courtprice FROM courttype");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                courttypes.add(rs.getString("courttype") + " (NPR " + rs.getDouble("courtprice") + "/hour)");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        request.setAttribute("court", court);
        request.setAttribute("courttypes", courttypes);
        request.getRequestDispatcher("/WEB-INF/pages/admin/editcourt.jsp").forward(request, response);
    }

    private void saveCourt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courtName = request.getParameter("courtName");
        String courttype = request.getParameter("courttype");
        String status = request.getParameter("status");
        String[] featuresArray = request.getParameterValues("features");
        List<String> features = featuresArray != null ? Arrays.asList(featuresArray) : new ArrayList<>();

        if (ValidationUtil.isNullOrEmpty(courtName) || ValidationUtil.isNullOrEmpty(courttype) || ValidationUtil.isNullOrEmpty(status)) {
            request.setAttribute("error", "All fields are required.");
            showAddCourtForm(request, response);
            return;
        }

        // Fix: Correct regex pattern to split "Indoor (NPR 2400/hour)" and extract "Indoor"
        courttype = courttype.split(" \\(NPR ")[0]; // Split on " (NPR " to get the courttype

        Part filePart = request.getPart("courtImage");
        String imageUrl = "";
        if (filePart != null && filePart.getSize() > 0) {
            System.out.println("File part received. Size: " + filePart.getSize() + " bytes");
            ImageUtil imageUtil = new ImageUtil();
            String applicationPath = request.getServletContext().getRealPath("");
            System.out.println("Application path: " + applicationPath);
            if (!imageUtil.uploadImage(filePart, applicationPath)) {
                request.setAttribute("error", "Image upload failed. Check server logs for details.");
                showAddCourtForm(request, response);
                return;
            }
            imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + imageUtil.getImageNameFromPart(filePart);
            System.out.println("Generated imageUrl: " + imageUrl);
        } else {
            request.setAttribute("error", "No file uploaded or file size is zero.");
            showAddCourtForm(request, response);
            return;
        }

        CourtModel court = new CourtModel();
        court.setCourttype(courttype);
        court.setCourtName(courtName);
        court.setStatus(status);
        court.setFeatures(features);
        court.setImageUrl(imageUrl);

        if (courtService.addCourt(court)) {
            response.sendRedirect(request.getContextPath() + "/admin/courts");
        } else {
            request.setAttribute("error", "Failed to add court.");
            showAddCourtForm(request, response);
        }
    }
    private void updateCourt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String courtName = request.getParameter("courtName");
        String courttype = request.getParameter("courttype");
        String status = request.getParameter("status");
        String[] featuresArray = request.getParameterValues("features");
        List<String> features = featuresArray != null ? Arrays.asList(featuresArray) : new ArrayList<>();

        // Validation
        if (ValidationUtil.isNullOrEmpty(courtName) || ValidationUtil.isNullOrEmpty(courttype) || ValidationUtil.isNullOrEmpty(status)) {
            request.setAttribute("error", "All fields are required.");
            showEditCourtForm(request, response);
            return;
        }

        // Extract courttype value (remove the price part)
        courttype = courttype.split(" \\(NPR")[0];

        // Handle file upload (optional)
        String imageUrl;
        Part filePart = request.getPart("courtImage");
        if (filePart != null && filePart.getSize() > 0) {
            ImageUtil imageUtil = new ImageUtil();
            String applicationPath = request.getServletContext().getRealPath("");
            if (!imageUtil.uploadImage(filePart, applicationPath)) {
                request.setAttribute("error", "Image upload failed.");
                showEditCourtForm(request, response);
                return;
            }
            imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + imageUtil.getImageNameFromPart(filePart);
        } else {
            CourtModel existingCourt = courtService.getCourtById(id);
            imageUrl = existingCourt != null ? existingCourt.getImageUrl() : "";
        }

        // Update court
        CourtModel court = new CourtModel();
        court.setId(id);
        court.setCourttype(courttype);
        court.setCourtName(courtName);
        court.setStatus(status);
        court.setFeatures(features);
        court.setImageUrl(imageUrl);

        if (courtService.updateCourt(court)) {
            response.sendRedirect(request.getContextPath() + "/admin/courts");
        } else {
            request.setAttribute("error", "Failed to update court.");
            showEditCourtForm(request, response);
        }
    }

    private void deleteCourt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (courtService.deleteCourt(id)) {
            response.sendRedirect(request.getContextPath() + "/admin/courts");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/courts?error=Failed to delete court.");
        }
    }
}