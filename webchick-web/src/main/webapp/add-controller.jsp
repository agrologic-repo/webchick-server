<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><%=session.getAttribute("controller.page.add.title")%>
    </title>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
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
                    $( "input#programid" ).val( arr[1] );
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
            if (document.addForm.title.value == "") {
                document.getElementById("msgTitle").innerHTML = "Field can't be empty";
                document.getElementById("msgTitle").style.color = "RED";
                event.returnValue = false;
                document.addForm.title.focus();
                valid = false;
            } else {
                document.addForm.title.value = encode(document.addForm.title);
            }


            if (document.addForm.spinner.value == "") {
                document.getElementById("msgNetName").innerHTML = "Field can't be empty";
                document.getElementById("msgNetName").style.color = "RED";
                event.returnValue = false;
                document.addForm.spinner.focus();
                valid = false;
            }

            if(document.addForm.controllerType.value == "") {
                document.getElementById("msgControllerType").innerHTML = "Field can't be empty";
                document.getElementById("msgControllerType").style.color = "RED";
                event.returnValue = false;
                document.addForm.controllerType.focus();
                valid = false;
            }

            if (document.addForm.program.value == "") {
                document.getElementById("msgProgramId").innerHTML = "Field can't be empty";
                document.getElementById("msgProgramId").style.color = "RED";
                event.returnValue = false;
                document.addForm.title.focus();
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
                                <h1><%=session.getAttribute("controller.page.add.header")%>
                                </h1>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2><%=session.getAttribute("controller.page.sub.header")%>
                                </h2>

                                <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                                <table>
                                    <input id="userId" type="hidden" name="userId"
                                           value="<%=userId %>">
                                    <input id="cellinkId" type="hidden" name="cellinkId"
                                           value="<%=cellinkId %>">
                                    <tr>
                                        <td>House Name *</td>
                                        <td><input id="title" type="text" name="title" style="width:100px"></td>
                                        <td id="msgTitle"></td>
                                    </tr>
                                    <tr>
                                        <td>Net name *</td>
                                        <td>
                                            <input id="spinner" name="spinner" style="width:20px"/>
                                            <input id="netname" type="hidden" name="netname" value="">

                                        </td>
                                        <td id="msgNetName"></td>
                                    </tr>

                                    <tr>
                                        <td>Type *</td>
                                        <td>
                                            <input id="controllerType" name="controllerType" style="width:100px"/>
                                        </td>
                                        <td id="msgControllerType"></td>
                                    </tr>
                                    <tr>
                                        <td> Program *</td>
                                        <td>
                                            <input id="program" name="program" style="width:100px"/>
                                            <input id="programid" type="hidden"  name="programid" style="width:100px"/>
                                        </td>
                                        <td id="msgProgramId"></td>
                                    </tr>
                                    <tr>
                                        <td>Status</td>
                                        <td>
                                            <input type="checkbox" name="active" id="active" checked>Active</input>
                                        </td>
                                        <td></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <%if (user.getRole() == UserRole.ADMIN) {%>
                                <button name="btnCancel" type="button"
                                        onclick='return back("./cellink-setting.html?userId=<%=userId %>&cellinkId=<%=cellinkId%>");'>
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                                <%} else {%>
                                <button name="btnCancel" type="button"
                                        onclick='return back("./cellink-setting.html?userId=<%=userId %>&cellinkId=<%=cellinkId%>");'>
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