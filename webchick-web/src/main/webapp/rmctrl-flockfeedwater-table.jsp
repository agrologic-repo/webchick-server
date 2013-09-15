<%--
    Document   : rmctrl-flockfeedwater-table.jsp
    Created on : Jan 25, 2011, 5:18:46 PM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. ®
    Version    : 0.1.1.1
--%>

<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Integer fromDay = -1;
    Integer toDay = -1;
    try {
        fromDay = Integer.parseInt(request.getParameter("fromDay"));
        if (fromDay == null) {
            fromDay = -1;
        }
        toDay = Integer.parseInt(request.getParameter("toDay"));
        if (toDay == null) {
            toDay = -1;
        }
    } catch (Exception ex) {

    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="css/calendar.css"/>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/multiopt.css"/>
</head>
<body>
<form action="./rmctrl-flockfeedwater-table.jsp">
    <input type="hidden" name="flockId" value="<%=flockId%>">
    <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
    <table cellpadding="1" cellspacing="1" border="0" width="100%">
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <h1>Feed And Water Consumption Table</h1>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <h2>Feed[KG] and Water[Lt] Consumption </h2>
                    <table border="0">
                        <tr>
                            <td>
                                From : <input type="text" size="5" name="fromDay">
                            </td>
                            <td>
                                To : <input type="text" size="5" name="toDay">
                            </td>
                            <td>
                                <input type="submit" value="Submit">
                            </td>
                        </tr>
                    </table>
                    <br/>
                    <jsp:include page="FeedWater"/>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td align="center">
                <p>
                    <button id="btnClose" name="btnClose"
                            onclick='self.close()'><%=session.getAttribute("button.close")%>
                    </button>
                </p>
            </td>
        </tr>
    </table>
</form>
</body>
</html>