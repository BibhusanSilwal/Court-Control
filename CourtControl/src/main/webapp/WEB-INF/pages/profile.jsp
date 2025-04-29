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
                <form action="${pageContext.request.contextPath}/updateProfile" method="post">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" value="${not empty sessionScope.user and not empty sessionScope.user.userName ? sessionScope.user.userName : 'bibhusan silwal'}" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value="${not empty sessionScope.user and not empty sessionScope.user.email ? sessionScope.user.email : 'bibhusansilwal62@gmail.com'}" required>
                    </div>
                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text" name="phone" value="${not empty sessionScope.user and not empty sessionScope.user.number ? sessionScope.user.number : '+977 9803639288'}" required>
                    </div>
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelEdit()">Cancel</button>
                        <button type="submit" class="save-btn">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="booking-section">
            <h2>Booking History</h2>
            <c:choose>
                <c:when test="${not empty bookings}">
                    <c:forEach var="booking" items="${bookings}">
                        <div class="booking">
                            <div class="booking-details">
                                <span>${booking.courtType}</span>
                                <div class="date-time">
                                    <span class="icon calendar-icon"></span> ${booking.date}
                                    <span class="icon clock-icon"></span> ${booking.time}
                                </div>
                            </div>
                            <div class="status ${booking.status == 'Completed' ? 'completed' : 'upcoming'}">${booking.status}</div>
                            <div class="price">NPR ${booking.price}</div>
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