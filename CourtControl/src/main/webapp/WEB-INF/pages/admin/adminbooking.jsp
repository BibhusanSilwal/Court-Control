<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Bookings - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminbooking.css">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <div class="main-content">
            <div class="header">
                <h1>Bookings</h1>
                <a href="${pageContext.request.contextPath}/booking" class="new-booking-btn">New Booking</a>
            </div>
            <div class="table-header">
                <div class="filter">
                    <select>
                        <option>All Bookings</option>
                        <option>Confirmed</option>
                        <option>Pending</option>
                        <option>Completed</option>
                    </select>
                </div>
                <div class="search">
                    <input type="text" placeholder="Search bookings...">
                </div>
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
                            <td colspan="5">No bookings found.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>