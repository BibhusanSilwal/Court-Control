package com.CourtControl.util;

import com.CourtControl.model.CustomerModel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    /**
     * Sets a session attribute with the specified name and value.
     *
     * @param request HttpServletRequest object
     * @param name    Name of the session attribute
     * @param value   Value of the session attribute
     */
    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        HttpSession session = request.getSession(true);
        session.setAttribute(name, value);
    }

    public static Object getAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        return (session != null) ? session.getAttribute(name) : null;
    }
}