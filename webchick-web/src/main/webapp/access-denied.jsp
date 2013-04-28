<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.Vector"%>

<jsp:directive.page import="com.agrologic.app.app.dto.UserDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=request.getSession().getAttribute("dir") %>" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Access Denied</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <link rel="shortcut icon" href="img/favicon5.ico"/>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script language="javascript" src="js/menu.js"></script>
    </head>
    <body>
            <div id="header">
            <%@include file="usermenuontop.jsp"%>
            </div>
            <div id="main-shell">
            <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td>
                    <h1><%=session.getAttribute("access.denied.page.header")%></h1>
                    <p><h2><%=session.getAttribute("access.denied.page.subtitle")%></h2></p>
                    <p>
                    <p><%=session.getAttribute("access.denied.page.description")%><br /><br />
                    <br />
                    <br />
                </td>
            </tr>
        </table>
        </div>
    </body>
</html>