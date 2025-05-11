<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                                    <td><c:out value="${feedback.userName}"/></td>
                                    <td><c:out value="${feedback.feedbackText}"/></td>
                                    <td>
                                        <c:if test="${not empty feedback.submissionDate}">
                                            <fmt:formatDate value="${feedback.submissionDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </c:if>
                                        <c:if test="${empty feedback.submissionDate}">
                                            N/A
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3">No feedback available.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>