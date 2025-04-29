<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - Court Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contactus.css" type="text/css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <section class="contact-section">
        <div class="contact-container">
            <h1>Contact Us</h1>
            <p>We’d love to hear from you! Fill out the form below or reach out to us directly.</p>
            
            <div class="contact-content">
                <div class="contact-form">
                    <h2>Send Us a Message</h2>
                    <form action="${pageContext.request.contextPath}/contactus" method="post" onsubmit="return validateContactForm()">
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        <div class="form-group">
                            <label for="message">Message</label>
                            <textarea id="message" name="message" rows="5" required></textarea>
                        </div>
                        <button type="submit">Send Message</button>
                    </form>
                </div>
                <div class="contact-info">
                    <h2>Contact Information</h2>
                    <p><strong>Email:</strong> support@courtcontrol.com</p>
                    <p><strong>Phone:</strong> +977 9803639288 </p>
                    <p><strong>Address:</strong> Kalanki, Kathmandu, Nepal</p>
                </div>
            </div>
        </div>
    </section>
    
    <jsp:include page="footer.jsp"/>
    
    <script>
        function validateContactForm() {
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const message = document.getElementById('message').value;
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (name.trim().length === 0) {
                alert('Name cannot be empty.');
                return false;
            }
            if (!emailRegex.test(email)) {
                alert('Please enter a valid email address.');
                return false;
            }
            if (message.trim().length === 0) {
                alert('Message cannot be empty.');
                return false;
            }
            return true;
        }
    </script>
</body>
</html>