<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    List<String> companies = (List<String>) request.getSession().getAttribute("companies");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Add User</title>
    <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm."/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script type="text/javascript">
        function clearForm() {
            document.addForm.Nusername.value = "";
            document.addForm.Npassword.value = "";
            document.addForm.Nfname.value = "";
            document.addForm.Nlname.value = "";
            document.addForm.Nrole.selectedIndex = 0;
        }
        function back(link) {
            window.document.location.replace(link);
            return false;
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
            document.getElementById("msgUserName").innerHTML = "";
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
                document.getElementById("msgRole").innerHTML = "&nbsp;Choose role ";
                document.getElementById("msgRole").style.color = "RED";
                document.addForm.Nlname.focus();
                valid = false;
            }
            if (document.addForm.Nphone.value == "") {
                document.getElementById("msgPhone").innerHTML = "&nbsp;Phone can't be empty, type none ";
                document.getElementById("msgPhone").style.color = "RED";
                document.addForm.Nphone.focus();
                valid = false;
            }
            if (document.addForm.Nlname.value == "") {
                document.getElementById("msgLName").innerHTML = "&nbsp;Last name can't be empty";
                document.getElementById("msgLName").style.color = "RED";
                document.addForm.Nlname.focus();
                valid = false;
            }
            if (document.addForm.Nfname.value == "") {
                document.getElementById("msgFName").innerHTML = "&nbsp;First name can't be empty";
                document.getElementById("msgFName").style.color = "RED";
                event.returnValue = false;
                document.addForm.Nfname.focus();
                valid = false;
            }
            if (document.addForm.Npassword.value == "") {
                document.getElementById("msgPassword").innerHTML = "&nbsp;Password can't be empty";
                document.getElementById("msgPassword").style.color = "RED";
                document.addForm.Npassword.focus();
                valid = false;
            }
            if (document.addForm.Nusername.value == "") {
                document.getElementById("msgUserName").innerHTML = "&nbsp;User name can't be empty";
                document.getElementById("msgUserName").style.color = "RED";
                document.addForm.Nusername.focus();
                valid = false;
            }
            valid = validateEmail(document.addForm.Nemail);
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
                    <p>

                    <h1>Add User</h1></p>
                </td>
            </tr>
            <tr>
                <td>
                    <p>

                    <h2>add user </h2></p>
                    <div><p style="color:red;">Boxes with an asterisk next to them are required</p></div>
                    <table borderColor=#0000ff cellSpacing=1 cellPadding=1 align=left bgColor=#ffffff border=0
                           width="100%">
                        <tr>
                            <td class="">Login *</td>
                            <td><input id="Nusername" type="text" name="Nusername" onchange="validateLoginName();"
                                       onfocus="this.style.background='orange'" onblur="this.style.background='white'"/>
                            </td>
                            <td id="msgUserName"></td>
                        </tr>
                        <tr>
                            <td class="">Password *</td>
                            <td><input class="textFeild" id="Npassword" type="password" name="Npassword"
                                       onfocus="this.style.background='orange'" onblur="this.style.background='white'"/>
                            </td>
                            <td id="msgPassword" align="left"></td>
                        </tr>
                        <tr>
                            <td class="">Fist name *</td>
                            <td><input id="Nfname" type="text" name="Nfname" onfocus="this.style.background='orange'"
                                       onblur="this.style.background='white'"/></td>
                            <td id="msgFName" align="left"></td>
                        </tr>
                        <tr>
                            <td class="">Last name *</td>
                            <td><input id="Nlname" type="text" name="Nlname" onfocus="this.style.background='orange'"
                                       onblur="this.style.background='white'"/></td>
                            <td id="msgLName"></td>
                        </tr>
                        <tr>
                            <td class="">Phone</td>
                            <td><input id="Nphone" type="text" name="Nphone" onfocus="this.style.background='orange'"
                                       onblur="this.style.background='white'"/></td>
                            <td id="msgPhone"></td>
                        </tr>
                        <tr>
                            <td class="">Email</td>
                            <td><input id="Nemail" type="text" name="Nemail" onfocus="this.style.background='orange'"
                                       onblur="this.style.background='white'"/></td>
                            <td id="msgEmail" align="left" height="22"></td>
                        </tr>
                        <tr>
                            <td class="">Role *</td>
                            <td>
                                <select id="Nrole" name="Nrole" class="dropDownList" style="width:130px">
                                    <option value="0"></option>
                                    <option value="1"><%=session.getAttribute("user.role.admin")%>
                                    </option>
                                    <option value="2"><%=session.getAttribute("user.role.regular")%>
                                    </option>
                                    <option value="3"><%=session.getAttribute("user.role.advanced")%>
                                    </option>
                                </select>
                            </td>
                            <td id="msgRole"></td>
                        </tr>
                        <tr>
                            <td class="">Company</td>
                            <td>
                                <div id="existingCompanyDiv" name="existingCompanyDiv" style="display:block;">
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
                                    <input type="text" name="Ncompany" value="" onfocus="this.style.background='orange'"
                                           onblur="this.style.background='white'"/>
                                </div>
                            </td>
                            <td><input type="checkbox" id="newCompany" name="newCompany" onclick="showNewCompany()"> Add
                                Company
                            </td>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="btnCancel" name="btnCancel"
                            onclick='return back("./all-users.html");'><%=session.getAttribute("button.cancel")%>
                    </button>
                    <button type="submit" onclick='return validate();'><%=session.getAttribute("button.ok")%>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
