package com.CourtControl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.CourtControl.model.FeedbackModel;
import com.CourtControl.service.FeedbackService;

/**
 * Servlet implementation class ContactusController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/contactus" })
public class ContactusController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactusController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to contactus.jsp to display the contact form
        request.getRequestDispatcher("WEB-INF/pages/contactus.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the form data from the request
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        // Get userId from session, if it exists
        HttpSession session = request.getSession(false); // false means don't create a new session
        int userId = 0; // Default to 0 if no session or userId
        if (session != null && session.getAttribute("userId") != null) {
        	userId = Integer.parseInt((String) session.getAttribute("userId"));
        }

        // Simple validation (you can enhance this with more checks if needed)
        if (name != null && !name.isEmpty() && email != null && !email.isEmpty() && message != null && !message.isEmpty()) {
            FeedbackModel feedback = new FeedbackModel(0, userId, name, email, message);
            FeedbackService fserv = new FeedbackService();
            fserv.sendFeedback(feedback);
            request.setAttribute("successMessage", "Thank you for contacting us! We'll get back to you soon.");
        } else {
            // If fields are missing, show an error message
            request.setAttribute("errorMessage", "Please fill in all fields.");
        }

        // Forward to the contact us page to show the result
        request.getRequestDispatcher("WEB-INF/pages/contactus.jsp").forward(request, response);
    }
}