<% String filename = request.getParameter("filename"); %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Download Excel</title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
</head>
<body>
<table style="border:1px solid #ff0000;background-color:#f7f7f7" align="center">
    <tr style="font-weight:bold;">
        <td align="center" align="center" colspan=2 style="border-bottom: 2px solid #000000;">Download Excel File</td>
    </tr>
    <tr>
        <td align="center">
            <a href="rmctrl-download-file.jsp?filename=<%=filename%>" onclick="javascript:history.back()">Click To
                Download File</a>
        </td>
    </tr>
    <tr>
        <td>
            <hr>
    </tr>
    <tr>
        <td align="center">
            <p>
                <button id="btnClose" name="btnClose" onclick='self.close()'><%=session.getAttribute("button.close")%>
                </button>
            </p>
        </td>
    </tr>
</table>
</body>
</html>