<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pricing - Hoops Heaven</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pricing.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>

    <!-- Pricing Section -->
    <section class="pricing-section">
        <div class="pricing-container">
            <h1>Simple, Transparent Pricing</h1>
            <p class="subtitle">Choose the plan that best suits your needs</p>

            <div class="pricing-plans">
                <!-- Pay Per Hour -->
                <div class="pricing-card">
                    <h3>Pay Per Hour</h3>
                    <p class="price">NPR 800-1,500 /hour</p>
                    <p class="description">Perfect for casual players</p>
                    <ul class="features">
                        <li>Access to all courts</li>
                        <li>No member required</li>
                        <li>Flexible scheduling</li>
                        <li>Basic equipment included</li>
                        <li>Online booking system</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/booking" class="get-started-btn">Get Started</a>
                </div>

                <!-- Monthly Member -->
                <div class="pricing-card">
                    <h3>Monthly Member</h3>
                    <p class="price">NPR 12,000 /month</p>
                    <p class="description">Great for regular players</p>
                    <ul class="features">
                        <li>20 hours of court time per month</li>
                        <li>Priority booking</li>
                        <li>Access to all courts</li>
                        <li>Premium equipment included</li>
                        <li>Locker access</li>
                        <li>10% discount on additional hours</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/register" class="get-started-btn">Get Started</a>
                </div>

                <!-- Team Package -->
                <div class="pricing-card">
                    <h3>Team Package</h3>
                    <p class="price">NPR 25,000 /month</p>
                    <p class="description">Ideal for basketball teams</p>
                    <ul class="features">
                        <li>40 hours of court time per month</li>
                        <li>Priority booking</li>
                        <li>Access to all courts</li>
                        <li>Premium equipment included</li>
                        <li>Team locker room</li>
                        <li>20% discount on additional hours</li>
                        <li>Free coaching session</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/register" class="get-started-btn">Get Started</a>
                </div>
            </div>
        </div>
    </section>

    <!-- Additional Services Section -->
    <section class="services-section">
        <div class="services-container">
            <h2>Additional Services</h2>
            <div class="services-cards">
                <div class="service-card">
                    <h3>Professional Coaching</h3>
                    <p>One-on-one training with experienced coaches</p>
                    <p class="price">NPR 2,000 /hour</p>
                </div>
                <div class="service-card">
                    <h3>Equipment Rental</h3>
                    <p>High-quality basketballs and training gear</p>
                    <p class="price">Starting at NPR 200</p>
                </div>
                <div class="service-card">
                    <h3>Tournament Organization</h3>
                    <p>Full support for organizing basketball tournaments</p>
                    <p class="price">Custom pricing</p>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="footer.jsp"/>
</body>
</html>