package com.agrologic.app.web.controller;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.User;
import com.agrologic.app.web.AbstractServlet;
import com.agrologic.app.web.CheckUserInSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyFarms {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CellinkDao cellinkDao;
    private final ControllerDao controllerDao;


    @Autowired
    public MyFarms(CellinkDao cellinkDao, ControllerDao controllerDao) {
        this.cellinkDao = cellinkDao;
        this.controllerDao = controllerDao;
    }

    @RequestMapping(value = "/my-farms.html", method = RequestMethod.GET)
    public ModelAndView showMyFarms(@RequestParam(value = "userId") Long userId,
                                         HttpSession session, HttpServletRequest request, HttpServletResponse response) {

        User user = (User) session.getAttribute("user");
        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            try {
                response.sendRedirect("./login.jsp");
            } catch (IOException e) {
                logger.error("Unauthorized access!");
            }
        }

        Collection<Cellink> cellinks = getCellinks(userId);
        Map<String, Object> pageModel = new HashMap<String, Object>();
        pageModel.put("cellinks", cellinks);
        pageModel.put("userId", user.getId());

        return new ModelAndView("my-farms", pageModel);
    }


    private Collection<Cellink> getCellinks(Long userId) {
        try {
            Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(userId);
            for (Cellink cellink : cellinks) {
                Collection<com.agrologic.app.model.Controller> controllers = controllerDao.getAllByCellink(cellink.getId());
                cellink.setControllers(controllers);
            }
            return cellinks;
        } catch (SQLException ex) {
            logger.error("Error during sql query running ", ex);
            return new ArrayList<Cellink>();
        }
    }
}
