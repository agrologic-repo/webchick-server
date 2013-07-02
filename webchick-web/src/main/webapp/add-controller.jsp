<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Program" %>

<jsp:directive.page import="java.util.Collection"/>

<% UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Collection<Program> programs = (Collection<Program>) request.getSession().getAttribute("programs");
    Collection<String> controllernames = (Collection<String>) request.getSession().getAttribute("controllernames");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><%=session.getAttribute("new.controller.page.title")%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <script type="text/javascript" language="javascript">
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
        function reset() {
            document.getElementById("msgNetName").innerHTML = "";
            document.getElementById("msgProgramId").innerHTML = "";
            document.getElementById("msgTitle").innerHTML = ""
        }
        function validate() {
            reset();
            if (document.addForm.Ntitle.value == "") {
                document.getElementById("msgTitle").innerHTML = "&nbsp; Title can't be empty";
                document.getElementById("msgTitle").style.color = "RED";
                event.returnValue = false;
                document.addForm.Ntitle.focus();
            } else {
                document.addForm.Ntitle.value = encode(document.addForm.Ntitle);
            }
            if (document.addForm.Nnetname.value == "") {
                document.getElementById("msgNetName").innerHTML = "&nbsp;Net name can't be empty";
                document.getElementById("msgNetName").style.color = "RED";
                event.returnValue = false;
                document.addForm.Nnetname.focus();
            }
            if (document.addForm.Nprogramid.value == "None") {
                document.getElementById("msgProgramId").innerHTML = "&nbsp;Program Version doesn't choosed";
                document.getElementById("msgProgramId").style.color = "RED";
                event.returnValue = false;
                document.addForm.Nprogramid.focus();
            }
            if (!valid) {
                return false;
            }
        }
        function encode(txtObj) {
            var source = txtObj.value;
            var result = '';
            for (i = 0; i < source.length; i++)
                result += '&#' + source.charCodeAt(i) + ';';
            return result;
        }
        function showNewName() {
            var checked = addForm.newControllerName.checked;
            if (checked == true) {
                document.getElementById('existingNameDiv').style.display = "none";
                document.getElementById('newNameDiv').style.display = "inline";
            } else {
                document.getElementById('existingNameDiv').style.display = "inline";
                document.getElementById('newNameDiv').style.display = "none";
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
                <form id="addForm" name="addForm" action="./addcontroller.html" method="post">
                    <table border=0 cellPadding=1 cellSpacing=1>
                        <tr>
                            <td>
                                <h1><%=session.getAttribute("new.controller.page.header")%>
                                </h1>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2><%=session.getAttribute("new.controller.page.sub.header")%>
                                </h2>

                                <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                                <table>
                                    <input id="userId" type="hidden" name="userId" class=rightTitles
                                           value="<%=userId %>">
                                    <input id="cellinkId" type="hidden" name="cellinkId" class=rightTitles
                                           value="<%=cellinkId %>">
                                    <tr>
                                        <td>Title *</td>
                                        <td><input id="Ntitle" type="text" name="Ntitle" style="width:100px"></td>
                                        <td id="msgTitle"></td>
                                    </tr>
                                    <tr>
                                        <td>Net name *</td>
                                        <td><input id="Nnetname" type="text" name="Nnetname" style="width:100px"></td>
                                        <td id="msgNetName"></td>
                                    </tr>
                                    <tr>
                                        <td> Program Version *</td>
                                        <td>
                                            <select id="Nprogramid" name="Nprogramid" style="width:120px">
                                                <option value="None" selected>Select
                                                        <% for(Program p:programs) {%>
                                                <option value="<%=p.getId() %>"><%=p.getName() %>
                                                        <%}%>
                                            </select>
                                        </td>
                                        <td id="msgProgramId"></td>
                                    </tr>
                                    <tr>
                                        <td>Name&nbsp;</td>
                                        <td>
                                            <div id="existingNameDiv" name="existingNameDiv" style="display:block;">
                                                <select id="Ncontrollernamelist" name="Ncontrollernamelist"
                                                        class="dropDownList" style="width:130px">
                                                    <option value=""></option>
                                                    <% for (String n : controllernames) {%>
                                                    <option value="<%=n%>"><%=n%>
                                                    </option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div id="newNameDiv" name="newNameDiv" style="display:none;">
                                                <input type="text" name="Ncontrollername"
                                                       onfocus="this.style.background='orange'"
                                                       onblur="this.style.background='white'"/>
                                            </div>
                                        </td>
                                        <td><input type="checkbox" id="newControllerName" name="newControllerName"
                                                   onclick="showNewName()">Add Name</input></td>
                                    </tr>
                                    <tr>
                                        <td>Status</td>
                                        <td>
                                            <input type="checkbox" name="Nactive" id="Nactive" checked>Active</input>
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <%if (user.getRole() == UserRole.ADMINISTRATOR) {%>
                                <button name="btnCancel" type="button" onclick='window.history.back()'>
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                                <%} else {%>
                                <button name="btnCancel" type="button" onclick="window.history.back()">
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                                <%}%>
                                <button id="btnAdd" name="btnAdd" type="submit" onclick="return validate();">
                                    <%=session.getAttribute("button.ok") %>
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