package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class AutocompleteDataType extends AbstractServlet {
    private static final long serialVersionUID = 1L;
    private DataDao dataDao;

    public AutocompleteDataType() {
        super();
        dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html charset=utf-8");

        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        String type = request.getParameter("term");
        Long dataType = Long.parseLong(type);

        JSONArray arrayObj = new JSONArray();
        Collection<Data> dataCollection = null;
        try {
            dataCollection = dataDao.find(dataType);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Iterator<Data> iterator = dataCollection.iterator();
        while (iterator.hasNext()) {
            Data data = iterator.next();
            arrayObj.put("" + data.getType() + "");
        }
        out.println(arrayObj.toString());
        out.close();


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Do something
    }

}