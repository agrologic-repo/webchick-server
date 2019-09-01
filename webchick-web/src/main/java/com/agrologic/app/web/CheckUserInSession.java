package com.agrologic.app.web;

import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CheckUserInSession {
    public static boolean isUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        boolean result = user != null;
        if(result == true) {
            if(user.getUserRole().equals(UserRole.USER)) {
                try {
                    Long userId = Long.valueOf(request.getParameter("userId"));
                    if (user.getId().equals(userId)) {
                        result = true;
                    } else {
                        result = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = true;
                }
            }
        }
        return result;
    }

    public static boolean isUserInSession(ServletContext ctx) {
        User user = (User) ctx.getAttribute("user");
        return user != null;
    }
}



