<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <jsp:include page="sidebar.jsp"/>
        <main class="main-content">
            <header class="main-header">
                <h1>Dashboard</h1>
                <p>An overview of your basketball court management system.</p>
            </header>
            <section class="metrics">
                <div class="metric-card">
                    <h3>Total Bookings</h3>
                    <p class="metric-value">${totalBookings}</p>
                </div>
                <div class="metric-card">
                    <h3>Revenue</h3>
                    <p class="metric-value">${totalRevenue}</p>
                </div>
                <div class="metric-card">
                    <h3>Active Users</h3>
                    <p class="metric-value">${activeUsers}</p>
                </div>
                <div class="metric-card">
                    <h3>Avg. Session</h3>
                    <p class="metric-value">${avgSession}</p>
                </div>
            </section>
            <section class="charts">
                <div class="chart-card">
                    <div class="chart-header">
                        <h3>Bookings by Court</h3>
                        <select id="bookingsTimeRange" onchange="updateBookingsChart()">
                            <option value="7">Last 7 days</option>
                            <option value="30">Last 30 days</option>
                            <option value="90">Last 90 days</option>
                        </select>
                    </div>
                    <canvas id="bookingsChart" width="400" height="200"></canvas>
                </div>
                <div class="chart-card">
                    <div class="chart-header">
                        <h3>Revenue Overview</h3>
                        <select id="revenueTimeRange" onchange="updateRevenueChart()">
                            <option value="7">Last 7 days</option>
                            <option value="30">Last 30 days</option>
                            <option value="90">Last 90 days</option>
                        </select>
                    </div>
                    <canvas id="revenueChart" width="400" height="200"></canvas>
                </div>
            </section>
            <section class="recent-bookings">
                <div class="recent-bookings-header">
                    <h3>Recent Bookings</h3>
                    <a href="${pageContext.request.contextPath}/admin/bookings" class="view-all">View all</a>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Customer</th>
                            <th>Court</th>
                            <th>Date & Time</th>
                            <th>Status</th>
                            <th>Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="booking" items="${bookings}">
                            <tr>
                                <td>${booking.customerName}<br>${booking.customerPhone}</td>
                                <td>${booking.courtName}</td>
                                <td>${booking.bookingDate}<br>${booking.timeSlot}</td>
                                <td><span class="status ${booking.status.toLowerCase()}">${booking.status}</span></td>
                                <td>${booking.price}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bookings}">
                            <tr>
                                <td colspan="5">No recent bookings found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
    <script>
        // Bookings by Court Chart
        console.log("Initializing Bookings Chart");
        const bookingsCtx = document.getElementById('bookingsChart').getContext('2d');
        let bookingsChart;
        if (bookingsCtx) {
            bookingsChart = new Chart(bookingsCtx, {
                type: 'bar',
                data: {
                    labels: [<c:forEach var="label" items="${bookingsByCourtLabels}" varStatus="loop">'${label}'${loop.last ? '' : ','}</c:forEach>],
                    datasets: [{
                        label: 'Bookings',
                        data: [<c:forEach var="data" items="${bookingsByCourtData}" varStatus="loop">${data}${loop.last ? '' : ','}</c:forEach>],
                        backgroundColor: 'rgba(75, 192, 192, 0.5)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: { beginAtZero: true }
                    }
                }
            });
            console.log("Bookings Chart initialized");
        } else {
            console.error("Bookings Chart canvas not found");
        }

        // Revenue Overview Chart
        console.log("Initializing Revenue Chart");
        const revenueCtx = document.getElementById('revenueChart').getContext('2d');
        let revenueChart;
        if (revenueCtx) {
            revenueChart = new Chart(revenueCtx, {
                type: 'line',
                data: {
                    labels: [<c:forEach var="label" items="${dailyRevenueLabels}" varStatus="loop">'${label}'${loop.last ? '' : ','}</c:forEach>],
                    datasets: [{
                        label: 'Revenue (NPR)',
                        data: [<c:forEach var="data" items="${dailyRevenueData}" varStatus="loop">${data}${loop.last ? '' : ','}</c:forEach>],
                        fill: false,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        tension: 0.1
                    }]
                },
                options: {
                    scales: {
                        y: { beginAtZero: true }
                    }
                }
            });
            console.log("Revenue Chart initialized");
        } else {
            console.error("Revenue Chart canvas not found");
        }

        function updateBookingsChart() {
            const timeRange = document.getElementById('bookingsTimeRange').value;
            fetch('${pageContext.request.contextPath}/admin/chart-data?chartType=bookings&days=' + timeRange)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error('Error fetching bookings data:', data.error);
                        return;
                    }
                    bookingsChart.data.labels = data.labels;
                    bookingsChart.data.datasets[0].data = data.data;
                    bookingsChart.update();
                    console.log('Bookings Chart updated with data:', data);
                })
                .catch(error => console.error('Error fetching bookings data:', error));
        }

        function updateRevenueChart() {
            const timeRange = document.getElementById('revenueTimeRange').value;
            fetch('${pageContext.request.contextPath}/admin/chart-data?chartType=revenue&days=' + timeRange)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error('Error fetching revenue data:', data.error);
                        return;
                    }
                    revenueChart.data.labels = data.labels;
                    revenueChart.data.datasets[0].data = data.data;
                    revenueChart.update();
                    console.log('Revenue Chart updated with data:', data);
                })
                .catch(error => console.error('Error fetching revenue data:', error));
        }
    </script>
</body>
</html>