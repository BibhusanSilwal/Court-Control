<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" type="text/css">
    <style>
        .profile-img {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            margin-right: 8px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
    <div class="nav-bar">
        <div class="nav-links">
            <div><a href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/resources/images/HoopsHeaven.png"></a></div>
            <div><a href="${pageContext.request.contextPath}/home">Home</a></div>
            <div><a href="${pageContext.request.contextPath}/booking">Book a Court</a></div>
            <div><a href="${pageContext.request.contextPath}/pricing">Pricing</a></div>
            <div><a href="${pageContext.request.contextPath}/aboutus">About us</a></div>
            <div><a href="${pageContext.request.contextPath}/contactus">Contact us</a></div>
        </div>
        <div class="auth-section">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <div class="login"><a href="${pageContext.request.contextPath}/login">Login</a></div>
                    <div class="register"><a href="${pageContext.request.contextPath}/register">Register</a></div>
                </c:when>
                <c:otherwise>
                    <div class="user-section" style="display: flex; align-items: center;">
                        <a href="${pageContext.request.contextPath}/userprofile">
                            <img src="${pageContext.request.contextPath}/resources/images/${sessionScope.user.imageUrl}" 
                                 alt="Profile Picture" 
                                 class="profile-img">
                            <span class="user-icon" title="${sessionScope.user.userName}">
                                ${not empty sessionScope.user.userName ? sessionScope.user.userName : '?'}
                            </span>
                        </a>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>