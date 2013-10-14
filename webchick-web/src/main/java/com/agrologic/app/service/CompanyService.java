package com.agrologic.app.service;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CompanyService {
    UserDao userDao;

    public CompanyService() {
        this.userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
    }

    public boolean isCompaniesInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Collection<String> companies = (Collection<String>) session.getAttribute("companies");
        return companies != null;
    }

    public void setCompaniesToSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            Collection<String> companies = userDao.getUserCompanies();
            session.setAttribute("companies", companies);
        } catch (SQLException e) {
            session.setAttribute("companies", new ArrayList<String>());
            e.printStackTrace();
        }
    }

}
