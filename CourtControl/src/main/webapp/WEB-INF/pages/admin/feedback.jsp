<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Feedback - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminbooking.css">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="header">
                <h1>Feedback</h1>
            </div>

            <div class="table-header">
                <div class="filter">
                    <select>
                        <option>All Feedback</option>
                        <option>Recent</option>
                    </select>
                </div>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>User</th>
                        <th>Feedback</th>
                        <th>Submitted At</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty feedbackList}">
                            <c:forEach var="feedback" items="${feedbackList}">
                                <tr>
                                    <td>${feedback.userName}</td>
                                    <td>${feedback.feedbackText}</td>
                                    <td>${feedback.submittedAt}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <!-- Fallback Static Feedback Data -->
                            <tr>
                                <td>bibhusan silwal</td>
                                <td>Great service, loved the court booking system!</td>
                                <td>2023-05-10 14:30:00</td>
                            </tr>
                            <tr>
                                <td>Anup Wagle</td>
                                <td>The courts are well-maintained, but the app could be faster.</td>
                                <td>2023-05-12 09:15:00</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>