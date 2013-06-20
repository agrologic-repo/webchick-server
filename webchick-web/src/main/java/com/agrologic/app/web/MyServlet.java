/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author Administrator
 */
public class MyServlet extends HttpServlet implements Runnable {

    long lastprime = 0;
    Date lastprimeModified = new Date();
    Thread searcher;

    public void init() throws ServletException {
        searcher = new Thread(this);
        searcher.setPriority(Thread.MIN_PRIORITY);
        searcher.start();
    }

    public void run() {
        long candidate = 2;

        while (true) {
            candidate += 2;
            try {
                searcher.sleep(5000);
            } catch (InterruptedException ignored) {

            }
            lastprime = 1;
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        if (lastprime == 0) {
            out.println("Still searching for first prime...");
        } else {
            out.println("The last prime discovered was " + lastprime);
            out.println(" at " + lastprimeModified);
        }
    }

    public void destroy() {
        searcher.stop();
    }
}
