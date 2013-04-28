<%
String message = (String) request.getSession().getAttribute("message");
request.getSession().setAttribute("message", null);

Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
request.getSession().setAttribute("error", null);
%>

<%if (message != null) {
        if (!errorFlag) {%>
<table class="infoMsg" align="center">
    <tr>
        <td><img src="img/success.png"/>&nbsp;&nbsp;&nbsp;
            <b><%=message%></b>
        </td>
    </tr>
</table>
<%} else {%>
<table class="errMsg" align="center">
    <tr>
        <td><img src="img/unsuccess.gif"/>&nbsp;&nbsp;&nbsp;
            <b><%=message%></b>
        </td>
    </tr>
</table>
<%}%>
<%} else {%>
<table align="center" width="250px">
    <tr>
        <td>&nbsp;</td>
    </tr>
</table>
<%}%>