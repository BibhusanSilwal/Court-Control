<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book a Court - Hoops Heaven</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>

    <section class="booking-section">
        <div class="booking-container">
            <h1>Book a Court</h1>
            <p class="subtitle">Select your preferred court, date, and time</p>

            <c:if test="${not empty error}">
                <p class="error-message">${error}</p>
            </c:if>

            <div class="booking-columns">
                <!-- Left Column: Select a Court -->
                <div class="booking-left">
                    <div class="booking-step">
                        <h2>1. Select a Court</h2>
                        <div class="court-selection">
                            <c:forEach var="court" items="${courts}">
                                <div class="court-card" data-court-id="${court.id}" onclick="selectCourt(this, '${court.id}', '${court.name}', '${court.price}')">
                                    <img src="${pageContext.request.contextPath}/resources/images/${court.image}" alt="${court.name}">
                                    <h3>${court.name}</h3>
                                    <p>${court.description}</p>
                                    <p class="price">NPR ${court.price} /hour</p>
                                    <span class="selected-badge">Selected</span>
                                </div>
                            </c:forEach>
                            <!-- Static fallback if courts are not set -->
                            <c:if test="${empty courts}">
                                <div class="court-card" data-court-id="1" onclick="selectCourt(this, '1', 'Indoor Court', '1200')">
                                    <img src="${pageContext.request.contextPath}/resources/images/indoorcourt.jpg" alt="Indoor Court">
                                    <h3>Indoor Court</h3>
                                    <p>Climate-controlled indoor court with professional flooring and lighting.</p>
                                    <p class="price">NPR 1200 /hour</p>
                                    <span class="selected-badge">Selected</span>
                                </div>
                                <div class="court-card" data-court-id="2" onclick="selectCourt(this, '2', 'Outdoor Court', '800')">
                                    <img src="${pageContext.request.contextPath}/resources/images/outdoorcourt.jpg" alt="Outdoor Court">
                                    <h3>Outdoor Court</h3>
                                    <p>Premium outdoor court with high-quality surface and adjustable hoops.</p>
                                    <p class="price">NPR 800 /hour</p>
                                    <span class="selected-badge">Selected</span>
                                </div>
                                <div class="court-card" data-court-id="3" onclick="selectCourt(this, '3', 'Training Court', '1500')">
                                    <img src="${pageContext.request.contextPath}/resources/images/trainingcourt.jpg" alt="Training Court">
                                    <h3>Training Court</h3>
                                    <p>Specialized court with training equipment and shooting machines.</p>
                                    <p class="price">NPR 1500 /hour</p>
                                    <span class="selected-badge">Selected</span>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <!-- Right Column: Date, Time, and Summary -->
                <div class="booking-right">
                    <!-- Step 2: Select a Date -->
                    <div class="booking-step">
                        <h2>2. Select a Date</h2>
                        <div class="date-selection">
                            <input type="date" id="booking-date" onchange="selectDate(this)" min="2025-04-23">
                        </div>
                    </div>

                    <!-- Step 3: Select a Time Slot -->
                    <div class="booking-step">
                        <h2>3. Select a Time Slot</h2>
                        <div class="time-selection">
                            <div class="time-slots-header">
                                <span>Available Time Slots</span>
                            </div>
                            <div class="time-slots">
                                <c:forEach var="timeSlot" items="${timeSlots}">
                                    <div class="time-slot" onclick="selectTimeSlot(this, '${timeSlot}')">${timeSlot}</div>
                                </c:forEach>
                                <!-- Static fallback if time slots are not set -->
                                <c:if test="${empty timeSlots}">
                                    <div class="time-slot" onclick="selectTimeSlot(this, '6:00 AM - 7:00 AM')">6:00 AM - 7:00 AM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '7:00 AM - 8:00 AM')">7:00 AM - 8:00 AM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '8:00 AM - 9:00 AM')">8:00 AM - 9:00 AM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '9:00 AM - 10:00 AM')">9:00 AM - 10:00 AM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '10:00 AM - 11:00 AM')">10:00 AM - 11:00 AM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '11:00 AM - 12:00 PM')">11:00 AM - 12:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '12:00 PM - 1:00 PM')">12:00 PM - 1:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '1:00 PM - 2:00 PM')">1:00 PM - 2:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '2:00 PM - 3:00 PM')">2:00 PM - 3:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '3:00 PM - 4:00 PM')">3:00 PM - 4:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '4:00 PM - 5:00 PM')">4:00 PM - 5:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '5:00 PM - 6:00 PM')">5:00 PM - 6:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '6:00 PM - 7:00 PM')">6:00 PM - 7:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '7:00 PM - 8:00 PM')">7:00 PM - 8:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '8:00 PM - 9:00 PM')">8:00 PM - 9:00 PM</div>
                                    <div class="time-slot" onclick="selectTimeSlot(this, '9:00 PM - 10:00 PM')">9:00 PM - 10:00 PM</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
					
                    <!-- Booking Summary -->
                    <div class="booking-summary">
                        <h2>Booking Summary</h2>
                        <div class="summary-item">
                            <span>Court:</span>
                            <span id="summary-court">Not selected</span>
                        </div>
                        <div class="summary-item">
                            <span>Date:</span>
                            <span id="summary-date">Not selected</span>
                        </div>
                        <div class="summary-item">
                            <span>Time:</span>
                            <span id="summary-time">Not selected</span>
                        </div>
                        <c:if test="${not empty success}">
        <div class="success-message">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <!-- User is logged in, show Book Now button -->
                                <form id="booking-form" action="${pageContext.request.contextPath}/booking" method="POST">
                                    <input type="hidden" name="courtId" id="court-id" value="">
                                    <input type="hidden" name="courtName" id="court-name" value="">
                                    <input type="hidden" name="bookingDate" id="booking-date-hidden" value="">
                                    <input type="hidden" name="timeSlot" id="time-slot" value="">
                                    <button type="submit" id="book-btn" class="book-btn" disabled>Book Now</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <!-- User is not logged in, show Login to Book button -->
                                <a href="${pageContext.request.contextPath}/login" id="book-btn" class="book-btn" disabled>Login to Book</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="footer.jsp"/>

    <script>
        let selectedCourtId = null;
        let selectedCourtName = null;
        let selectedDate = null;
        let selectedTime = null;

        function selectCourt(element, courtId, courtName, courtPrice) {
            // Deselect previous selection
            document.querySelectorAll('.court-card').forEach(card => card.classList.remove('selected'));
            // Select current
            element.classList.add('selected');
            selectedCourtId = courtId;
            selectedCourtName = courtName;
            document.getElementById('summary-court').textContent = courtName;
            document.getElementById('court-id').value = courtId;
            document.getElementById('court-name').value = courtName;
            updateBookButton();
        }

        function selectDate(element) {
            selectedDate = element.value;
            document.getElementById('summary-date').textContent = selectedDate;
            document.getElementById('booking-date-hidden').value = selectedDate;
            updateBookButton();
        }

        function selectTimeSlot(element, timeSlot) {
            // Deselect previous selection
            document.querySelectorAll('.time-slot').forEach(slot => slot.classList.remove('selected'));
            // Select current
            element.classList.add('selected');
            selectedTime = timeSlot;
            document.getElementById('summary-time').textContent = timeSlot;
            document.getElementById('time-slot').value = timeSlot;
            updateBookButton();
        }

        function updateBookButton() {
            const bookBtn = document.getElementById('book-btn');
            if (selectedCourtId && selectedCourtName && selectedDate && selectedTime) {
                bookBtn.removeAttribute('disabled');
                bookBtn.classList.add('enabled');
            } else {
                bookBtn.setAttribute('disabled', 'disabled');
                bookBtn.classList.remove('enabled');
            }
        }
    </script>
</body>
</html>