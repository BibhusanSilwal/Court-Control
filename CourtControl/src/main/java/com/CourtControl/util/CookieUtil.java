package com.CourtControl.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * Adds a cookie to the response with the specified name, value, and max age.
     *
     * @param response HttpServletResponse object
     * @param name     Name of the cookie
     * @param value    Value of the cookie
     * @param maxAge   Max age of the cookie in seconds
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/"); // Cookie is available for the entire app
        response.addCookie(cookie);
    }

    /**
     * Removes a cookie by setting its max age to 0.
     *
     * @param response HttpServletResponse object
     * @param name     Name of the cookie to remove
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}