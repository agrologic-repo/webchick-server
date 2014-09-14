package com.agrologic.app.web.controller;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.model.Cellink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OverviewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CellinkDao cellinkDao;

    @Autowired
    public OverviewController(CellinkDao cellinkDao) {
        this.cellinkDao = cellinkDao;
    }

    @RequestMapping(value = "/rmctrl-main-screen.html", method = RequestMethod.GET)
    public ModelAndView showRemoteMainScreen(@RequestParam(value = "userId") long userId, @RequestParam(value = "cellinkId") long cellinkId) {
        Cellink cellink = null;
        try {
            cellink = cellinkDao.getById(cellinkId);
        } catch (SQLException ex) {
            logger.error("Database error", ex);
        }

        Map<String, Object> pageModel = new HashMap<String, Object>();
        pageModel.put("cellink", cellink);
        pageModel.put("userId", userId);
        return new ModelAndView("rmctrl-main-screen", pageModel);
    }
}
