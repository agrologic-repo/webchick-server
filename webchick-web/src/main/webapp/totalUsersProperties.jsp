<%@ include file="disableCaching.jsp" %>
<%@ page contentType="text/html" pageEncoding="utf-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.Vector"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/strict.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <title>Total Users Properties</title>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
        ConnectorDao
        <script type="text/javascript" language="javascript">
        function refresh()
        {
            window.document.location.reload();
            return false;
        }
        </script>
    </head>
    <body>
        <table class ="main" align="center" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="left" valign="top">
                    <%@include file="topUserMenu.jsp"%>
                    <%@include file="topLogin.jsp"%>
                </td>
            </tr>
            <tr>
                <td>
                <div class="mainContent">
                    <div class="subMainContent">
                    </ br>
                    </ br>
                        <h1>Users Properties</h1>
                        <p><h2>users properties</h2>
                            <br />
                            <br />
                            <br />
                            <TABLE style="height: 369px; width: 760px" cellSpacing=1 cellPadding=1 align=left border=0>
                                <TR>
                                    <TD width="165" colspan="3">
                                        <img border="0" src="TotalUserPropertiesPieChart" width="599" height="379">
                                    </TD>
                                </TR>
                                <TR>
                                <TD width="550">
                                    <button class="button" id="close" name="close" onclick='return back();'><img src="img/back1.gif" > Back </button>&nbsp;&nbsp;&nbsp;
                                    <button class="button" id="refresh" name="refresh" onclick='return refresh();'><img src="img/refresh.gif" > Refresh </button>&nbsp;&nbsp;&nbsp;
                                </TD>
                                </TR>

                            </TABLE>
                     </div>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>

