<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.User" %>


<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Controller editController = (Controller) request.getAttribute("editcontroller");
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("controller.page.edit.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <style>
        .ui-autocomplete {
            max-height: 200px;
            overflow-y: auto;
            overflow-x: hidden;
        }
    </style>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
            $("input#controllerType").autocomplete({
                width: 300,
                max: 10,
                delay: 500,
                minLength: 0,
                autoFocus: true,
                cacheLength: 0,
                scroll: true,
                highlight: false,
                source: function (request, response) {
                    $.ajax({
                        url: "./autocomplete-controller-name",
                        dataType: "json",
                        data: request,
                        success: function( data) {
                            response( $.map( data.sendString, function( item ) {
                                return {
                                    label: item.key,
                                    value: item.key + ":" + item.value
                                }
                            }));
                        }
                    });
                },
                select: function( event, ui ) {
                    var arr = ui.item.value.split(':');
                    $( "#netname" ).val(arr[1]);
                    $( "input#controllerType" ).val( arr[0] );
                    return false;
                }
            }).focus(function () {$(this).autocomplete("search","")});
        });
        /**
         * spin for net name {0-99}
         */
        $(function () {
            $("#spinner").spinner({
                spin: function( event, ui ) {
                    if ( ui.value > 99 ) {
                        $( this ).spinner( "value", 0 );
                        return false;
                    } else if ( ui.value < 0 ) {
                        $( this ).spinner( "value", 99 );
                        return false;
                    }
                }
            });
        });
        /**
         * set net name by controller type and net number
         */
        function setNetName() {
            var temp = $("#netname").val();
            if (temp.length > 2) {
                temp = temp.substring(0, 2);
            }
            var i = $("#spinner").val();
            if (i / 10 >= 1) {
                temp = temp + i;
            } else {
                temp = temp + "0" + i;
            }
            $("#netname").val(temp);
        }

        $(document).ready(function () {
            $("input#program").autocomplete({
                width: 300,
                max: 10,
                delay: 500,
                minLength: 0,
                autoFocus: true,
                cacheLength: 0,
                scroll: true,
                highlight: false,
                source: function (request, response) {
                    $.ajax({
                        url: "./autocomplete-program",
                        dataType: "json",
                        data: request,
                        success: function( data) {
                            response( $.map( data.programsMap, function( item ) {
                                return {
                                    label: item.key,
                                    value: item.key + ":" + item.value
                                }
                            }));
                        }
                    });
                },
                select: function( event, ui ) {
                    var arr = ui.item.value.split(':');
                    $( "input#program" ).val( arr[0] );
                    $( "input#programId" ).val( arr[1] );
                    return false;
                }
            }).focus(function () {$(this).autocomplete("search","")});
        });

        function reset() {
            document.getElementById("msgTitle").innerHTML = ""
            document.getElementById("msgNetName").innerHTML = "";
            document.getElementById("msgControllerType").innerHTML = "";
            document.getElementById("msgProgramId").innerHTML = "";
        }
        function validate() {
            reset();
            var valid = true;
            if (document.editForm.title.value == "") {
                document.getElementById("msgTitle").innerHTML = "Field can't be empty";
                document.getElementById("msgTitle").style.color = "RED";
                event.returnValue = false;
                document.editForm.title.focus();
                valid = false;
            } else {
                document.editForm.title.value = encode(document.editForm.title);
            }

            if (document.editForm.spinner.value == "") {
                document.getElementById("msgNetName").innerHTML = "Field can't be empty";
                document.getElementById("msgNetName").style.color = "RED";
                event.returnValue = false;
                document.editForm.spinner.focus();
                valid = false;
            }

            if(document.editForm.controllerType.value == "") {
                document.getElementById("msgControllerType").innerHTML = "Field can't be empty";
                document.getElementById("msgControllerType").style.color = "RED";
                event.returnValue = false;
                document.editForm.controllerType.focus();
                valid = false;
            }

            if (document.editForm.program.value == "") {
                document.getElementById("msgProgramId").innerHTML = "Field can't be empty";
                document.getElementById("msgProgramId").style.color = "RED";
                event.returnValue = false;
                document.editForm.title.focus();
                valid = false;
            }

            if (!valid) {
                return false;
            }

            setNetName();
        }
        function encode(txtObj) {
            var source = txtObj.value;
            var result = '';
            for (i = 0; i < source.length; i++)
                result += '&#' + source.charCodeAt(i) + ';';
            return result;
        }
        function showNewName() {
            var checked = editForm.newControllerName.checked;
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
    <form id="editForm" name="editForm"
          action="editcontroller.html?userId=<%=userId%>&cellinkId=<%=cellinkId %>&controllerId=<%=editController.getId() %>"
          method="post" onsubmit="return validate();">
        <table border="0" cellPadding=1 cellSpacing=1 width="100%">
            <tr>
                <td valign="top" style="padding-top:0px">
                    <h1><%=session.getAttribute("controller.page.edit.title")%></h1>

                    <p>

                    <h2><%=session.getAttribute("controller.page.edit.title")%> <%=editController.getId() %>
                    </h2>

                    <div><p style="color:red">Boxes with an asterisk next to them are required</p></div>
                    <p>

                    <table>
                        <input id="userId" type="hidden" name="userId"
                               value="<%=userId %>">
                        <input id="cellinkId" type="hidden" name="cellinkId"
                               value="<%=cellinkId %>">
                        <tr>
                            <td>House Name *</td>
                            <td><input id="title" type="text" name="title" style="width:100px"
                                       value="<%=editController.getTitle()%>"></td>
                            <td id="msgTitle"></td>
                        </tr>
                        <tr>
                            <td>Net name *</td>
                            <td>
                                <input id="spinner" name="spinner" style="width:20px"
                                       value="<%=editController.getNetNumber()%>"/>
                                <input id="netname" type="hidden" name="netname"
                                       value="<%=editController.getNetName()%>">
                            </td>
                            <td id="msgNetName"></td>
                        </tr>

                        <tr>
                            <td>Type *</td>
                            <td>
                                <input id="controllerType" name="controllerType" style="width:100px"
                                        value="<%=editController.getName()%>"/>
                            </td>
                            <td id="msgControllerType"></td>
                        </tr>
                        <tr>
                            <td> Program *</td>
                            <td>
                                <input id="program" name="program" style="width:100px"
                                       value="<%=editController.getProgram().getName()%>"/>
                                <input id="programId" type="hidden"  name="programId" style="width:100px"
                                        value="<%=editController.getProgramId()%>"/>
                            </td>
                            <td id="msgProgramId"></td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td>
                                <%if (editController.isActive()) {%>
                                <input type="checkbox" name="active" id="active" checked>
                                <%} else {%>
                                <input type="checkbox" name="active" id="active">
                                <%}%>
                            </td>
                            <td></td>
                        </tr>
                    </table>

                    <%--<table>--%>
                        <%--<tr>--%>
                            <%--<td class="rightCell">Title *</td>--%>
                            <%--<td><input type="text" name="Ntitle" style="width:100px"--%>
                                       <%--value="<%=editController.getTitle()%>"></td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td class="rightCell">Net Name *</td>--%>
                            <%--<td><input type="text" name="Nnetname" style="width:100px"--%>
                                       <%--value="<%=editController.getNetName()%>"></td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td class="rightCell"> Program Version *</td>--%>
                            <%--<td align="left">--%>
                                <%--<select id="NprogramId" name="NprogramId" style="width:120px">--%>
                                    <%--<option value="None" selected>Select--%>
                                            <%--<% for(Program p:programs) {%>--%>
                                    <%--<option value="<%=p.getId() %>"><%=p.getName() %>--%>
                                            <%--<%}%>--%>
                                <%--</select>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td class="rightCell">Name&nbsp;</td>--%>
                            <%--<td>--%>
                                <%--<div id="existingNameDiv" name="existingNameDiv" style="display:block;">--%>
                                    <%--<select id="Ncontrollernamelist" name="Ncontrollernamelist" class="dropDownList"--%>
                                            <%--style="width:130px">--%>
                                        <%--<option value=""></option>--%>
                                        <%--<% for (String n : controllernames) {%>--%>
                                        <%--<option value="<%=n%>"><%=n%>--%>
                                        <%--</option>--%>
                                        <%--<%}%>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                                <%--<div id="newNameDiv" name="newNameDiv" style="display:none;">--%>
                                    <%--<input type="text" name="Ncontrollername" onfocus="this.style.background='orange'"--%>
                                           <%--onblur="this.style.background='white'"/>--%>
                                <%--</div>--%>
                            <%--</td>--%>
                            <%--<td><input type="checkbox" id="newControllerName" name="newControllerName"--%>
                                       <%--onclick="showNewName()">--%>
                                <%--Add Name</input></td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td class="rightCell">Status</td>--%>
                            <%--<td>--%>
                                <%--<%if (editController.isActive()) {%>--%>
                                <%--<input type="checkbox" name="Nactive" id="Nactive" checked>--%>
                                <%--<%} else {%>--%>
                                <%--<input type="checkbox" name="Nactive" id="Nactive">--%>
                                <%--<%}%>--%>
                                <%--Active</input>--%>
                            <%--</td>--%>
                            <%--<td class="rightCell">&nbsp;</td>--%>
                        <%--</tr>--%>

                    <%--</table>--%>

                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <%if (user.getRole() == UserRole.ADMIN) {%>
                    <button name="btnCancel"
                            onclick='return back("./cellink-setting.html?userId=<%=userId %>&cellinkId=<%=cellinkId%>");'>
                        <%=session.getAttribute("button.cancel") %>
                    </button>
                    <%} else {%>
                    <button name="btnCancel"
                            onclick='return back("./cellink-setting.html?userId=<%=userId %>&cellinkId=<%=cellinkId%>");'>
                        <%=session.getAttribute("button.cancel") %>
                    </button>
                    <%}%>
                    <button name="btnOk" type="submit">
                        <%=session.getAttribute("button.ok") %>
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>

<%--<script language="Javascript">--%>
    <%--var length = document.editForm.NprogramId.options.length;--%>
    <%--var programId =--%>
    <%--<%= editController.getProgramId() %>--%>
    <%--for (var i = 0; i < length; i++) {--%>
        <%--if (document.editForm.NprogramId.options[i].value == programId) {--%>
            <%--document.editForm.NprogramId.selectedIndex = i;--%>
            <%--break;--%>
        <%--}--%>
    <%--}--%>

    <%--var length = document.editForm.Ncontrollernamelist.options.length;--%>
    <%--var ctrlname = '<%= editController.getName() %>'--%>
    <%--for (var i = 0; i < length; i++) {--%>
        <%--if (document.editForm.Ncontrollernamelist.options[i].value == ctrlname) {--%>
            <%--document.editForm.Ncontrollernamelist.selectedIndex = i;--%>
            <%--break;--%>
        <%--}--%>
    <%--}--%>
<%--</script>--%>
</body>
</html>