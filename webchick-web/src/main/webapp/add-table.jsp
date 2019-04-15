<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%  User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Screen screen = (Screen) session.getAttribute("screen");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add Table</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function reset() {
            document.getElementById("msgTableName").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ntablename.value == "") {
                document.getElementById("msgTableName").innerHTML = "&nbsp;Table title can't be empty";
                document.getElementById("msgTableName").style.color = "RED";
                document.addForm.Ntablename.focus();
                valid = false;
            }
            if (!valid) {
                return false;
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
            <td width="483">
                <p>

                <h1>Add Table</h1></p>
            </td>
        </tr>
        <tr>
            <td>
                <p>

                <h2>add table to screen <%=screen.getTitle()%>
                </h2></p>
                <p style="color:red;">

                <div>Boxes with an asterisk next to them are required</div>
                </p>
                <form id="addForm" name="addForm" action="./addtable.html" method="post" onsubmit="return validate();">
                    <input type="hidden" id="programId" name="programId" value="<%=screen.getProgramId()%>">
                    <input type="hidden" id="screenId" name="screenId" value="<%=screen.getId()%>">
                    <table width="auto" align="left">
                        <tr>
                            <td align="left">Table name *</td>
                            <td><input id="Ntablename" type="text" name="Ntablename" style="width:150px" maxlength="25">
                            </td>
                            <td id="msgTableName"></td>
                        </tr>
                        <tr>
                            <td align="left">Position</td>
                            <td><input id="Nposition" type="text" name="Nposition" style="width:40px"
                                       value="<%=screen.getTables().size() + 1%>"></td>
                        </tr>
                        <tr>
                            <td>
                                <button id="btnBack" name="btnBack"
                                        onclick='return back("./all-tables.html?programId=<%=screen.getProgramId()%>&screenId=<%=screen.getId()%>");'>
                                    <%=session.getAttribute("button.cancel")%>
                                </button>
                                <button id="btnAdd" name="btnAdd" type="submit">
                                    <%=session.getAttribute("button.ok")%>
                                </button>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>

</body>
</html>