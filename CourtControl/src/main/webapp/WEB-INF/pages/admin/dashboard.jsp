<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <main class="main-content">
            <header class="main-header">
                <h1>Dashboard</h1>
                <p>An overview of your basketball court management system.</p>
            </header>
            <section class="metrics">
                <div class="metric-card">
                    <h3>Total Bookings</h3>
                    <p class="metric-value">1,284</p>
                    <p class="metric-change positive">+12% from last month</p>
                </div>
                <div class="metric-card">
                    <h3>Revenue</h3>
                    <p class="metric-value">NPR 156,000</p>
                    <p class="metric-change positive">+8% from last month</p>
                </div>
                <div class="metric-card">
                    <h3>Active Users</h3>
                    <p class="metric-value">521</p>
                    <p class="metric-change positive">+18% from last month</p>
                </div>
                <div class="metric-card">
                    <h3>Avg. Session</h3>
                    <p class="metric-value">1.8 hours</p>
                    <p class="metric-change positive">+5% from last month</p>
                </div>
            </section>
            <section class="charts">
                <div class="chart-card">
                    <div class="chart-header">
                        <h3>Bookings by Court</h3>
                        <select>
                            <option>Last 7 days</option>
                            <option>Last 30 days</option>
                            <option>Last 90 days</option>
                        </select>
                    </div>
                    <div class="chart-placeholder">
                        <p>Chart visualization</p>
                    </div>
                </div>
                <div class="chart-card">
                    <div class="chart-header">
                        <h3>Revenue Overview</h3>
                        <select>
                            <option>Last 7 days</option>
                            <option>Last 30 days</option>
                            <option>Last 90 days</option>
                        </select>
                    </div>
                    <div class="chart-placeholder">
                        
                        <p>Chart visualization</p>
                    </div>
                </div>
            </section>
            <section class="recent-bookings">
                <div class="recent-bookings-header">
                    <h3>Recent Bookings</h3>
                    <a href="${pageContext.request.contextPath}/admin/bookings" class="view-all">View all</a>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Customer</th>
                            <th>Court</th>
                            <th>Date & Time</th>
                            <th>Status</th>
                            <th>Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="booking" items="${bookings}">
                            <tr>
                                <td>${booking.customerName}<br>${booking.customerPhone}</td>
                                <td>${booking.courtName}</td>
                                <td>${booking.bookingDate}<br>${booking.timeSlot}</td>
                                <td><span class="status ${booking.status.toLowerCase()}">${booking.status}</span></td>
                                <td>${booking.price}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bookings}">
                            <tr>
                                <td colspan="5">No recent bookings found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </main>
    </div>
</body>
</html>