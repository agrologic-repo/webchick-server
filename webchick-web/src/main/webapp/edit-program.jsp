<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.User" %>

<%  User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Program program = (Program) request.getAttribute("program");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Edit Program</title>


    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" language="javascript">
        function validate() {
            if (document.editForm.Nprogramname.value == "") {
                alert('Enter name for the program ');
                document.editForm.Nprogramname.focus();
                return false;
            } else {
                document.editForm.submit();
            }
        }

    </script>
</head>
<body>
<div id="header">
    <%@include file="usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td valign="top" height="648px">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Edit Program</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>edit program - <%=program.getId() %>
                            </h2>

                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form action="./editprogram.html" method="post" id="editForm" name="editForm">
                                <input id="programid" type="hidden" name="programid" value="<%=program.getId() %>">
                                <table cellSpacing=1 cellPadding=1 width="250px" align="left" border="0">
                                    <tr>
                                        <td class="rightCell">Program name *</td>
                                        <td><input id="Nprogramname" type="text" name="Nprogramname"
                                                   value="<%=program.getName() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <button id="btnBack" name="btnBack"
                                                    onclick='return back("./all-programs.html");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <button id="btnSave" type="submit" name="btnSave"
                                                    onclick="return validate();">
                                                <%=session.getAttribute("button.ok") %>
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

</body>
</html>
