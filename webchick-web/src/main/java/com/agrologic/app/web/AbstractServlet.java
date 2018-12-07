package com.agrologic.app.web;

import com.agrologic.app.service.LocaleService;
import com.agrologic.app.service.MessageLocaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AbstractServlet extends HttpServlet {
    protected MessageLocaleService messageLocaleService;
    protected LocaleService localeService;

    /**
     * Logger for class and subclasses
     */
    final Logger logger = LoggerFactory.getLogger(getClass());

    protected ResourceBundle getMessages(HttpServletRequest request) {
        if (messageLocaleService == null) {
            Locale currLocale = (Locale) request.getSession().getAttribute("currLocale");
            messageLocaleService = new MessageLocaleService(currLocale);
        }
        return messageLocaleService.getMessages();
    }

    protected ResourceBundle getDefaultMessages(HttpServletRequest request) {
        if (messageLocaleService == null) {
            Locale currLocale = (Locale) request.getSession().getAttribute("currLocale");
            messageLocaleService = new MessageLocaleService(currLocale);
        }
        return messageLocaleService.getDefaultLocaleMessages();
    }

    protected void setInfoMessage(HttpServletRequest request, String logText, String messageText) {
        logger.info(logText);
        request.setAttribute("message", messageText);
        request.setAttribute("error", false);

    }

    protected void setErrorMessage(HttpServletRequest request, String logText, String messageText, Exception e) {
        logger.info(logText, e);
        request.setAttribute("message", messageText);
        request.setAttribute("error", true);
    }

    protected Long getInSessionLanguageId(HttpServletRequest request) {
        Locale currLocale = (Locale) request.getSession().getAttribute("currLocale");
        String lang = currLocale.getLanguage();
        if (localeService == null) {
            localeService = new LocaleService();
        }

        try {
            return localeService.getLanguageId(lang);
        } catch (SQLException e) {
            return 1L;// default language id
        }
    }
}


