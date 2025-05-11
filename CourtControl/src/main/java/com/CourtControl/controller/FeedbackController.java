package com.CourtControl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.CourtControl.service.FeedbackService;
import com.CourtControl.model.FeedbackModel;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class FeedbackController
 */
@WebServlet(
		asyncSupported = true, 
		urlPatterns = {  
				"/admin/feedback"
		})
public class FeedbackController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FeedbackService feedbackService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FeedbackController() {
		super();
		this.feedbackService = new FeedbackService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Fetch feedback data from the database
		List<FeedbackModel> feedbackList = feedbackService.getAllFeedback();
		
		// Set feedback list as request attribute
		request.setAttribute("feedbackList", feedbackList);
		
		// Forward to JSP
		request.getRequestDispatcher("/WEB-INF/pages/admin/feedback.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}