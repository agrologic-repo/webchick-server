<%--
    Document   : statistic
    Created on : Nov 10, 2011, 9:16:33 AM
    Author     : Administrator
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>

<%  UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Statistics</title>
        <head>
            <title><%=session.getAttribute("database.page.title")%></title>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <script language="javascript" src="js/general.js"></script>
            <link rel="shortcut icon" href="img/favicon5.ico"/>
            <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
            <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        </head>
    </head>
    <body>
        <div id="header">
            <%@include file="usermenuontop.jsp"%>
        </div>
        <div id="main-shell">
            <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                <tr>
                    <td>
                        <h1><%=session.getAttribute("database.page.header")%></h1>
                        <h2><%=session.getAttribute("database.page.sub.header")%></h2>
                    </td>
                    <td align="center">
                        <%@ include file="messages.jsp"%>
                    </td>
                </tr>
                <tr>
                    <td colspan="9">
                        <img border="0" src="TotalCellinkStatePieChart" width="100" height="100"></img>
                    </td>
                </tr>
                <tr>
                    <td colspan="9"><br />
                    </td>
                </tr>


                <tr>
                    <td colspan="9">
                        <button id="btnBack" name="btnBack" onclick='return back("./main.jsp");'><%=session.getAttribute("button.back")%></button>
                    </td>
                </tr>
            </table>
        </div>

    </body>
</html>
