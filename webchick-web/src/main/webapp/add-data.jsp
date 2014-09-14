<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.LanguageDao" %>
<%@ page import="com.agrologic.app.model.Language" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long tableId = Long.parseLong(request.getParameter("tableId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    Long programId = Long.parseLong(request.getParameter("programId"));
    Integer position = Integer.parseInt(request.getParameter("position"));

    LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    Collection<Language> languages = languageDao.geAll();
    session.setAttribute("languages", languages);

%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
<meta http-equiv="Content-type" value="text/html; charset=utf-8">
<title>Add New Data</title>
<link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
<link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
<STYLE TYPE="text/css" media="all">
    .ui-autocomplete {
        position: absolute;
        cursor: default;
        height: 200px;
        overflow-y: scroll;
        overflow-x: hidden;
    }
</STYLE>

<script type="text/javascript" src="resources/javascript/jquery.js">;</script>
<script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
<script type="text/javascript">
    $(document).ready(function () {
        $("input#dataType").autocomplete({
            width: 300,
            max: 10,
            delay: 100,
            minLength: 1,
            autoFocus: true,
            cacheLength: 1,
            scroll: true,
            highlight: false,
            source: function (request, response) {
                $.ajax({
                    url: "./autocomplete-data",
                    dataType: "json",
                    data: request,
                    success: function (data, textStatus, jqXHR) {
                        console.log(data);
                        var items = data;
                        response(items);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(textStatus);
                    }
                });
            },
            select: function (event, ui) {
                var v = ui.item.value;
                if (v == "") {
                    $('#msgDataType').html("Missing requested Data ID : { " + v + " } ");
                    return false;
                } else {
                    $('#msgDataType').html("Requested Data ID  is exist : { " + v + " } ");
                }
                // update what is displayed in the textbox
                this.value = v;
                return false;
            }

        });
    });
</script>
<script type="text/javascript">
    var actionName = ''
    function optionToAddScreen(divToShow) {
        if (divToShow == "one") {
            document.getElementById('addOne').style.display = "inline";
            document.getElementById('addMulti').style.display = "none";
        }
        if (divToShow == "mul") {
            document.getElementById('addOne').style.display = "none";
            document.getElementById('addMulti').style.display = "inline";
        }
    }
    /**
     *
     * @param langid
     */
    function getTranslation(langid) {
        var dataId = document.addForm.dataType.value
        if (dataId == "") {
            return;
        }
        var url = "translate-data?dataId=" + encodeURIComponent(dataId) + "&langId=" + langid;
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            req = new ActiveXObject("Microsoft.XMLHTTP");
        }
        req.open("GET", url, true);
        req.onreadystatechange = callbackTranslation;
        req.send(null);
    }
    function callbackTranslation() {
        if (req.readyState == 4 && req.status == 200) {
            var msgNode = req.responseXML.getElementsByTagName("message")[0];
            var msg = msgNode.childNodes[0].nodeValue;
            alert(msg);
        }
    }

    function resetMulti() {
        document.getElementById("msgStartDataID").innerHTML = "";
        document.getElementById("msgEndDataID").innerHTML = "";
        document.getElementById("msgStartpos").innerHTML = "";
    }
    function validateMulti() {
        var valid = true;
        resetMulti();

        if (document.addMultiForm.startdataId.value == "") {
            document.getElementById("msgStartDataID").innerHTML = "&nbsp;Start DataID can't be empty";
            document.getElementById("msgStartDataID").style.color = "RED";
            document.addMultiForm.startdataId.focus();
            valid = false;
        }
        if (document.addMultiForm.enddataId.value == "") {
            document.getElementById("msgEndDataID").innerHTML = "&nbsp;End DataID can't be empty";
            document.getElementById("msgEndDataID").style.color = "RED";
            document.addMultiForm.enddataId.focus();
            valid = false;
        }
        if (document.addMultiForm.startpos.value == "") {
            document.getElementById("msgStartpos").innerHTML = "&nbsp;Start position can't be empty";
            document.getElementById("msgStartpos").style.color = "RED";
            document.addMultiForm.startpos.focus();
            valid = false;
        }
        if (!valid) {
            return false;
        }
        actionName = "addMultiForm";
        document.getElementById("loading").innerHTML = "adding data set .....";
        document.getElementById("loading").style.color = "GREEN";
    }
    function reset() {
        document.getElementById("msgDataType").innerHTML = "";
        document.getElementById("msgNewDataLabel").innerHTML = "";
    }
    function validate() {
        var valid = true;
        reset();
        if (document.addForm.dataType.value == "") {
            document.getElementById("msgDataType").innerHTML = "&nbsp;Data Source can't be empty";
            document.getElementById("msgDataType").style.color = "RED";
            document.addForm.dataType.focus();
            valid = false;
        }
        if (document.addForm.chbox.checked == true) {
            if (document.addForm.Nlabel.value == "") {
                document.getElementById("msgNewDataLabel").innerHTML = "&nbsp;New Label can't be empty";
                document.getElementById("msgNewDataLabel").style.color = "RED";
                document.addForm.Nlabel.focus();
                valid = false;
            } else {
                document.addForm.Nlabel.value = encode(document.addForm.Nlabel);
            }
        }

        if (!valid) {
            return false;
        }
        actionName = "addForm";
    }

    function closeWindow() {
        self.close();
        window.opener.location.reload(true);
    }
    function check() {
        if (document.addForm.chbox.checked == true) {
            document.addForm.langListBox.disabled = false;
        } else {
            document.addForm.langListBox.disabled = true;
        }
    }
    function encode(txtObj) {
        var source = txtObj.value;
        var result = '';
        for (i = 0; i < source.length; i++) {
            result += '&#' + source.charCodeAt(i) + ';';
        }
        return result;
    }
    function showProgress() {
        if (actionName == "addForm") {
            closeWindow();
        } else {
            var start = document.getElementById("startdataId").value;
            var end = document.getElementById("enddataId").value;
            var dif = end - start;
            wait(100 * dif);
            closeWindow();
        }
    }
    function wait(msecs) {
        document.getElementById("loading").innerHTML = "adding data set .";
        var text = document.getElementById("loading").innerHTML;
        var start = new Date().getTime();
        var cur = start
        while (cur - start < msecs) {
            document.getElementById("loading").innerHTML = text + ".";
            document.getElementById("loading").style.color = "GREEN";
            cur = new Date().getTime();
            text = document.getElementById("loading").innerHTML;
        }
    }
</script>
</head>
<body onunload="showProgress();">
<table class="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
    <tr>
        <td>
            <h1>Add Data</h1>

            <h2>add data to screen table </h2><br>

            <div><br>
                <input type="radio" name="group1" id="one" checked onclick="optionToAddScreen('one')">&nbsp;Add one data
                <input type="radio" name="group1" id="mul" onclick="optionToAddScreen('mul')">&nbsp;Add data set <br>
            </div>
            <br>

            <div id="addOne" style="display:block;">
                <form id="addForm" name="addForm" action="./addtabledata.html" method="post"
                      onsubmit="return validate();">
                    <table width="100%" align="left" border="0">
                        <input type="hidden" id="programId" name="programId" value="<%=programId%>"/>
                        <input type="hidden" id="screenId" name="screenId" value="<%=screenId%>"/>
                        <input type="hidden" id="tableId" name="tableId" value="<%=tableId%>"/>
                        <input type="hidden" id="display" name="display" value="yes"/>
                        <input type="hidden" id="position" name="position" value="<%=position%>"/>
                        <tr>
                            <td>Data source</td>
                            <td><input id="dataType" name="dataType" maxlength="20"/></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td colspan="2" id="msgDataType" align="left"></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <button type="button" onclick="getTranslation(1);">English</button>
                                <button type="button" onclick="getTranslation(2);">Hebrew</button>
                                <button type="button" onclick="getTranslation(3);">Russian</button>
                                <button type="button" onclick="getTranslation(4);">Chinese</button>
                                <button type="button" onclick="getTranslation(5);">French</button>
                            </td>
                        </tr>
                        <tr>
                            <td>New Label</td>
                            <td>
                                <input type="text" id="Nlabel" name="Nlabel">
                                <input type="checkbox" name="chbox" align="left" onclick="check();">Add Label
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td colspan="2" id="msgNewDataLabel" align="left"></td>
                        </tr>
                        <tr>
                            <td>Choose Language</td>
                            <td colspan="3">
                                <select id="langListBox" name="langListBox"
                                        style="width:auto;height:25px;font-size:medium;" disabled>
                                    <c:forEach var="l" items="${languages}" varStatus="status">
                                        <option value="${l.id}">${l.language}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td colspan="2">
                                <button id="btnAdd" name="btnAdd" type="submit">
                                    <%=session.getAttribute("button.ok") %>
                                </button>
                                <button id="btnBack" name="btnBack" type="button" onclick='self.close();'>
                                    <%=session.getAttribute("button.cancel") %>
                                </button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="addMulti" style="display:none;">
                <form id="addMultiForm" name="addMultiForm" action="./adddataset.html" method="post"
                      onsubmit="return validateMulti();">
                    <table border="0">
                        <input type="hidden" id="programId" name="programId" value="<%=programId%>">
                        <input type="hidden" id="screenId" name="screenId" value="<%=screenId%>">
                        <input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
                        <input type="hidden" id="display" name="display" value="yes">
                        <input type="hidden" id="position" name="position" value="<%=position%>">
                        <tr>
                            <td>Start Data ID</td>
                            <td><input id="startdataId" type="text" name="startdataId" maxlength="6"></td>
                            <td id="msgStartDataID"></td>
                        </tr>
                        <tr>
                            <td>End Data ID</td>
                            <td><input id="enddataId" type="text" name="enddataId" maxlength="6"></td>
                            <td id="msgEndDataID"></td>
                        </tr>
                        <tr>
                            <td>Table ID</td>
                            <td><input id="tableId" type="text" name="tableId" maxlength="6" value="<%=tableId%>"></td>
                        </tr>
                        <tr>
                            <td>Screen ID</td>
                            <td><input id="screenId" type="text" name="screenId" maxlength="6" value="<%=screenId%>">
                            </td>
                        </tr>
                        <tr>
                            <td>Program ID</td>
                            <td><input id="programId" type="text" name="programId" maxlength="6" value="<%=programId%>">
                            </td>
                        </tr>
                        <tr>
                            <td>Show</td>
                            <td><input id="show" type="text" name="show" maxlength="6" value="yes"></td>
                        </tr>
                        <tr>
                            <td>Start Position</td>
                            <td><input id="startpos" type="text" name="startpos" maxlength="6"></td>
                            <td id="msgStartpos"></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <button id="btnAdd" name="btnAdd" type="submit"><%=session.getAttribute("button.ok") %>
                                </button>
                                <button id="btnBack" name="btnBack" type="button"
                                        onclick='self.close();'><%=session.getAttribute("button.cancel") %>
                                </button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <form name="loadForm">
                <span id="loading" name="loading"></span>
            </form>
        </td>
    </tr>
</table>
</body>
</html>
