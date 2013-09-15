<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String) request.getSession().getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("database.page.title")%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script language="javascript" src="js/general.js"></script>
    <link rel="shortcut icon" href="img/favicon5.ico"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
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
                    <img src="img/database.png" style="cursor: pointer" hspace="5"
                         border="0"/><%=session.getAttribute("button.database")%>
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="9"><br/>
            </td>
        </tr>


        <tr>
            <td colspan="9">
                <button id="btnBack" name="btnBack"
                        onclick='return back("./main.jsp");'><%=session.getAttribute("button.back")%>
                </button>
            </td>
        </tr>
    </table>
</div>

</body>
</html>