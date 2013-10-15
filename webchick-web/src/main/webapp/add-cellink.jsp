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
    Long userId = Long.parseLong(request.getParameter("userId"));
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add Cellink</title>

    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
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
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td valign="top" height="648px">
                <form id="addForm" name="addForm" action="./addcellink.html?userId=<%=userId%>" method="post">
                    <table border=0 cellPadding=1 cellSpacing=1>
                        <tr>
                            <td>
                                <h1>Add Cellink</h1>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2>add cellink </h2>

                                <div>
                                    <p style="color:red;">Boxes with an asterisk next to them are required</p>
                                </div>
                                <input id="Nuserid" readonly type="hidden" name="Nuserid" class=rightTitles
                                       value="<%=userId%>">
                                <table>
                                    <tr>
                                        <td class="">Cellink name *</td>
                                        <td><input id="Ncellinkname" type="text" name="Ncellinkname"
                                                   style="width:100px"></td>
                                        <td id="msgCellinkName"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Password *</td>
                                        <td><input id="Npassword" type="text" name="Npassword" style="width:100px"></td>
                                        <td id="msgPassword"></td>
                                    </tr>
                                    <tr>
                                        <td class="">SIM Number</td>
                                        <td><input id="Nsim" type="text" name="Nsim" style="width:100px"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Type</td>
                                        <td>
                                            <select id="Ntype" name="Ntype" style="width:120px">
                                                <option value="" selected></option>
                                                <% for (String type : Cellink.getTypeList()) {%>
                                                <option value="<%=type %>"><%=type %>
                                                </option>
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
                            <td>
                                <button id="btnBack" name="btnBack"
                                        onclick='return back("./userinfo.html?userId=<%=userId %>");'>
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                                <button id="btnAdd" type="submit" name="btnAdd" onclick="return validate();">
                                    <%=session.getAttribute("button.ok") %>
                                </button>
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>
</body>
</html>