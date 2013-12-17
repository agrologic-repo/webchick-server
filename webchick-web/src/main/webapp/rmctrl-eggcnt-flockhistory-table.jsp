<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
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
<form action="./rmctrl-eggcnt-flockhistory-table.jsp">
    <input type="hidden" name="flockId" value="<%=flockId%>">
    <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" name="userId" value="<%=userId%>">
    <table cellpadding="1" cellspacing="1" border="0" width="100%">
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <h1>Flock History Table</h1>
                </fieldset>
            </td>
        </tr>
        <tr>
            <td align="center">
                <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px; width: 85%">
                    <h2></h2>
                    <table border="0">
                        <tr>
                            <td>
                                <%if (fromDay == -1 || toDay == -1) {%>
                                From : <input type="text" size="5" name="fromDay"/>
                                To : <input type="text" size="5" name="toDay"/>
                                <%} else {%>
                                From : <input type="text" size="5" name="fromDay" value="<%=fromDay%>"/>
                                To : <input type="text" size="5" name="toDay" value="<%=toDay %>"/>
                                <%}%>
                                <input type="submit" value="Submit">
                            </td>
                        </tr>
                    </table>
                    <br/>
                    <jsp:include page="view-table-eggcounthistory.html"/>
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