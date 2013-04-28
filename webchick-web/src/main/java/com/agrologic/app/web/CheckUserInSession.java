
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.model.UserDto;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Title: CheckUserInSession <br> Decription: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class CheckUserInSession {
    public static boolean isUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto     user    = (UserDto) session.getAttribute("user");

        return user != null;
    }

    public static boolean isUserInSession(ServletContext ctx) {
        UserDto user = (UserDto) ctx.getAttribute("user");

        return user != null;
    }
}



