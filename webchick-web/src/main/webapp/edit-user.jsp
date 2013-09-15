<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.Collection" %>


<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message = (String) request.getSession().getAttribute("message");
    request.getSession().setAttribute("message", null);

    Boolean errorFlag = (Boolean) request.getSession().getAttribute("error");
    request.getSession().setAttribute("error", null);

    Collection<User> users = (Collection<User>) request.getSession().getAttribute("users");
    Long userId = Long.parseLong(request.getParameter("userId"));
    User editUser = findUserToEdit(users, userId);
    Collection<String> companies = (Collection<String>) request.getSession().getAttribute("companies");
%>

<%! User findUserToEdit(Collection<User> users, Long userId) {
    for (User u : users) {
        if (u.getId().equals(userId)) {
            return u;
        }
    }
    return null;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Edit User</title>
    <link rel="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
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
            } else if (document.editForm.Nrolel.selectedIndex == 0) {
                document.editForm.Nrolel.selectedIndex
                alert('Select user role');
                document.editForm.Nrolel.focus();
                return false;
            } else if (validateEmail(document.editForm.Nemail) == false) {
                document.editForm.Nemail.focus();
                return false;
            } else {
                var i = document.editForm.Nrolel.selectedIndex;
                document.editForm.Nrole.value = document.editForm.Nrolel.options[i].value;
                document.editForm.submit();
            }
        }
        function clearForm() {
            document.addForm.Nusername.value = "";
            document.addForm.Npassword.value = "";
            document.addForm.Nfname.value = "";
            document.addForm.Nlname.value = "";
            document.addForm.Nrolel.selectedIndex = 0;
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
        function back(link) {
            window.history.back();
            return false;
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
                            <h1>Edit user</h1>

                            <h2><%=editUser.getFirstName()%> <%=editUser.getFirstName()%>
                            </h2>
                            <img border="0" src="img/user1.png"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p style="color:red">Boxes with an asterisk next to them are required</p>

                            <form id="editForm" name="editForm" action="./edituser.html" method="post">
                                <table borderColor=#0000ff cellSpacing=1 cellPadding=1 align=left bgColor=#ffffff
                                       border=0>
                                    <input type="hidden" class=leftTitles name="Nuserid" value="<%=editUser.getId()%>">
                                    <tr>
                                        <td class="">Login *</td>
                                        <td><input id="Nusername" type="text" name="Nusername" readonly
                                                   value="<%=editUser.getLogin()%>" onchange="validateLoginName();">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="">Password *</td>
                                        <td><input id="Npassword" type="password" name="Npassword"
                                                   value="<%=editUser.getPassword()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">First name *</td>
                                        <td><input id="Nfname" type="text" name="Nfname"
                                                   value="<%=editUser.getFirstName()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Last name *</td>
                                        <td><input id="Nlname" type="text" name="Nlname"
                                                   value="<%=editUser.getLastName()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Phone</td>
                                        <td><input id="Nphone" type="text" name="Nphone"
                                                   value="<%=editUser.getPhone()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Email</td>
                                        <td><input id="Nemail" type="text" name="Nemail"
                                                   value="<%=editUser.getEmail()%>"></td>
                                    </tr>
                                    <tr>
                                        <td class="">Role *</td>
                                        <td>
                                            <input type="hidden" name="Nrole" value="<%=editUser.getRole()%>">
                                            <select id="Nrolel" name="Nrolel" class="dropDownList">
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
                                            <div id="existingCompanyDiv" name="existingCompanyDiv"
                                                 style="display:block;">
                                                <select id="Ncompanylist" name="Ncompanylist" class="dropDownList"
                                                        style="width:130px">
                                                    <option value=""></option>
                                                    <% for (String c : companies) {%>
                                                    <option value="<%=c%>"><%=c%>
                                                    </option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div id="newCompanyDiv" name="newCompanyDiv" style="display:none;">
                                                <input type="text" name="Ncompany" value=""
                                                       onfocus="this.style.background='orange'"
                                                       onblur="this.style.background='white'"/>
                                            </div>
                                        </td>
                                        <td><input type="checkbox" id="newCompany" name="newCompany"
                                                   onclick="showNewCompany()">Add Company</input></td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnCancel" name="btnCancel" onclick='javascript:window.history.back();'>
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
    <%= editUser.getRole().getValue() %>
    for (var i = 0; i < length; i++) {
        if (document.editForm.Nrolel.options[i].value == role) {
            document.editForm.Nrolel.selectedIndex = i;
            break;
        }
    }

    var length = document.editForm.Ncompanylist.options.length;
    var company = '<%= editUser.getCompany()%>'
    for (var i = 0; i < length; i++) {
        if (document.editForm.Ncompanylist.options[i].value == company) {
            document.editForm.Ncompanylist.selectedIndex = i;
            break;
        }
    }
</script>
</body>

</html>