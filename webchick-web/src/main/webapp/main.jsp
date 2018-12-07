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
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("home.page.title")%></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>

</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%" style="line-height: 25px;">
        <tr>
            <td>
                <%if (user.getRole() == UserRole.ADMIN) {%>
                    <h1><%=session.getAttribute("home.page.header")%> - <%=session.getAttribute("user.role.admin")%></h1>
                <%} else {%>
                    <h1><%=session.getAttribute("home.page.header")%> - <%=session.getAttribute("user.role.regular")%></h1>
                <%}%>
                <p><h2><%=session.getAttribute("label.database")%></h2></p>
                <ul>
                    <li><a href="overview.html?userId=<%=user.getId()%>"><%=session.getAttribute("menu.overview")%></a>
                        - <%=session.getAttribute("label.overview.descript")%>
                    </li>
                    <li><a href="all-users.html?userId=<%=user.getId()%>"><%=session.getAttribute("menu.users")%></a>
                        - <%=session.getAttribute("label.manager.descript")%>
                    </li>
                    <li><a href="all-programs.html"><%=session.getAttribute("menu.screens")%></a>
                        - <%=session.getAttribute("label.maintenance.descript")%>
                    </li>
                    <%if (user.getRole() == UserRole.ADMIN) {%>
                    <li><a href="view-result.html"><%=session.getAttribute("label.preview")%></a>
                        - <%=session.getAttribute("label.preview.descript")%>
                    </li>
                    <%}%>
                </ul>
                <p><h2><%=session.getAttribute("label.user")%></h2></p>
                <ul>
                    <li>
                        <a href="change-password.jsp?userId=<%=user.getId()%>">
                            <%=session.getAttribute("label.change.password")%> </a>
                        - <%=session.getAttribute("label.change.password.descript")%>
                    </li>
                    <li><a href="logout.html"><%=session.getAttribute("label.logout")%></a>
                        - <%=session.getAttribute("label.logout.descript")%>
                </ul>
                <p><h2><%=session.getAttribute("label.help")%></h2></p>
                <ul class="niceList">
                    <li><a target="_blank" href="./help/index.html"><%=session.getAttribute("label.help")%></a>
                        - <%=session.getAttribute("label.help.descript")%>
                    </li>
                </ul>
                <%--<p><%=session.getAttribute("label.version")%></p>--%>
                <p>version 6.7.32</p>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
