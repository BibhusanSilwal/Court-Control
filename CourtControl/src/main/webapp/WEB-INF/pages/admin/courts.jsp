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

            <div class="courts-grid">
                <c:choose>
                    <c:when test="${not empty courtList}">
                        <c:forEach var="court" items="${courtList}">
                            <div class="court-card">
                                <div class="court-image">
                                    <img src="${court.imageUrl}" alt="${court.name}">
                                    <div class="court-status ${court.status.toLowerCase()}">${court.status}</div>
                                </div>
                                <div class="court-details">
                                    <h3>${court.name}</h3>
                                    <div class="court-price">NPR ${court.price}/hour</div>
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
                        <div class="court-card">
                            <div class="court-image">
                                <img src="${pageContext.request.contextPath}/resources/images/outdoorcourt.jpg" alt="Outdoor Court B">
                                <div class="court-status occupied">Occupied</div>
                            </div>
                            <div class="court-details">
                                <h3>Outdoor Court B</h3>
                                <div class="court-price">NPR 800/hour</div>
                                <div class="court-features">
                                    <h4>Features:</h4>
                                    <ul>
                                        <li>Flood Lights</li>
                                        <li>Covered Seating</li>
                                        <li>Water Station</li>
                                    </ul>
                                </div>
                                <div class="court-actions">
                                    <a href="#" class="edit-btn">✏️</a>
                                    <a href="#" class="delete-btn">🗑️</a>
                                </div>
                            </div>
                        </div>
                        <div class="court-card">
                            <div class="court-image">
                                <img src="${pageContext.request.contextPath}/resources/images/trainingcourt.jpg" alt="Training Court C">
                                <div class="court-status maintenance">Maintenance</div>
                            </div>
                            <div class="court-details">
                                <h3>Training Court C</h3>
                                <div class="court-price">NPR 1000/hour</div>
                                <div class="court-features">
                                    <h4>Features:</h4>
                                    <ul>
                                        <li>Training Equipment</li>
                                        <li>Score Board</li>
                                        <li>First Aid Kit</li>
                                    </ul>
                                </div>
                                <div class="court-actions">
                                    <a href="#" class="edit-btn">✏️</a>
                                    <a href="#" class="delete-btn">🗑️</a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>