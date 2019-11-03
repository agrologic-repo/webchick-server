package com.agrologic.app.web.controller;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DataDao;
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
public class ClrearControllerData {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CellinkDao cellinkDao;
    private final DataDao dataDao;

    @Autowired
    public ClrearControllerData(CellinkDao cellinkDao, DataDao dataDao) {
        this.cellinkDao = cellinkDao;
        this.dataDao = dataDao;
    }

    @RequestMapping(value = "/clear-controller-data.html", method = RequestMethod.GET)
    public ModelAndView clearControllerData(@RequestParam(value = "userId") long userId,
                                            @RequestParam(value = "cellinkId") long cellinkId,
                                            @RequestParam(value = "controllerId") long controllerId) {
        Cellink cellink = null;
        try {
            cellink = cellinkDao.getById(cellinkId);
            dataDao.clearControllerData(controllerId);
        } catch (SQLException ex) {
            logger.error("Database error", ex);
        }

        Map<String, Object> pageModel = new HashMap<String, Object>();
        pageModel.put("cellink", cellink);
        pageModel.put("userId", userId);
        return new ModelAndView("rmctrl-main-screen", pageModel);
    }
}
