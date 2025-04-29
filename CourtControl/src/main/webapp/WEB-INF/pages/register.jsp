<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Hoops Heaven</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="register-container">
        <div class="logo">
            <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 0C8.954 0 0 8.954 0 20s8.954 20 20 20 20-8.954 20-20S31.046 0 20 0zm0 36C11.178 36 4 28.822 4 20S11.178 4 20 4s16 7.178 16 16-7.178 16-16 16z" fill="#1A1A1A"/>
                <path d="M28 12H12v4h16v-4zM12 24h16v4H12v-4z" fill="#1A1A1A"/>
            </svg>
        </div>
        <h2>Create your account</h2>
        <p class="subtitle">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>

        <form action="${pageContext.request.contextPath}/register" method="post" enctype="multipart/form-data" onsubmit="return validateRegisterForm()">
            <div class="form-grid">
                <div class="form-group">
                    <label for="fName">First Name</label>
                    <input type="text" id="fName" name="fName" value="${param.fName}" required>
                </div>
                <div class="form-group">
                    <label for="lName">Last Name</label>
                    <input type="text" id="lName" name="lName" value="${param.lName}" required>
                </div>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" value="${param.username}" required>
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth</label>
                    <input type="date" id="dob" name="dob" value="${param.dob}" required>
                </div>
                <div class="form-group full-width">
                    <label>Gender</label>
                    <div class="gender-options">
                        <label><input type="radio" name="gender" value="Male" ${param.gender == 'Male' ? 'checked' : ''} required> Male</label>
                        <label><input type="radio" name="gender" value="Female" ${param.gender == 'Female' ? 'checked' : ''}> Female</label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email">Email address</label>
                    <input type="email" id="email" name="email" value="${param.email}" required>
                </div>
                <div class="form-group">
                    <label for="number">Phone Number</label>
                    <input type="text" id="number" name="number" value="${param.number}" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" value="${param.password}" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>
                <div class="form-group full-width">
                    <label for="img">Profile Image</label>
                    <input type="file" id="img" name="img">
                </div>
            </div>

            <span class="error" style="color: red;">
                <c:if test="${not empty error}">
                    ${errorMessage}
                </c:if>
            </span>
            <span class="success" style="color: green;">
                <c:if test="${not empty success}">
                    ${success}
                </c:if>
            </span>
            <button type="submit">Create Account</button>
        </form>
        <a href="${pageContext.request.contextPath}/" class="home-btn">Go to Home</a>
    </div>

    <script>
        function validateRegisterForm() {
            const email = document.getElementById('email').value;
            const phone = document.getElementById('number').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const dob = document.getElementById('dob').value;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /^\d{10}$/;

            if (!emailRegex.test(email)) {
                alert('Please enter a valid email address.');
                return false;
            }
            if (!phoneRegex.test(phone)) {
                alert('Please enter a valid 10-digit phone number.');
                return false;
            }
            if (password.length < 6) {
                alert('Password must be at least 6 characters long.');
                return false;
            }
            if (password !== confirmPassword) {
                alert('Passwords do not match.');
                return false;
            }
            const dobDate = new Date(dob);
            const today = new Date();
            let age = today.getFullYear() - dobDate.getFullYear();
            const monthDiff = today.getMonth() - dobDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dobDate.getDate())) {
                age--;
            }
            if (age < 13) {
                alert('You must be at least 13 years old to register.');
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
