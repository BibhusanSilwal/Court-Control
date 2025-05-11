<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Courts Management - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/courts.css">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="header">
                <h1>Courts Management</h1>
                <a href="${pageContext.request.contextPath}/admin/addcourt" class="add-btn">+ Add New Court</a>
            </div>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <div class="courts-grid">
                <c:choose>
                    <c:when test="${not empty courtList}">
                        <c:forEach var="court" items="${courtList}">
                            <div class="court-card">
                                <div class="court-image">
                                    <img src="${court.imageUrl}" alt="${court.courtName}">
                                    <div class="court-status ${court.status.toLowerCase()}">${court.status}</div>
                                </div>
                                <div class="court-details">
                                    <h3>${court.courtName}</h3>
                                    <div class="court-type">Type: ${court.courttypeName}</div>
                                    <div class="court-price">NPR ${court.courtprice}/hour</div>
                                    <div class="court-features">
                                        <h4>Features:</h4>
                                        <ul>
                                            <c:forEach var="feature" items="${court.features}">
                                                <li>${feature}</li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                    <div class="court-actions">
                                        <a href="${pageContext.request.contextPath}/admin/editCourt?id=${court.id}" class="edit-btn">✏️</a>
                                        <a href="${pageContext.request.contextPath}/admin/deleteCourt?id=${court.id}" class="delete-btn">🗑️</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Fallback Static Court Data -->
                        <div class="court-card">
                            <div class="court-image">
                                <img src="${pageContext.request.contextPath}/resources/images/indoorcourt.jpg" alt="Indoor Court A">
                                <div class="court-status available">Available</div>
                            </div>
                            <div class="court-details">
                                <h3>Indoor Court A</h3>
                                <div class="court-type">Type: Indoor</div>
                                <div class="court-price">NPR 1200/hour</div>
                                <div class="court-features">
                                    <h4>Features:</h4>
                                    <ul>
                                        <li>Air Conditioning</li>
                                        <li>Professional Flooring</li>
                                        <li>Locker Room</li>
                                    </ul>
                                </div>
                                <div class="court-actions">
                                    <a href="#" class="edit-btn">✏️</a>
                                    <a href="#" class="delete-btn">🗑️</a>
                                </div>
                            </div>
                        </div>
                        <!-- Add other static court cards as needed -->
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>