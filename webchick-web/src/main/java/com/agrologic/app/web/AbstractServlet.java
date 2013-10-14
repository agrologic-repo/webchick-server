package com.agrologic.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractServlet extends HttpServlet {
    /**
     * Logger for class and subclasses
     */
    final Logger logger = LoggerFactory.getLogger(getClass());

    protected void checkUserInSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            StringBuffer url = request.getRequestURL();
            logger.info(url.toString());
            try {
                request.getRequestDispatcher("http://localhost:8080/webchick-web/login.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}


