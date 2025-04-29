<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
</head>
<body>
<div class="sidebar">
    <div class="sidebar-header">
        <div class="logo">
            <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 0C8.954 0 0 8.954 0 20s8.954 20 20 20 20-8.954 20-20S31.046 0 20 0zm0 36C11.178 36 4 28.822 4 20S11.178 4 20 4s16 7.178 16 16-7.178 16-16 16z" fill="#ff6200"/>
                <path d="M28 12H12v4h16v-4zM12 24h16v4H12v-4z" fill="#ff6200"/>
            </svg>
            <span>Court Control</span>
        </div>
    </div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}">
            <span class="icon">🏠</span> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin/bookings" class="nav-item ${pageContext.request.requestURI.contains('bookings') ? 'active' : ''}">
            <span class="icon">📅</span> Bookings
        </a>
        <a href="${pageContext.request.contextPath}/admin/courts" class="nav-item ${pageContext.request.requestURI.contains('courts') ? 'active' : ''}">
            <span class="icon">🏀</span> Courts
        </a>
        <a href="${pageContext.request.contextPath}/admin/analytics" class="nav-item ${pageContext.request.requestURI.contains('analytics') ? 'active' : ''}">
            <span class="icon">📊</span> Analytics
        </a>
        <a href="${pageContext.request.contextPath}/admin/feedback" class="nav-item ${pageContext.request.requestURI.contains('feedback') ? 'active' : ''}">
            <span class="icon">📝</span> Feedback
        </a>
        <a href="${pageContext.request.contextPath}/settings" class="nav-item ${pageContext.request.requestURI.contains('settings') ? 'active' : ''}">
            <span class="icon">⚙️</span> Settings
        </a>
    </nav>
    <div class="sidebar-footer">
        <div class="user-profile">
            <span class="user-role">ADMIN</span>
            <span class="user-email">${sessionScope.user != null ? sessionScope.user : "admin@gm..."}</span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">➡️</a>
        </div>
    </div>
</div>
</body>
</html>