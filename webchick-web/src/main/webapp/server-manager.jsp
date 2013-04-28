<%--
    Document   : server-manager
    Created on : Feb 6, 2012, 9:40:32 AM
    Author     : Administrator
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>

<!DOCTYPE html>

<%  UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
        <link rel="shortcut icon" href="img/favicon1.ico" type="image/x-icon"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    </head>
    <body>
        <div id="header">
            <%@include file="usermenuontop.jsp"%>
        </div>
        <div id="main-shell">
            <fieldset style="padding: 5px">
                <legend><b><%=session.getAttribute("cellink.states")%></b></legend>
                <table>
                    <tr>
                        <td>
                            
                        </td>
                    </tr>
                    <tr>
                        <td>

                        </td>
                    </tr>
                </table>
            </fieldset>
        </div>
    </body>
</html>
