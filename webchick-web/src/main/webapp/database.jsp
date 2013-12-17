<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("database.page.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td>
                <h1><%=session.getAttribute("database.page.header")%>
                </h1>

                <h2><%=session.getAttribute("database.page.sub.header")%>
                </h2>
            </td>
            <td align="center">
                <%@ include file="messages.jsp" %>
            </td>
        </tr>
        <tr>
            <td colspan="9">
                <a href="./dumpdatabase" onclick="window.location.href.replace('./dumpdatabase')">
                    <img src="resources/images/database.png" style="cursor: pointer" hspace="5"
                         border="0"/><%=session.getAttribute("button.database")%>
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="9"><br/>
            </td>
        </tr>
    </table>
</div>
</body>
</html>