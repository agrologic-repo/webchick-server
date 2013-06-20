<%--
    Document   : rmctrl-flockminmaxhum-graphs.jsp
    Created on : Jan 16, 2011, 10:23:18 AM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. ®
    Version    : 0.1.1.1
--%>

<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.UserDto" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

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

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Min Max Humidity Graph</title>
    <script type="text/javascript">
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
    </script>
</head>
<body>
<form action="./rmctrl-flockfeedwater-graphs.jsp">
    <input type="hidden" name="userId" value="<%=userId%>">
    <input type="hidden" name="flockId" value="<%=flockId%>">
    <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
    <table>
        <tr>
            <td>
                <img border="0"
                     src="./minmaxhumgraph.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&fromDay=<%=fromDay%>&toDay=<%=toDay%>"/>
            </td>
            <td valign="top">
                <table border="0">
                    <tr>
                        <td colspan="2">
                            <h4>Graph Parameters</h4>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            From :
                        </td>
                        <td>
                            <input type="text" size="5" name="fromDay">
                            &nbsp;grow day
                        </td>
                    </tr>
                    <tr>
                        <td>
                            To :
                        </td>
                        <td>
                            <input type="text" size="5" name="toDay">
                            &nbsp;grow day
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="Submit">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="left">
                <p>
                    <button id="btnBack" name="btnBack"
                            onclick='return back("./historyview.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId %>")'>
                        &nbsp;Back
                    </button>
                </p>
            </td>
        </tr>

    </table>
</form>
</body>
</html>
