<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Edit Profile</title>
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
            <td valign="top">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td>
                            <h1>Edit Profile</h1>

                            <h2><%=user.getFirstName()%> <%=user.getFirstName()%>
                            </h2>
                            <img border="0" src="resources/images/user1.png"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p style="color:red">Boxes with an asterisk next to them are required</p>

                            <form id="editForm" name="editForm" action="./editprofile.html" method="post">
                                <table borderColor=#0000ff cellSpacing=1 cellPadding=1 align=left bgColor=#ffffff
                                       border=0>
                                    <input type="hidden" class=leftTitles name="Nuserid" value="<%=user.getId()%>">
                                    <tr>
                                        <td class="">Login *</td>
                                        <td><input id="Nusername" type="text" name="Nusername" readonly
                                                   value="<%=user.getLogin()%>" onchange="validateLoginName();"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Password *</td>
                                        <td><input id="Npassword" type="password" name="Npassword"
                                                   value="<%=user.getPassword()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">First name *</td>
                                        <td><input id="Nfname" type="text" name="Nfname"
                                                   value="<%=user.getFirstName()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Last name *</td>
                                        <td><input id="Nlname" type="text" name="Nlname"
                                                   value="<%=user.getLastName()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Phone</td>
                                        <td><input id="Nphone" type="text" name="Nphone" value="<%=user.getPhone()%>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="">Email</td>
                                        <td><input id="Nemail" type="text" name="Nemail" value="<%=user.getEmail()%>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="">Role *</td>
                                        <td>
                                            <input type="hidden" name="Nrole" value="<%=user.getRole()%>">
                                            <select id="Nrolel" name="Nrolel" class="dropDownList" disabled>
                                                <option value="0"></option>
                                                <option value="1"><%=session.getAttribute("user.role.admin")%>
                                                </option>
                                                <option value="2"><%=session.getAttribute("user.role.regular")%>
                                                </option>
                                                <option value="3"><%=session.getAttribute("user.role.advanced")%>
                                                </option>
                                            </select>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td class="">Company</td>
                                        <td>
                                            <input type="text" name="Ncompany" value="<%=user.getCompany()%>" readonly/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnCancel" name="btnCancel"
                                    onclick='return back("./my-profile.jsp?userId=<%=user.getId()%>");'>
                                <%=session.getAttribute("button.cancel")%>
                            </button>
                            <button id="btnUpdate" type="submit" name="btnUpdate" onclick='return validate();'>
                                <%=session.getAttribute("button.ok")%>
                            </button>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

<script language="Javascript">
    var length = document.editForm.Nrolel.options.length;
    var role =
    <%= user.getRole().getValue() %>
    for (var i = 0; i < length; i++) {
        if (document.editForm.Nrolel.options[i].value == role) {
            document.editForm.Nrolel.selectedIndex = i;
            break;
        }
    }
</script>
</body>

</html>