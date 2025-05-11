<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Court - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addnewcourt.css">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="header">
                <h1>Add New Court</h1>
            </div>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <div class="form-card">
                <form action="${pageContext.request.contextPath}/admin/saveCourt" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="courtName">Court Name</label>
                        <input type="text" id="courtName" name="courtName" placeholder="e.g., Indoor Court A" required>
                    </div>

                    <div class="form-group">
                        <label for="courttype">Court Type</label>
                        <select id="courttype" name="courttype" required>
                            <c:forEach var="courttype" items="${courttypes}">
                                <option value="${courttype.split(' \\(NPR')[0]}">${courttype}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="Available">Available</option>
                            <option value="Occupied">Occupied</option>
                            <option value="Maintenance">Maintenance</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Features</label>
                        <div class="features-list">
                            <label><input type="checkbox" name="features" value="Air Conditioning"> Air Conditioning</label>
                            <label><input type="checkbox" name="features" value="Professional Flooring"> Professional Flooring</label>
                            <label><input type="checkbox" name="features" value="Locker Room"> Locker Room</label>
                            <label><input type="checkbox" name="features" value="Flood Lights"> Flood Lights</label>
                            <label><input type="checkbox" name="features" value="Covered Seating"> Covered Seating</label>
                            <label><input type="checkbox" name="features" value="Water Station"> Water Station</label>
                            <label><input type="checkbox" name="features" value="Training Equipment"> Training Equipment</label>
                            <label><input type="checkbox" name="features" value="Score Board"> Score Board</label>
                            <label><input type="checkbox" name="features" value="First Aid Kit"> First Aid Kit</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="courtImage">Court Image</label>
                        <input type="file" id="courtImage" name="courtImage" accept="image/*" required>
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