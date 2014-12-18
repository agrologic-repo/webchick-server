<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.dao.LanguageDao" %>
<%@ page import="com.agrologic.app.model.Language" %>
<%@ page import="com.agrologic.app.model.User" %>


<% User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    Collection<Language> languages = languageDao.geAll();
    session.setAttribute("languages", languages);
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Add program</title>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
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

    <script type="text/javascript">
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
                        success: function (data) {
                            response($.map(data.programsMap, function (item) {
                                return {
                                    label: item.key,
                                    value: item.key + ":" + item.value
                                }
                            }));
                        }
                    });
                },
                select: function (event, ui) {
                    var arr = ui.item.value.split(':');
                    $("input#program").val(arr[0]);
                    $("input#Selectedprogramid").val(arr[1]);
                    return false;
                }
            }).focus(function () {
                $(this).autocomplete("search", "")
            });
        });
        function reset() {
            document.getElementById("msgProgramId").innerHTML = "";
            document.getElementById("msgName").innerHTML = "";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Nprogramid.value == "") {
                document.getElementById("msgProgramId").innerHTML = "&nbsp;Program ID can't be empty";
                document.getElementById("msgProgramId").style.color = "RED";
                document.addForm.Nprogramid.focus();
                valid = false;
            }

            if (document.addForm.Nname.value == "") {
                document.getElementById("msgName").innerHTML = "&nbsp;Program name can't be empty";
                document.getElementById("msgName").style.color = "RED";
                document.addForm.Nname.focus();
                valid = false;
            }


            if (!IsNumeric(document.addForm.Nprogramid.value)) {
                document.getElementById("msgProgramId").innerHTML = "&nbsp;Invalid value in program id ";
                document.getElementById("msgProgramId").style.color = "RED";
                document.addForm.Nprogramid.focus();
                valid = false;
            }

            if (!valid) {
                return false;
            }
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
        var req;
        function validateProgId() {
            var programId = document.getElementById("Nprogramid");
            var url = "progidcheckservlet?programId=" + encodeURIComponent(programId.value);
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
                var msgOutout = document.getElementById("msgProgramId");
                msgOutout.innerHTML = msg;
                msgOutout.style.color = (msg == "program id valid") ? "green" : "red";
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
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Add Program</h1></p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>

                            <h2>add new program</h2>

                            <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                            <form id="addForm" name="addForm" action="./addprogram.html" method="post">
                                <table width="auto" align="left">
                                    <tr>
                                        <td align="left">Program ID *</td>
                                        <td>
                                            <input id="Nprogramid" type="text" name="Nprogramid" style="width:120px"
                                                   onchange="validateProgId();">
                                        </td>

                                        <td id="msgProgramId"></td>
                                    </tr>
                                    <tr>
                                        <td align="left">Program name *</td>
                                        <td><input id="Nname" type="text" name="Nname" style="width:120px"></td>
                                        <td id="msgName"></td>
                                    </tr>
                                    <tr>
                                        <td align="left">Select Program *</td>
                                        <td colspan="2">
                                            <input id="program" name="program" style="width:120px"/>
                                            <input id="Selectedprogramid" type="hidden" name="Selectedprogramid"
                                                   style="width:100px"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            &nbsp;
                                        </td>

                                        <td>
                                            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                                                <legend>Options to add </legend>
                                                <input type="checkbox" name="relays">Relays<br>
                                                <input type="checkbox" name="alarms">Alarms<br>
                                                <input type="checkbox" name="systemstates">System States<br>
                                                <hr>
                                                <input type="checkbox" name="specialdata" onclick="check()">Special data<br>
                                            </fieldset>
                                        </td>
                                        <td>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <button id="btnCancel" name="btnCancel"
                                                    onclick='return back("./all-programs.html");'>
                                                <%=session.getAttribute("button.cancel") %>
                                            </button>
                                            <button id="btnAdd" type="submit" name="btnAdd"
                                                    onclick="return validate();"><%=session.getAttribute("button.ok") %>
                                            </button>
                                        </td>
                                    </tr>
                                </table>
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
