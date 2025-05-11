<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Court - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addnewcourt.css">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="header">
                <h1>Edit Court</h1>
            </div>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <div class="form-card">
                <form action="${pageContext.request.contextPath}/admin/updateCourt" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${court.id}">
                    
                    <div class="form-group">
                        <label for="courtName">Court Name</label>
                        <input type="text" id="courtName" name="courtName" value="${court.courtName}" required>
                    </div>

                    <div class="form-group">
                        <label for="courttype">Court Type</label>
                        <select id="courttype" name="courttype" required>
                            <c:forEach var="courttypeOption" items="${courttypes}">
                                <c:set var="courttypeValue" value="${courttypeOption.split(' \\\\(NPR')[0]}"/>
                                <option value="${courttypeValue}" ${court.courttype == courttypeValue ? 'selected' : ''}>${courttypeOption}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="Available" ${court.status == 'Available' ? 'selected' : ''}>Available</option>
                            <option value="Occupied" ${court.status == 'Occupied' ? 'selected' : ''}>Occupied</option>
                            <option value="Maintenance" ${court.status == 'Maintenance' ? 'selected' : ''}>Maintenance</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Features</label>
                        <div class="features-list">
                            <label><input type="checkbox" name="features" value="Air Conditioning" ${court.features.contains('Air Conditioning') ? 'checked' : ''}> Air Conditioning</label>
                            <label><input type="checkbox" name="features" value="Professional Flooring" ${court.features.contains('Professional Flooring') ? 'checked' : ''}> Professional Flooring</label>
                            <label><input type="checkbox" name="features" value="Locker Room" ${court.features.contains('Locker Room') ? 'checked' : ''}> Locker Room</label>
                            <label><input type="checkbox" name="features" value="Flood Lights" ${court.features.contains('Flood Lights') ? 'checked' : ''}> Flood Lights</label>
                            <label><input type="checkbox" name="features" value="Covered Seating" ${court.features.contains('Covered Seating') ? 'checked' : ''}> Covered Seating</label>
                            <label><input type="checkbox" name="features" value="Water Station" ${court.features.contains('Water Station') ? 'checked' : ''}> Water Station</label>
                            <label><input type="checkbox" name="features" value="Training Equipment" ${court.features.contains('Training Equipment') ? 'checked' : ''}> Training Equipment</label>
                            <label><input type="checkbox" name="features" value="Score Board" ${court.features.contains('Score Board') ? 'checked' : ''}> Score Board</label>
                            <label><input type="checkbox" name="features" value="First Aid Kit" ${court.features.contains('First Aid Kit') ? 'checked' : ''}> First Aid Kit</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="courtImage">Court Image (Leave blank to keep current image)</label>
                        <input type="file" id="courtImage" name="courtImage" accept="image/*">
                        <p>Current Image: <a href="${court.imageUrl}" target="_blank">View Image</a></p>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/admin/courts" class="cancel-btn">Cancel</a>
                        <button type="submit" class="save-btn">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>