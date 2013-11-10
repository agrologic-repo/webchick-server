package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

public class AutocompleteControllerName extends AbstractServlet {
    private static final long serialVersionUID = 1L;
    private ControllerDao controllerDao;

    public AutocompleteControllerName() {
        super();
        controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html charset=utf-8");

        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        String name = request.getParameter("term");
        System.out.println(name);


        Map<String,String> result = null;
        try {
            result = controllerDao.getControllerSendStringNames(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        JSONArray sendStrings = new JSONArray();
        for(Map.Entry<String,String> entry:result.entrySet()) {
            JSONObject sendString = new JSONObject();
            try {
                sendString.put("key",entry.getKey());
                sendString.put("value",entry.getValue());
            } catch (JSONException e) {

            }
            sendStrings.put(sendString);
        }
        try {
            json.put("sendString",sendStrings);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        out.println(json.toString());
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Do something
    }

}