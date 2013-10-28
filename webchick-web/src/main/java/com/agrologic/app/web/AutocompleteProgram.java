package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.model.Program;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class AutocompleteProgram extends AbstractServlet {
    private static final long serialVersionUID = 1L;
    private ProgramDao programDao;

    public AutocompleteProgram() {
        super();
        programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html charset=utf-8");

        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        String name = request.getParameter("term");
        System.out.println(name);


        Collection<Program> result = null;
        try {
            result = programDao.getAll(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        JSONArray programs = new JSONArray();
        for(Program program:result) {
            JSONObject programMap = new JSONObject();
            try {
                programMap.put("key",program.getName());
                programMap.put("value",program.getId());
            } catch (JSONException e) {

            }
            programs.put(programMap);
        }
        try {
            json.put("programsMap",programs);
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