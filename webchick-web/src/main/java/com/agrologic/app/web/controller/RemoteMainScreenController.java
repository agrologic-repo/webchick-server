package com.agrologic.app.web.controller;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkCriteria;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RemoteMainScreenController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CellinkDao cellinkDao;
    private final ControllerDao controllerDao;


    @Autowired
    public RemoteMainScreenController(CellinkDao cellinkDao, ControllerDao controllerDao) {
        this.cellinkDao = cellinkDao;
        this.controllerDao = controllerDao;
    }

    @RequestMapping(value = "/overview.html", method = RequestMethod.GET)
    public ModelAndView showRemoteMainScreen(@RequestParam(value = "searchText", defaultValue = "") String searchText,
                                             @RequestParam(value = "state", required = false) Integer state,
                                             @RequestParam(value = "type", required = false) String type,
                                             @RequestParam(value = "index", defaultValue = "0") Integer index,
                                             HttpSession session) {
        User user = (User) session.getAttribute("user");

        CellinkCriteria criteria = new CellinkCriteria();
        criteria.setState(state);
        criteria.setName(searchText);
        criteria.setType(type);
        criteria.setIndex(index);
        criteria.setUserId(user.getId());
        criteria.setRole(user.getRole().getValue());
        if (user.getRole() == UserRole.DISTRIBUTOR) {
            criteria.setCompany(user.getCompany());
        }

        int count = getCount(criteria);
        int from = index * 25 + 1;
        int to = (index + 1) * 25;
        if (to > count) {
            to = count;
        }

        Map<String, Object> pageModel = new HashMap<String, Object>();
        pageModel.put("cellinks", getCellinks(criteria));
        pageModel.put("userId", user.getId());
        pageModel.put("of", count);
        pageModel.put("from", from);
        pageModel.put("to", to);
        pageModel.put("searchText", searchText);
        return new ModelAndView("overview", pageModel);
    }

    private int getCount(CellinkCriteria criteria) {
        try {
            return cellinkDao.count(criteria);
        } catch (SQLException ex) {
            logger.error("Error durring sql query running ", ex);
            return 0;
        }
    }

    private Collection<Cellink> getCellinks(CellinkCriteria criteria) {
        try {
            Collection<Cellink> cellinks = cellinkDao.getAll(criteria);
            for (Cellink cellink : cellinks) {
                Collection<com.agrologic.app.model.Controller> controllers = controllerDao.getAllByCellink(cellink.getId());
                cellink.setControllers(controllers);
            }
            return cellinks;
        } catch (SQLException ex) {
            logger.error("Error durring sql query running ", ex);
            return new ArrayList<Cellink>();
        }
    }
}
