<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long flockId = Long.parseLong(request.getParameter("flockId"));
    String growDay = request.getParameter("growDay");
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
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="resources/style/calendar.css"/>

    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/multiopt.css"/>
</head>
<body>
<form action="./rmctrl-flockhistory24-table.jsp">
    <input type="hidden" name="flockId" value="<%=flockId%>">
    <table cellpadding="1" cellspacing="1" border="0" width="100%">
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <h1>Flock History 24 hour Table</h1>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <br/>
                    <jsp:include page="view-tablehistory24.html"/>
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