<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aboutus.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <section class="about-section">
        <div class="about-container">
            <h1>About Us</h1>
            <p>
                Welcome to Court Control, your premier destination for managing basketball court bookings. 
                We are passionate about making sports accessible and enjoyable for everyone. Our platform 
                simplifies the process of booking courts, managing schedules, and connecting with other 
                players in your community.
            </p>
            <h2>Our Mission</h2>
            <p>
                At Court Control, our mission is to empower athletes and sports enthusiasts by providing a 
                seamless and efficient way to book basketball courts. Whether you're a casual player or a 
                competitive athlete, we’re here to ensure you have the best experience on the court.
            </p>
        </div>
    </section>
    
    <jsp:include page="footer.jsp"/>
</body>
</html>