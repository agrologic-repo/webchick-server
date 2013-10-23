<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.User" %>

<%  User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir") %>">
<head>
    <title>Access Denied</title>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td>
                <h1><%=session.getAttribute("access.denied.page.header")%></h1>
                <p><h2><%=session.getAttribute("access.denied.page.subtitle")%></h2></p>
                <p><p><%=session.getAttribute("access.denied.page.description")%><br/><br/><br/><br/>
            </td>
        </tr>
    </table>
</div>
</body>
</html>