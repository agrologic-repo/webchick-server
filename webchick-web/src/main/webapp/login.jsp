<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ page import="com.agrologic.app.utils.Base64" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="javax.servlet.http.Cookie" %>

<%
    String user = "";
    String pass = "";
    String remember = "";
    if (checkCookie(request, response)) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            remember = "checked";
            Cookie c = cookies[i];
            if (c.getName().equals("name")) {
                user = c.getValue();
            }
            if (c.getName().equals("password")) {
                pass = c.getValue();
                String encpsswd = Base64.encode(pass);
            }
        }
    }

    String hosting = getHosting(request);
    Logger logger = Logger.getRootLogger();
    String domain = getDomain(request);
    String[] strings = domain.split("\\.");
    if (strings.length == 4) {
        session.setAttribute("domain", domain);
    } else {
        int dotIdx = domain.indexOf(".");
        if (dotIdx == -1) {
            dotIdx = domain.length();
        }
        domain = domain.substring(0, dotIdx);
    }
    session.setAttribute("domain", domain);
    logger.info(domain);
%>

<%!
    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
%>

<%!
    public static String getHosting(HttpServletRequest request) {
        if (request.getServerName().equals("212.235.22.246")) {
            return "al";
        } else {
            return "sr";
        }
    }
%>

<%! public static String getDomain(HttpServletRequest request) {
    return request.getServerName();
}
%>

<%! public boolean checkCookie(HttpServletRequest request, HttpServletResponse response) {
    boolean rememberme = false;
    boolean logout = false;
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
        return false;
    }

    for (int i = 0; i < cookies.length; i++) {
        Cookie cookie = cookies[i];
        if (cookie.getName().equals("logout")) {
            logout = Boolean.parseBoolean(cookie.getValue());
            if (logout == true) {
                cookie.setValue("false");
                response.addCookie(cookie);
                return false;
            }
        }

        if (cookie.getName().equals("remember")) {
            rememberme = Boolean.parseBoolean(cookie.getValue());
        }
    }
    return rememberme;
}
%>
<script language="javascript">
    function doSubmit() {
        <%  StringBuffer url = request.getRequestURL();%>
        var lang = document.loginForm.selectLang.value;
        window.location = "<%=url%>?lang=" + lang;
    }
</script>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("login.page.title")%></title>
    <meta name="Author" content="Valery Manakhimov">
    <link rel="shortcut icon" href="resources/images/favicon5.ico">
    <script language="javascript" src="resources/javascript/menu.js">;</script>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
</head>
<body>
<table align="center" valign="middle">
    <tr>
        <td align="center" valign="middle">
            <form id="loginForm" name="loginForm" action="./login.html" method="POST" style="display: inline">
                <table id="login" style="border : 1px solid #388b39;border-collapse:collapse; padding: 5px"
                       width="400px">
                    <th colspan="2"
                        style="border : 1px solid blue; ;border-collapse:collapse;background:#283d9a; color: #FFFFFF;">
                        <h3><img src="resources/images/key.png"><%=session.getAttribute("login.page.header")%></h3>
                    </th>
                    <%  String errormessage = (String) request.getAttribute("errormessage");
                        if (errormessage != null) {%>
                        <tr style="border : 1px solid blue;border-collapse:collapse;background:#FF0000">
                            <td nowrap colspan="3">
                                <span style="font-color:FFFFFF; font-weight: bold;"><%=session.getAttribute("user.password.incorrect")%>
                            </span></td>
                        </tr>
                        <%}%>
                    <tr>
                        <td width="30%" colspan="2" color="white"><br><%=session.getAttribute("user.login.info")%></td>
                    </tr>
                    <tr>
                        <td width="70%" colspan="2"><br></td>
                    </tr>
                    <tr>
                        <td width="30%"><%=session.getAttribute("label.login")%></td>
                        <td width="70%"><input type="text" size="20" name="name" maxlength="30" value="<%=user%>"></td>
                    </tr>
                    <tr>
                        <td width="30%"><%=session.getAttribute("label.password")%></td>
                        <td width="70%">
                            <input type="password" size="20" name="password" maxlength="30"value="<%=pass%>"></td>
                    </tr>
                    <tr>
                        <td width="30%"><%=session.getAttribute("user.interface.language")%></td>
                        <td width="70%" valign="bottom">
                            <select name="selectLang" onchange="doSubmit();">
                                <option value="en">English</option>
                                <option value="ru">Russian</option>
                                <%  if (domain.equalsIgnoreCase("agrologic")
                                            || domain.equalsIgnoreCase("localhost")
                                            || domain.equalsIgnoreCase("lab")
                                            || domain.equalsIgnoreCase("192.168.1.101")) {
                                %>
                                    <option value="iw">Hebrew</option>
                                <%}%>
                                    <option value="zh">Chinese</option>
                                    <option value="fr">French</option>
                                    <option value="de">German</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="30%"><%=session.getAttribute("label.remember.me")%>
                        </td>
                        <td width="70%"><input type="checkbox" id="remember" name="remember" <%=remember%>/></td>
                    </tr>
                    <tr>
                        <td width="30%">&nbsp;</td>
                        <td width="70%">
                            <button type="submit" id="loginButton" name="loginButton" style="width:120px">
                                <%=session.getAttribute("button.login")%>
                            </button>

                        </td>
                        <%=hosting%>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>

<script language="Javascript">
    var length = document.loginForm.selectLang.options.length;
    var lng = "<%=(String) request.getAttribute("lang")%>";
    for (var i = 0; i < length; i++) {
        if (document.loginForm.selectLang.options[i].value == lng) {
            document.loginForm.selectLang.selectedIndex = i;
            break;
        }
    }
</script>
</body>
</html>
