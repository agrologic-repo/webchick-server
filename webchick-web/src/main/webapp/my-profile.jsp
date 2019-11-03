<%@ page import="com.agrologic.app.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><%=session.getAttribute("user.profile") %></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function validateEmail(email) {
            var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
            var address = email.value;
            if (address != "" && reg.test(address) == false) {
                alert('Invalid Email Address');
                return false;
            }
            return true;
        }
        function validate() {
            if (document.editForm.Nusername.value == "") {
                alert('Enter user name');
                document.editForm.Nusername.focus();
                return false;
            } else if (document.editForm.Npassword.value == "") {
                alert('Enter password');
                document.editForm.Npassword.focus();
                return false;
            } else if (document.editForm.Nfname.value == "") {
                alert('Enter first name');
                document.editForm.Nfname.focus();
                return false;
            } else if (document.editForm.Nlname.value == "") {
                alert('Enter last name');
                document.editForm.Nlname.focus();
                return false;
            } else if (document.editForm.Nrole.value == "0") {
                alert('Select group level');
                document.editForm.Nrole.focus();
                return false;
            } else if (validateEmail(document.editForm.Nemail) == false) {
                document.editForm.Nemail.focus();
                return false;
            } else {
                document.editForm.submit();
            }
        }
        function clearForm() {
            document.addForm.Nusername.value = "";
            document.addForm.Npassword.value = "";
            document.addForm.Nfname.value = "";
            document.addForm.Nlname.value = "";
            document.addForm.Nrole.selectedIndex = 3;
        }
        var req;
        function validateLoginName() {
            var loginName = document.getElementById("Nusername");
            var url = "logincheckservlet?loginName=" + encodeURIComponent(loginName.value);
            if (window.XMLHttpRequest)
                req = new XMLHttpRequest();
            else if (window.ActiveXObject)
                req = new ActiveXObject("Microsoft.XMLHTTP");
            req.open("GET", url, true);
            req.onreadystatechange = callback;
            req.send(null);
        }
        function callback() {
            if (req.readyState == 4 && req.status == 200) {
                var msgNode = req.responseXML.getElementsByTagName("message")[0];
                var msg = msgNode.childNodes[0].nodeValue;
                var msgOutout = document.getElementById("msgid");
                msgOutout.innerHTML = msg;
                msgOutout.style.color = (msg == "login valid") ? "green" : "red";
            }
        }

        function showNewCompany() {
            var checked = editForm.newCompany.checked;
            if (checked == true) {
                document.getElementById('existingCompanyDiv').style.display = "none";
                document.getElementById('newCompanyDiv').style.display = "inline";
            } else {
                document.getElementById('existingCompanyDiv').style.display = "inline";
                document.getElementById('newCompanyDiv').style.display = "none";
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
            <td valign="top" width="100%">
                <h1><%=session.getAttribute("user.profile") %>
                </h1>
                <table border=0 cellPadding=1 cellSpacing=1 width="100%">
                    <tr>
                        <td>
                            <h2><%=user.getFirstName()%> <%=user.getFirstName()%>
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <jsp:include page="messages.jsp"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <img border="0" src="resources/images/user1.png"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnEdit" name="btnEdit"
                                    onclick="redirect('./edit-profile.jsp?userId=<%=user.getId() %>')"><%=session.getAttribute("button.edit") %>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table class="table-list" border=1 width="100%" cellpadding="2" cellspacing="-1">
                                <tr>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.id") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getId() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.login") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getLogin() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.pwd") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getPassword() %>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.fname") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getFirstName() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.lname") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getLastName() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.phone") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getPhone() %>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.email") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getEmail() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.company") %>
                                    </td>
                                    <td bgcolor="#F3F3F3"><%=user.getCompany() %>
                                    </td>
                                    <td style="font-weight:bold"><%=session.getAttribute("table.col.user.role") %>
                                    </td>
                                    <td bgcolor="#F3F3F3">
                                        <%if (user.getRole() == UserRole.ADMIN) {%>
                                        <%=session.getAttribute("user.role.admin") %>
                                        <%} else if (user.getRole() == UserRole.DISTRIBUTOR) {%>
                                        <%=session.getAttribute("user.role.advanced") %>
                                        <%} else if (user.getRole() == UserRole.USER) {%>
                                        <%=session.getAttribute("user.role.regular") %>
                                        <%} else {%>

                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            <%if (user.getRole() == UserRole.USER) {%>
                            <button id="btnCancel" name="btnCancel"
                                    onclick='return back("./my-farms.html?userId=<%=user.getId() %>");'>
                                <%=session.getAttribute("button.back") %>
                            </button>
                            <%} else {%>
                            <button id="btnCancel" name="btnCancel" onclick='return back("./overview.html");'>
                                <%=session.getAttribute("button.back") %>
                            </button>
                            <%}%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
