<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <style>
        /* Toast Styles */
        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            background-color: #333;
            color: white;
            padding: 15px 20px;
            border-radius: 5px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            opacity: 0;
            transition: opacity 0.3s ease-in-out;
            display: none;
        }
        .toast.success {
            background-color: #28a745;
        }
        .toast.error {
            background-color: #dc3545;
        }
        .toast.show {
            display: block;
            opacity: 1;
        }
        .no-bookings {
    color: #666;
    font-style: italic;
    text-align: center;
}
    </style>
    <script>
        function toggleEditMode() {
            document.getElementById("profile-display").style.display = "none";
            document.getElementById("profile-edit").style.display = "block";
            document.getElementById("password-change").style.display = "none";
            clearToasts(); // Clear toasts when switching tabs
        }

        function togglePasswordChange() {
            document.getElementById("profile-display").style.display = "none";
            document.getElementById("profile-edit").style.display = "none";
            document.getElementById("password-change").style.display = "block";
            clearToasts(); // Clear toasts when switching tabs
        }

        function cancelEdit() {
            document.getElementById("profile-display").style.display = "block";
            document.getElementById("profile-edit").style.display = "none";
            document.getElementById("password-change").style.display = "none";
            clearToasts(); // Clear toasts when canceling
        }

        function showToast(message, type) {
            const toast = document.createElement("div");
            toast.className = `toast ${type}`;
            toast.textContent = message;
            document.body.appendChild(toast);

            setTimeout(() => {
                toast.classList.add("show");
                setTimeout(() => {
                    toast.classList.remove("show");
                    setTimeout(() => {
                        document.body.removeChild(toast);
                    }, 300); // Match transition duration
                }, 3000); // Display for 3 seconds
            }, 10);
        }

        function clearToasts() {
            const toasts = document.getElementsByClassName("toast");
            while (toasts.length > 0) {
                toasts[0].parentNode.removeChild(toasts[0]);
            }
        }

        // Show toasts on page load based on request attributes
        window.onload = function() {
            <c:if test="${not empty success}">
                showToast("${success}", "success");
            </c:if>
            <c:if test="${not empty successMessage}">
                showToast("${successMessage}", "success");
            </c:if>
            <c:if test="${not empty error}">
                showToast("${error}", "error");
            </c:if>
            <c:if test="${not empty passwordError}">
                showToast("${passwordError}", "error");
            </c:if>
        };
    </script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div class="container">
        <div class="profile-section">
            <h2>Profile Information 
                <a href="#" class="edit-link" onclick="toggleEditMode()">Edit Profile</a> 
                | <a href="#" class="edit-link" onclick="togglePasswordChange()">Change Password</a>
            </h2>
            
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

            <!-- Edit Profile Mode -->
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
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelEdit()">Cancel</button>
                        <button type="submit" class="save-btn">Save Changes</button>
                    </div>
                </form>
            </div>

            <!-- Change Password Mode -->
            <div id="password-change" style="display: none;">
                <form action="${pageContext.request.contextPath}/userprofile/changepassword" method="post">
                    <div class="form-group">
                        <label>Current Password</label>
                        <input type="password" name="currentPassword" required>
                    </div>
                    <div class="form-group">
                        <label>New Password</label>
                        <input type="password" name="newPassword" required>
                    </div>
                    <div class="form-group">
                        <label>Confirm New Password</label>
                        <input type="password" name="confirmPassword" required>
                    </div>
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelEdit()">Cancel</button>
                        <button type="submit" class="save-btn">Change Password</button>
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
                    <p>No bookings made.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>