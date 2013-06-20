<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=request.getSession().getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("home.page.title")%>
    </title>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <script type="text/javascript" src="js/fhelp.js"></script>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%" style="line-height: 25px;">
        <tr>
            <td>
                <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                <h1><%=session.getAttribute("home.page.header")%> - <%=session.getAttribute("user.role.admin")%>
                </h1>
                <%} else {%>
                <h1><%=session.getAttribute("home.page.header")%> - <%=session.getAttribute("user.role.regular")%>
                </h1>
                <%}%>
                <p>

                <h2><%=session.getAttribute("label.database")%>
                </h2></p>
                <ul>
                    <li><a href="overview.html?userId=<%=user.getId()%>"><%=session.getAttribute("menu.overview")%>
                    </a> - <%=session.getAttribute("label.overview.descript")%>
                    </li>
                    <li><a href="all-users.html?userId=<%=user.getId()%>"><%=session.getAttribute("menu.users")%>
                    </a> - <%=session.getAttribute("label.manager.descript")%>
                    </li>
                    <li><a href="all-programs.html"><%=session.getAttribute("menu.screens")%>
                    </a>
                        - <%=session.getAttribute("label.maintenance.descript")%>
                    </li>
                    <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                    <li><a href="view-result.html"><%=session.getAttribute("label.preview")%>
                    </a>
                        - <%=session.getAttribute("label.preview.descript")%>
                    </li>
                    <%}%>
                </ul>
                <p>

                <h2><%=session.getAttribute("label.user")%>
                </h2></p>
                <ul>
                    <li>
                        <a href="change-password.jsp?userId=<%=user.getId()%>"><%=session.getAttribute("label.change.password")%>
                        </a> - <%=session.getAttribute("label.change.password.descript")%>
                    </li>
                    <li><a href="logout.html"><%=session.getAttribute("label.logout")%>
                    </a> - <%=session.getAttribute("label.logout.descript")%>
                </ul>
                <p>

                <h2><%=session.getAttribute("label.help")%>
                </h2></p>
                <ul class="niceList">
                    <li><a target="_blank" href="./help.html"><%=session.getAttribute("label.help")%>
                    </a> - <%=session.getAttribute("label.help.descript")%>
                    </li>
                </ul>
                <p><%=session.getAttribute("label.version")%>
                </p>
            </td>
        </tr>
    </table>
</div>

</body>
</html>
