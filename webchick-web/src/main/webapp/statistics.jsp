<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>

    <title>Statistics</title>
<head>
    <title><%=session.getAttribute("database.page.title")%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>

</head>
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
                <img border="0" src="TotalCellinkStatePieChart" width="100" height="100"></img>
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
