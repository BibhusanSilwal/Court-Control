<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logout - Hoops Heaven</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f5f5f5;
        }
        .logout-container {
            text-align: center;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .success-message {
            color: #28a745;
            font-size: 1.2em;
            margin-bottom: 20px;
        }
        .redirect-message {
            color: #6c757d;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <div class="logout-container">
        <div class="success-message">You have been logged out successfully!</div>
        <div class="redirect-message">You will be redirected to the homepage in <span id="countdown">3</span> seconds...</div>
    </div>

    <script>
        let timeLeft = 3;
        const countdown = document.getElementById('countdown');
        const timer = setInterval(() => {
            timeLeft--;
            countdown.textContent = timeLeft;
            if (timeLeft <= 0) {
                clearInterval(timer);
                window.location.href = "${pageContext.request.contextPath}/home"; // Changed to /home
            }
        }, 1000);
    </script>
</body>
</html>