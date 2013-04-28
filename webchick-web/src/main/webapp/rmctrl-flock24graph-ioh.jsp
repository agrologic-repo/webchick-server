<%--
    Document   : rmctrl-test-graphs.jsp.jsp
    Created on : Mar 2, 2011, 3:50:40 PM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. ®
    Version    : 0.1.1.1
--%>

<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>

<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.FlockDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>

<%
    Long userId  = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Integer growDay = 1;
    try {
        growDay = Integer.parseInt(request.getParameter("growDay"));
        if(growDay == null) {
            growDay = 1;
        }
    } catch (Exception ex) {
    }
%>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>In\Out Temperature and Humidity Graph 24 Hour</title>
    <script type="text/javascript">
    function back(link) {
        window.document.location.replace(link);
        return false;
    }
    </script>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/multiopt.css"/>
    </head>
    <body>
        <form action="./rmctrl-flock24graph-ioh.jsp">
        <input type="hidden" name="userId" value="<%=userId%>">
        <input type="hidden" name="flockId" value="<%=flockId%>">
        <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
        <table>
            <tr>
                <td>
                    <img border="0" src="./Graph24HourIOHServlet?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>"/>
                </td>
                <td valign="top">
                    <table border="0">
                        <tr>
                            <td colspan="2">
                                <h4>Input Grow Day </h4>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Grow Day :
                            </td>
                            <td>
                                <input type="text" size="5" name="growDay">
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
                <p><button id="btnBack" name="btnBack" onclick='return back("./historyview.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId %>")'>&nbsp;Back</button></p>
                </td>
            </tr>
        </table>
        </form>
    </body>
</html>