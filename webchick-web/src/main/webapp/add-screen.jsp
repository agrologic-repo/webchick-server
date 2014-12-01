<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Program" %>
<%@ page import="com.agrologic.app.model.Screen" %>
<%@ page import="com.agrologic.app.model.User" %>


<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String param = request.getParameter("programId");
    if (param == null) {
        param = "1";
    }

    Long programId = Long.parseLong(param);
    Collection<Program> programs = (Collection<Program>) request.getAttribute("programs");

%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add Screen</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function showScreens(object) {
            if (object == "program") {
                var selectedIndex = document.addForm1.program.selectedIndex;
                var val = document.addForm1.program.options[selectedIndex].value;
                var screens = "screen" + val;
                var screen = document.getElementById(screens);
                screen.style.display = "block"
                var elements = document.getElementsByTagName("SELECT");
                var size = elements.length;
                for (var i = 0; i < size; i++) {
                    var elmId = elements[i].id
                    if (screens != elmId && object != elmId) {
                        document.getElementById(elmId).style.display = "none";
                    }
                }
            }
        }
        function optionToAddScreen(divToShow) {
            if (divToShow == "ns") {
                document.getElementById('newScreen').style.display = "inline";
                document.getElementById('chooseExisting').style.display = "none";
            }
            if (divToShow == "es") {
                document.getElementById('newScreen').style.display = "none";
                document.getElementById('chooseExisting').style.display = "inline";
            }
        }
        function save() {
            var spid = document.addForm1.programs.value;
            var ssid = "screen" + document.addForm1.programs.value;
            document.getElementById("selectedProgramId").value = spid;
            document.getElementById("selectedScreenId").value = document.getElementById(ssid).value;
        }
        function reset() {
            document.getElementById("msgScreenTitle").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm2.NscreenTitle.value == "") {
                document.getElementById("msgScreenTitle").innerHTML = "&nbsp;Screen title can't be empty";
                document.getElementById("msgScreenTitle").style.color = "RED";
                document.addForm2.NscreenTitle.focus();
                valid = false;
            }
            if (!valid) {
                return false;
            }
            var spid = document.addForm1.programs.value;
            var ssid = "screen" + document.addForm1.programs.value;

        }
        function IsNumeric(sText) {
            var ValidChars = "0123456789.";
            var IsNumber = true;
            var Char;

            for (i = 0; i < sText.length && IsNumber == true; i++) {
                Char = sText.charAt(i);
                if (ValidChars.indexOf(Char) == -1) {
                    IsNumber = false;
                }
            }
            return IsNumber;
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

                            <h1>Add Screen</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>add new screen</h2>
                            <div><br>
                                <input type="radio" name="group1" id="ns" checked onclick="optionToAddScreen('ns')">&nbsp;New
                                Screen
                                <input type="radio" name="group1" id="es" onclick="optionToAddScreen('es')">&nbsp;Choose
                                Existing <br>
                            </div>
                            <form id="addForm1" name="addForm1" action="./add-selected-screen.html" method="post"
                                  onsubmit="save()">
                                <input type="hidden" id="programId" name="programId" value="<%=programId%>">
                                <input type="hidden" id="selectedProgramId" name="selectedProgramId">
                                <input type="hidden" id="selectedScreenId" name="selectedScreenId">

                                <div id="chooseExisting" style="display:none;">
                                    <div><p style="color:red;">Choose program from which you want select screen</div>
                                    <table>
                                        <tr>
                                            <td>Program Version</td>
                                            <td>
                                                <% Long defaultProgId = new Long(1); %>
                                                <select name="programs" id="program" onclick="showScreens('program')">
                                                    <% for (Program p : programs) {%>
                                                    <% if (p.getId().equals(defaultProgId)) {%>
                                                        <option value="<%=p.getId() %>" selected><%=p.getName() %>
                                                    <%} else if(!p.getId().equals(programId)){%>
                                                        <option value="<%=p.getId() %>"><%=p.getName() %>
                                                    <%}%>
                                                    <%}%>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Choose Screen</td>
                                            <td valign="center">
                                                <%for (Program p : programs) {%>
                                                <p>
                                                    <%if(p.getId()== 1){%>
                                                        <select name="screen<%=p.getId()%>" id="screen<%=p.getId()%>" style="display:block">
                                                        <%for (Screen s : p.getScreens()) {%>
                                                            <option value="<%=s.getId()%>"><%=s.getTitle()%>
                                                        <%}%>
                                                        </select>
                                                    <%} else if(!p.getId().equals(programId)){%>
                                                        <select name="screen<%=p.getId()%>" id="screen<%=p.getId()%>" style="display:none">
                                                        <%for (Screen s : p.getScreens()) {%>
                                                            <option value="<%=s.getId()%>"><%=s.getTitle()%>
                                                        <%}%>
                                                        </select>
                                                    <%}%>
                                                <%}%>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="btnCancel" name="btnCancel"
                                                        onclick='return back("./all-screens.html?programId=<%=programId%>");'><%=session.getAttribute("button.cancel") %>
                                                </button>
                                                <button id="btnAdd" name="btnAdd"
                                                        type="submit"><%=session.getAttribute("button.ok") %>
                                                </button>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </form>
                            <form id="addForm2" name="addForm2" action="./addscreen.html" method="post"
                                  onsubmit="return validate();">
                                <div id="newScreen" style="display:block;">
                                    <input type="hidden" id="programId" name="programId" value="<%=programId%>">

                                    <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                                    <p>
                                    <table width="auto" align="left">
                                        <tr>
                                            <td align="left">Screen Title *</td>
                                            <td>
                                                <input id="NscreenTitle" type="text" name="NscreenTitle"
                                                       style="width:120px"></td>
                                            <td id="msgScreenTitle"></td>
                                        </tr>
                                        <tr>
                                            <td valign="top">Description</td>
                                            <td><textarea name="NscreenDescript" cols="60" rows="6"></textarea></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="btnCancel" name="btnCancel"
                                                        onclick='return back("./all-screens.html?programId=<%=programId%>");'><%=session.getAttribute("button.cancel") %>
                                                </button>
                                                <button id="btnAdd" name="btnAdd"
                                                        type="submit"><%=session.getAttribute("button.ok") %>
                                                </button>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
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
