<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <script>
        function toggleEditMode() {
            document.getElementById("profile-display").style.display = "none";
            document.getElementById("profile-edit").style.display = "block";
        }

        function cancelEdit() {
            document.getElementById("profile-display").style.display = "block";
            document.getElementById("profile-edit").style.display = "none";
        }
    </script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div class="container">
        <div class="profile-section">
            <h2>Profile Information <a href="#" class="edit-link" onclick="toggleEditMode()">Edit</a></h2>
            
            <!-- Display Mode -->
            <div id="profile-display">
                <c:choose>
                    <c:when test="${not empty sessionScope.user and not empty sessionScope.user.userName}">
                        <p><span class="icon user-icon"></span> ${sessionScope.user.userName}</p>
                        <p><span class="icon email-icon"></span> ${sessionScope.user.email}</p>
                        <p><span class="icon phone-icon"></span> ${sessionScope.user.number}</p>
                    </c:when>
                    <c:otherwise>
                        <!-- Fallback Static User Data -->
                        <p><span class="icon user-icon"></span> bibhusan silwal</p>
                        <p><span class="icon email-icon"></span> bibhusansilwal62@gmail.com</p>
                        <p><span class="icon phone-icon"></span> +977 9803639288</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Edit Mode -->
            <div id="profile-edit" style="display: none;">
                <form action="${pageContext.request.contextPath}/userprofile" method="post">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="username" value="${not empty username ? username : sessionScope.user.userName}" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value="${not empty email ? email : sessionScope.user.email}">
                    </div>
                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text" name="number" value="${not empty number ? number : sessionScope.user.number}">
                    </div>
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelEdit()">Cancel</button>
                        <button type="submit" class="save-btn">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="booking-section">
            <h2>Booking History</h2>
            <c:if test="${not empty successMessage}">
                <p style="color: green;">${successMessage}</p>
            </c:if>
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
            <c:choose>
                <c:when test="${not empty bookings}">
                    <c:forEach var="booking" items="${bookings}">
                        <div class="booking">
                            <div class="booking-details">
                                <span>${booking.courtName}</span>
                                <div class="date-time">
                                    <span class="icon calendar-icon"></span> ${booking.bookingDate}
                                    <span class="icon clock-icon"></span> ${booking.timeSlot}
                                </div>
                            </div>
                            <div class="status ${booking.status == 'Completed' ? 'completed' : 'upcoming'}">${booking.status}</div>
                            <div class="price">${booking.price}</div>
                            <form action="${pageContext.request.contextPath}/userprofile/delete" method="post" style="display: inline;">
                                <input type="hidden" name="bookingId" value="${booking.bookingId}">
                                <button type="submit" class="delete-btn" onclick="return confirm('Are you sure you want to delete this booking?')">Delete</button>
                            </form>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <!-- Fallback Static Booking Data -->
                    <div class="booking">
                        <div class="booking-details">
                            <span>Indoor Court</span>
                            <div class="date-time">
                                <span class="icon calendar-icon"></span> 5/15/2023
                                <span class="icon clock-icon"></span> 6:00 PM - 8:00 PM
                            </div>
                        </div>
                        <div class="status completed">Completed</div>
                        <div class="price">NPR 2400</div>
                    </div>
                    <div class="booking">
                        <div class="booking-details">
                            <span>Outdoor Court</span>
                            <div class="date-time">
                                <span class="icon calendar-icon"></span> 5/20/2023
                                <span class="icon clock-icon"></span> 4:00 PM - 5:00 PM
                            </div>
                        </div>
                        <div class="status upcoming">Upcoming</div>
                        <div class="price">NPR 800</div>
                    </div>
                    <div class="booking">
                        <div class="booking-details">
                            <span>Training Court</span>
                            <div class="date-time">
                                <span class="icon calendar-icon"></span> 5/25/2023
                                <span class="icon clock-icon"></span> 7:00 AM - 9:00 AM
                            </div>
                        </div>
                        <div class="status upcoming">Upcoming</div>
                        <div class="price">NPR 3000</div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>