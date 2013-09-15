package com.agrologic.app.web;

import com.agrologic.app.model.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CheckUserInSession {
    public static boolean isUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return user != null;
    }

    public static boolean isUserInSession(ServletContext ctx) {
        User user = (User) ctx.getAttribute("user");
        return user != null;
    }
}



