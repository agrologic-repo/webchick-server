<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Screen screen = (Screen) request.getAttribute("screen");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Edit Screen</title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" language="javascript">
        function reset() {
            document.getElementById("msgScreenTitle").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.editForm.Ntitle.value == "") {
                document.editForm.Ntitle.focus();
                return false;
            } else if (document.editForm.Nposition.value == "") {
                document.editForm.Nposition.focus();
                return false;
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
            <td valign="top" height="648px">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Edit Screen</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>edit screen - <%=screen.getId() %>
                            </h2></p>
                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form action="./editscreen.html" method="post" id="editForm" name="editForm">
                                <input id="programid" type="hidden" name="programid"
                                       value="<%=screen.getProgramId() %>">
                                <input id="screenid" type="hidden" name="screenid" value="<%=screen.getId() %>">
                                <table cellSpacing=1 cellPadding=1 width="auto" align="left" border="0">
                                    <tr>
                                        <td class="rightCell">Table title *</td>
                                        <td><input id="Ntitle" type="text" name="Ntitle" value="<%=screen.getTitle() %>"
                                                   style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell">Row position *</td>
                                        <td><input id="Nposition" type="text" name="Nposition"
                                                   value="<%=screen.getPosition() %>" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="rightCell" valign="top">Description &nbsp;</td>
                                        <td><textarea name="Ndescript" cols="60" rows="6"><%=screen.getDescript() %>
                                        </textarea></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button id="btnBack" name="btnBack"
                                                    onclick='return back("./all-screens.html?programId=<%=screen.getProgramId() %>");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <button id="btnUpdate" name="btnUpdate" type="submit"
                                                    onclick='return validate();'>
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