<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Hoops Heaven</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 0C8.954 0 0 8.954 0 20s8.954 20 20 20 20-8.954 20-20S31.046 0 20 0zm0 36C11.178 36 4 28.822 4 20S11.178 4 20 4s16 7.178 16 16-7.178 16-16 16z" fill="#1A1A1A"/>
                <path d="M28 12H12v4h16v-4zM12 24h16v4H12v-4z" fill="#1A1A1A"/>
            </svg>
        </div>
        <h2>Sign in to your account</h2>
        <p class="subtitle">Or <a href="${pageContext.request.contextPath}/register">create a new account</a></p>

        <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validateLoginForm()">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${param.username}" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
         
            <span class="error">
                <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
            </span>
            <button type="submit">Sign in</button>
        </form>
        <a href="${pageContext.request.contextPath}/" class="home-btn">Go to Home</a>
    </div>

    <script>
        function validateLoginForm() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            if (username.trim().length === 0) {
                alert('Username cannot be empty.');
                return false;
            }

            if (password.length < 6) {
                alert('Password must be at least 6 characters long.');
                return false;
            }

            return true;
        }
    </script>
</body>
</html>