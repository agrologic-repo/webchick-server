<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<String> companies = (Collection<String>) session.getAttribute("companies");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("user.page.add.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function clearForm() {
            document.addForm.Nusername.value = "";
            document.addForm.Npassword.value = "";
            document.addForm.Nfname.value = "";
            document.addForm.Nlname.value = "";
            document.addForm.Nrole.selectedIndex = 0;
        }
    </script>
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
                var msgOutout = document.getElementById("msgUserName");
                msgOutout.innerHTML = msg;
                msgOutout.style.color = (msg == "login valid") ? "green" : "red";
            }
        }
        function reset() {
            document.getElementById("msgPassword").innerHTML = ""
            document.getElementById("msgFName").innerHTML = "";
            document.getElementById("msgLName").innerHTML = "";
            document.getElementById("msgRole").innerHTML = "";
            document.getElementById("msgPhone").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Nrole.value == "0") {
                document.getElementById("msgRole").innerHTML = "Field can't be empty";
                document.getElementById("msgRole").style.color = "RED";
                document.addForm.Nlname.focus();
                valid = false;
            }
            if (document.addForm.Nphone.value == "") {
                document.getElementById("msgPhone").innerHTML = "Field can't be empty";
                document.getElementById("msgPhone").style.color = "RED";
                document.addForm.Nphone.focus();
                valid = false;
            }
            if (document.addForm.Nlname.value == "") {
                document.getElementById("msgLName").innerHTML = "Field can't be empty";
                document.getElementById("msgLName").style.color = "RED";
                document.addForm.Nlname.focus();
                valid = false;
            }
            if (document.addForm.Nfname.value == "") {
                document.getElementById("msgFName").innerHTML = "Field can't be empty";
                document.getElementById("msgFName").style.color = "RED";
                event.returnValue = false;
                document.addForm.Nfname.focus();
                valid = false;
            }
            if (document.addForm.Npassword.value == "") {
                document.getElementById("msgPassword").innerHTML = "Field can't be empty";
                document.getElementById("msgPassword").style.color = "RED";
                document.addForm.Npassword.focus();
                valid = false;
            }
            if (document.addForm.Nusername.value == "") {
                document.getElementById("msgUserName").innerHTML = "Field can't be empty";
                document.getElementById("msgUserName").style.color = "RED";
                document.addForm.Nusername.focus();
                valid = false;
            }

            if(document.addForm.Nemail.value != "") {
                validateEmail(document.addForm.Nemail);
            }

            if (!valid) {
                return false;
            }
        }
        function showNewCompany() {
            var checked = addForm.newCompany.checked;
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
    <form id="addForm" name="addForm" action="./adduser.html" method="post">
        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td>
                    <h1><%=session.getAttribute("user.page.add.title")%>
                    </h1>
                </td>
            </tr>
            <tr>
                <td>
                    <h2><%=session.getAttribute("user.page.add.title")%>
                    </h2>

                    <div><p style="color:red;">Boxes with an asterisk next to them are required</p></div>
                    <table>
                        <tr>
                            <td><%=session.getAttribute("user.page.login")%> *</td>
                            <td><input id="Nusername" type="text" name="Nusername" onchange="validateLoginName();" onblur="validateLoginName();"/>
                            </td>
                            <td id="msgUserName"></td>
                        </tr>
                        <tr>
                            <td class=""><%=session.getAttribute("user.page.password")%> *</td>
                            <td><input class="textFeild" id="Npassword" type="password" name="Npassword"/>
                            </td>
                            <td id="msgPassword"></td>
                        </tr>
                        <tr>
                            <td><%=session.getAttribute("user.page.fname")%> *</td>
                            <td><input id="Nfname" type="text" name="Nfname"/></td>
                            <td id="msgFName"></td>
                        </tr>
                        <tr>
                            <td><%=session.getAttribute("user.page.lname")%> *</td>
                            <td><input id="Nlname" type="text" name="Nlname"/></td>
                            <td id="msgLName"></td>
                        </tr>
                        <tr>
                            <td class=""><%=session.getAttribute("user.page.phone")%>
                            </td>
                            <td><input id="Nphone" type="text" name="Nphone"/></td>
                            <td id="msgPhone"></td>
                        </tr>
                        <tr>
                            <td class=""><%=session.getAttribute("user.page.email")%>
                            </td>
                            <td><input id="Nemail" type="text" name="Nemail"/></td>
                            <td id="msgEmail" align="left" height="22"></td>
                        </tr>
                        <tr>
                            <td class=""><%=session.getAttribute("user.page.role")%> *</td>
                            <td>
                                <select id="Nrole" name="Nrole" class="dropDownList">
                                    <option value="0"></option>
                                    <option value="1"><%=session.getAttribute("user.role.admin")%></option>
                                    <option value="2"><%=session.getAttribute("user.role.regular")%></option>
                                    <option value="3"><%=session.getAttribute("user.role.advanced")%></option>
                                    <option value="4">ReadOnlyUser</option>
                                </select>
                            </td>
                            <td id="msgRole"></td>
                        </tr>
                        <tr>
                            <td class=""><%=session.getAttribute("user.page.company")%>
                            </td>
                            <td>
                                <div id="existingCompanyDiv" name="existingCompanyDiv" style="display:block;">
                                    <select id="Ncompanylist" name="Ncompanylist" class="dropDownList">
                                        <option value=""></option>
                                        <% for (String c : companies) {%>
                                        <option value="<%=c%>"><%=c%>
                                        </option>
                                        <%}%>
                                    </select>
                                </div>
                                <div id="newCompanyDiv" name="newCompanyDiv" style="display:none;">
                                    <input type="text" name="Ncompany"/>
                                </div>
                            </td>
                            <td><input type="checkbox" id="newCompany" name="newCompany" onclick="showNewCompany()">
                                Add Company</input>
                            </td>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="btnCancel" name="btnCancel" onclick='return back("./all-users.html");'>
                        <%=session.getAttribute("button.cancel")%>
                    </button>
                    <button id="btnAdd" type="submit" name="btnAdd" onclick="return validate();">
                        <%=session.getAttribute("button.ok") %>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
