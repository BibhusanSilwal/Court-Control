<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hoops Heaven - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <!-- Hero Section -->
    <section class="hero-section">
        <div class="hero-content">
            <h1>Welcome to <span>Hoops Heaven</span></h1>
            <p>Kathmandu's premier basketball facility. Experience world-class courts and seamless booking with our Court Control system.</p>
            <div class="hero-buttons">
                <a href="${pageContext.request.contextPath}/booking" class="hero-btn hero-book-btn" aria-label="Book a basketball court">Book a Court</a>
                <a href="${pageContext.request.contextPath}/pricing" class="hero-btn hero-price-btn" aria-label="View pricing details">View Pricing</a>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section">
        <div class="stats-container">
            <div class="stat-item">
                <h3>4+</h3>
                <p>Professional Courts</p>
            </div>
            <div class="stat-item">
                <h3>16h</h3>
                <p>Daily Operation</p>
            </div>
            <div class="stat-item">
                <h3>500+</h3>
                <p>Active Members</p>
            </div>
            <div class="stat-item">
                <h3>4.9</h3>
                <p>User Rating</p>
            </div>
        </div>
    </section>

    <!-- Why Choose Section -->
    <section class="why-choose-section">
        <h2>Why Choose Hoops Heaven?</h2>
        <p class="subtitle">The best basketball experience in Kathmandu</p>
        <div class="why-choose-container">
            <div class="why-choose-card">
                <div class="icon">📍</div>
                <h3>Prime Location</h3>
                <p>Conveniently located in the heart of Kathmandu with easy access and parking.</p>
            </div>
            <div class="why-choose-card">
                <div class="icon">📅</div>
                <h3>Easy Booking</h3>
                <p>Book your court online with our simple Court Control system.</p>
            </div>
            <div class="why-choose-card">
                <div class="icon">⏰</div>
                <h3>Extended Hours</h3>
                <p>Open early and late to accommodate your schedule, seven days a week.</p>
            </div>
            <div class="why-choose-card">
                <div class="icon">💳</div>
                <h3>Flexible Payment</h3>
                <p>Multiple payment options available online and in-person.</p>
            </div>
        </div>
    </section>

    <!-- Courts Section -->
    <section class="courts-section">
        <h2>Our World-Class Courts</h2>
        <p class="subtitle">Professional-grade courts for players of all levels</p>
        <div class="courts-container">
            <div class="court-card">
                <img src="${pageContext.request.contextPath}/resources/images/indoorcourt.jpg" alt="Indoor Court">
                <h3>Indoor Court</h3>
                <p>Climate-controlled indoor court with professional flooring and lighting.</p>
                <a href="${pageContext.request.contextPath}/booking" class="court-btn">Book this court</a>
            </div>
            <div class="court-card">
                <img src="${pageContext.request.contextPath}/resources/images/outdoorcourt.jpg" alt="Outdoor Court">
                <h3>Outdoor Court</h3>
                <p>Premium outdoor court with durable surface and adjustable hoops.</p>
                <a href="${pageContext.request.contextPath}/booking" class="court-btn">Book this court</a>
            </div>
            <div class="court-card">
                <img src="${pageContext.request.contextPath}/resources/images/trainingcourt.jpg" alt="Training Court">
                <h3>Training Court</h3>
                <p>Specialized court with training equipment and shooting machines.</p>
                <a href="${pageContext.request.contextPath}/booking" class="court-btn">Book this court</a>
            </div>
        </div>
    </section>

    <!-- Testimonials Section -->
    <section class="testimonials-section">
        <h2>What Our Customers Say</h2>
        <div class="testimonials-container">
            <div class="testimonial-card">
                <img src="${pageContext.request.contextPath}/resources/images/customer1.jpg" alt="Customer 1">
                <h3>Aarav Sharma</h3>
                <p class="rating">★★★★★</p>
                <p>"The Court Control system makes booking so easy! I can reserve my preferred court from my phone in seconds."</p>
            </div>
            <div class="testimonial-card">
                <img src="${pageContext.request.contextPath}/resources/images/customer2.jpg" alt="Customer 2">
                <h3>Priya Thapa</h3>
                <p class="rating">★★★★★</p>
                <p>"Hoops Heaven has the best courts in Kathmandu. The indoor court is perfect during monsoon!"</p>
            </div>
            <div class="testimonial-card">
                <img src="${pageContext.request.contextPath}/resources/images/customer3.jpg" alt="Customer 3">
                <h3>Rajesh Magar</h3>
                <p class="rating">★★★★★</p>
                <p>"I've been playing basketball for years, and Hoops Heaven is by far the best facility in Nepal. Court Control makes scheduling games so simple."</p>
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
        <div class="cta-content">
            <h2>Ready to hit the court?</h2>
            <p>Book your session today.</p>
            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/booking" class="cta-btn cta-book-btn">Book Now</a>
                <a href="${pageContext.request.contextPath}/pricing" class="cta-btn cta-price-btn">View Pricing</a>
            </div>
        </div>
    </section>

    <jsp:include page="footer.jsp"/>
</body>
</html>