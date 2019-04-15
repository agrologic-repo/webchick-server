package com.agrologic.app.web.controller;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RemoteMainScreenController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CellinkDao cellinkDao;

    @Autowired
    public RemoteMainScreenController(CellinkDao cellinkDao) {
        this.cellinkDao = cellinkDao;
    }

    @RequestMapping(value = {"/rmctrl-main-screen.html","/rmctrl-controller-screens-ajax.html"}, method = RequestMethod.GET)
    public ModelAndView showRemoteMainScreen(HttpServletRequest request,
                                             @RequestParam(value = "userId", defaultValue = "") Long userId,
                                             @RequestParam(value = "cellinkId", defaultValue = "") Long cellinkId,
                                             @RequestParam(value = "controllerId", defaultValue = "") Long controllerId,
                                             @RequestParam(value = "screenId", defaultValue = "") Long screenId) {
        Cellink cellink = null;
        try {
            cellink = cellinkDao.getById(cellinkId);
        } catch (SQLException ex) {
            logger.error("Database error", ex);
        }

        Map<String, Object> pageModel = new HashMap<String, Object>();
        pageModel.put("cellink", cellink);
        pageModel.put("userId", userId);
        pageModel.put("controllerId", controllerId);
        pageModel.put("screenId", screenId);

        String servletPath = request.getServletPath();
        if(servletPath.toLowerCase().equals("/rmctrl-main-screen.html".toLowerCase())){
            return new ModelAndView("rmctrl-main-screen", pageModel);
        } else {
            return new ModelAndView("rmctrl-controller-screens-ajax", pageModel);
        }
    }

    @RequestMapping(value = {"/rmctrl-main-screen.html", "/rmctrl-controller-screens-ajax.html"}, method = RequestMethod.POST)
    public String changeValue(HttpServletRequest request, @RequestParam MultiValueMap<String, String> allReqParams){

        Long userId = Long.parseLong((String)request.getSession().getAttribute("userId"));
        Long cellinkId = Long.parseLong((String)request.getSession().getAttribute("cellinkId"));

        try {
            String contIdScrId = sendData(allReqParams);
            if (contIdScrId != null) {
                String[] arr = contIdScrId.split(",");
                String servletPath = request.getServletPath();
                if (servletPath.equals("/rmctrl-main-screen.html")) {
                    return "redirect:./rmctrl-main-screen.html?userId=" + userId + "&cellinkId=" + cellinkId;
                } else {
                    return "redirect:./rmctrl-controller-screens-ajax.html?userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + arr[0] + "&screenId=" + arr[1];
                }
            } else {
                logger.info("Error occurs while changing data");
                return "redirect:./rmctrl-main-screen.html?userId=" + userId + "&cellinkId=" + cellinkId;
            }
        } catch (SQLException e) {
            logger.info("Error occurs while changing data", e);
            return "redirect:./rmctrl-main-screen.html?userId=" + userId + "&cellinkId=" + cellinkId;
        }
    }

    public String clearDots(String s) {
        String str = s.replace(".", "");

        str = str.replace(":", "");
        str = str.replace("/", "");

        return str;
    }

    private String sendData(MultiValueMap<String, String> allReqParams) throws SQLException{
        String retStr = null;

        for(Map.Entry<String, List<String>> entry : allReqParams.entrySet()){

            String [] contIdScrIdDataId = entry.getKey().split(",");
            retStr = contIdScrIdDataId[0] + "," + contIdScrIdDataId[1];

            if(contIdScrIdDataId.length == 3) {

                Long controllerId = Long.parseLong(contIdScrIdDataId[0]);
                Long dataId = Long.parseLong(contIdScrIdDataId[2].trim());

                int size = entry.getValue().size();

                    for (String value : entry.getValue()) {

                        if (size == 1) {

                            if ((value != null) && !value.equals("") && !(value.equals("-1"))) {

                                value = clearDots(value);
                                Long valueTch = Long.parseLong(value.trim());
                                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                                Long valueO = controllerDao.getControllerDataValue(dataId, controllerId);

                                Data data = null;

                                try {
                                    data = dataDao.getById(dataId);

                                    switch (data.getFormat()) {
                                        case DataFormat.TIME:

                                            valueO = DataFormat.convertToTimeFormat(valueO);

                                            break;

                                        case DataFormat.DEC_4:

                                            break;

                                        case DataFormat.DEC_5:

                                            valueO = DataFormat.convertToPositiveValue(valueO);

                                            break;
                                    }

                                } catch (EmptyResultDataAccessException e) {
                                    return retStr = null;
                                }

                                if (!(valueTch.equals(valueO))) {

                                    data.setValueFromUI(valueTch);
                                    controllerDao.sendNewDataValueToController(controllerId, data.getId(), data.getValue());
                                    controllerDao.saveNewDataValueOnController(controllerId, data.getId(), data.getValue());
                                    logger.info("Data successfully changed :" + data);

                                }
                            }
                        }
                    }
                }
            }
        return  retStr;
    }
}