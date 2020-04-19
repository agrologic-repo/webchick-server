<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Cellink" %>
<%@ page import="com.agrologic.app.model.User" %>


<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    User editUser = (User) request.getAttribute("edituser");
    Cellink cellink = (Cellink) request.getAttribute("editcellink");
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Edit Cellink</title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function reset() {
            document.getElementById("msgCellinkName").innerHTML = "";
            document.getElementById("msgPassword").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ncellinkname.value == "") {
                document.getElementById("msgCellinkName").innerHTML = "&nbsp;Cellink name can't be empty";
                document.getElementById("msgCellinkName").style.color = "RED";
                document.addForm.Ncellinkname.focus();
                valid = false;
            }

            if (document.addForm.Npassword.value == "") {
                document.getElementById("msgPassword").innerHTML = "&nbsp;Password can't be empty";
                document.getElementById("msgPassword").style.color = "RED";
                document.addForm.Npassword.focus();
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
    <form id="editForm" name="editForm" action="./editcellink.html?userId=<%=editUser.getId()%>" method="post">
        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td width="483">
                    <h1>Edit Cellink</h1>
                </td>
            </tr>
            <tr>
                <td>
                    <h2>edit cellink - <%=cellink.getId()%>
                    </h2>

                    <div><p style="color:red">Boxes with an asterisk next to them are required</p></div>
                    <input id="Nuserid" type="hidden" name="Nuserid" value="<%=cellink.getUserId()%>">
                    <input id="Ncellinkid" type="hidden" name="Ncellinkid" value="<%=cellink.getId()%>">
                    <table>
                        <tr>
                            <td class="rightCell">Cellink name *</td>
                            <td><input id="Ncellinkname" type="text" name="Ncellinkname" value="<%=cellink.getName()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="rightCell">Password *</td>
                            <td><input id="Npassword" type="text" name="Npassword" value="<%=cellink.getPassword()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="rightCell">SIM Number&nbsp;&nbsp;</td>
                            <td><input id="Nsim" type="text" name="Nsim" value="<%=cellink.getSimNumber()%>"
                                       style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="">Type</td>
                            <td>
                                <select id="Ntype" name="Ntype" style="width:120px">
                                    <option value="N/A">
                                    <% for (String type : Cellink.getTypeList()) {%>
                                        <%if(cellink.getType().equals(type)){ %>
                                            <option value="<%=type%>" selected><%=type%>
                                        <%} else {%>
                                            <option value="<%=type%>"><%=type%>
                                        <%}%>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="">Version</td>
                            <td><input id="Nversion" type="text" name="Nversion" style="width:100px"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <button id="btnBack" name="btnBack"
                            onclick='return back("./userinfo.html?userId=<%=editUser.getId()%>");'>
                        <%=session.getAttribute("button.cancel")%>
                    </button>
                    <button id="btnAdd" type="submit" name="btnAdd" onclick="return validate();">
                        <%=session.getAttribute("button.ok")%>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>