<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Integer growDay = 1;
    try {
        growDay = Integer.parseInt(request.getParameter("growDay"));
        if (growDay == null) {
            growDay = 1;
        }
    } catch (Exception ex) {
    }
%>

<html dir="<%=session.getAttribute("dir")%>">
<head>

    <title>In\Out Temperature and Humidity Graph 24 Hour</title>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/multiopt.css"/>
    <script type="text/javascript" src="resources/custom/javascript/general.js">;</script>
</head>
<body>
<form action="./rmctrl-flock24graph-wfc.jsp">
    <input type="hidden" name="userId" value="<%=userId%>">
    <input type="hidden" name="flockId" value="<%=flockId%>">
    <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
    <table>
        <tr>
            <td>
                <img border="0"
                     src="./Graph24HourFWServlet?userId=<%=userId%>&cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&growDay=<%=growDay%>"/>
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