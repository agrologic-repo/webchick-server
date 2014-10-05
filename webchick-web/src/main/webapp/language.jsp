<%@ page import="java.util.*" %>
<%
    String lang = request.getParameter("lang");
    String sessionLang = (String) request.getSession().getAttribute("lang");
    if (lang == null) {
        if (sessionLang == null) {
            lang = "en";
        } else {
            lang = sessionLang;
        }
    }
    request.getSession().setAttribute("lang", lang);
    request.setAttribute("lang", lang);

    Locale locale = null;
    if (lang.equals("ru")) {
        locale = new Locale("ru", "RU");
        request.getSession().setAttribute("dir", "ltr");
    } else if (lang.equals("iw")) {
        locale = new Locale("iw", "IL");
        request.getSession().setAttribute("dir", "rtl");
    } else if (lang.equals("zh")) {
        locale = new Locale("zh", "CN");
        request.getSession().setAttribute("dir", "ltr");
    } else if (lang.equals("fr")) {
        locale = new Locale("fr", "FR");
        request.getSession().setAttribute("dir", "ltr");
    } else if (lang.equals("de")) {
        locale = new Locale("de", "DE");
        request.getSession().setAttribute("dir", "ltr");
    } else if (lang.equals("tr")) {
        locale = new Locale("tr", "TR");
        request.getSession().setAttribute("dir", "ltr");
    } else {
        locale = Locale.US;
        request.getSession().setAttribute("dir", "ltr");
    }

    Locale oldLocale = (Locale) session.getAttribute("currLocale");
    session.setAttribute("currLocale", locale);

//   SessionLocaleResolver expects locale to be stored in this attribute
    session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE",locale);

    if (oldLocale == null) {
        oldLocale = locale;
    }
    session.setAttribute("oldLocale", oldLocale);


    ResourceBundle bundle = ResourceBundle.getBundle("labels", locale);
    for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
        String key = e.nextElement();
        String s = bundle.getString(key);
        session.setAttribute(key, s);
    }



%>