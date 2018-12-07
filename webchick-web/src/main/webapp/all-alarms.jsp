<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Alarm" %>
<%@ page import="com.agrologic.app.model.Language" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.util.Collection" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<Language> languages = (Collection<Language>) request.getAttribute("languages");
    Collection<Alarm> alarmNames = (Collection<Alarm>) request.getAttribute("alarmNames");

    String translateLangStr = request.getParameter("translateLang");
    if (translateLangStr == null) {
        translateLangStr = "1";
    }
    Long translateLang = Long.parseLong(translateLangStr);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Alarms Collection</title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <script type="text/javascript" src="resources/javascript/ftabs.js">;</script>
    <script type="text/javascript" src="resources/javascript/util.js">;</script>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function save() {
            var datas = document.addForm.dataid;
            var bits = document.addForm.bits;
            var alarmnames = document.addForm.alarmnames;
            var dataMap = new Hashtable();
            var k = 0;
            for (var i = 0; i < datas.length; i++) {
                var bitMap = new Hashtable();
                var datavalue = datas[i].value;
                for (var j = k; j < (k + 16); j++) {
                    var bitvalue = bits[j].value;
                    var selected = alarmnames[j].selectedIndex;
                    var alarmText = alarmnames[j].options[selected].innerHTML + "-" + selected;
                    bitMap.put(bitvalue, alarmText);
                }
                dataMap.put(datavalue, bitMap);
                k += 16;
            }
            document.getElementById("datamap").value = dataMap;
        }
        function filterLanguages() {
            var langId = document.formAlarms.Lang_Filter.value;
            redirect("./all-alarms.html?translateLang=" + langId);
            return false;
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
            <td>
                <h1>Alarm Collection</h1>

                <h2>alarm list</h2>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <jsp:include page="messages.jsp"/>
            </td>
        </tr>
        <tr>
            <td>
                <button style="float:left" id="btnCancelTop" name="btnCancelTop"
                        onclick='return back("./all-programs.html");'>
                    <%=session.getAttribute("button.back") %>
                </button>
                <button id="btnAdd" name="btnAdd"
                        onclick="window.open('./add-alarm.jsp?translateLang=<%=translateLang %>&alarmId=<%=alarmNames.size()+1 %>','mywindow','status=yes, resize=yes, width=250,height=180,left=350,top=400,screenX=100,screenY=100');">
                    <%=session.getAttribute("button.add") %>
                </button>
            </td>
        </tr>
        <tr>
            <td>
                <form id="formAlarms" name="formAlarms">
                    <div style="height:420px; overflow: auto;">
                        <table class="table-list" border="1" cellpadding="1" cellspacing="0">
                            <input type="hidden" id="programId" name="translateLang" value="<%=translateLang %>">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Text</th>
                                <th>Translate
                                    <select id="Lang_Filter" name="Lang_Filter" onchange="return filterLanguages();">
                                        <%for (Language l : languages) {%>
                                        <option value="<%=l.getId()%>"><%=l.getLanguage()%>
                                        </option>
                                        <%}%>
                                    </select>
                                </th>
                                <th colspan="2">
                                    Action
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (Alarm alarmName : alarmNames) {%>
                            <tr>
                                <td>
                                    <%=alarmName.getId()%>
                                </td>
                                <td>
                                    <%=alarmName.getText()%>
                                </td>
                                <td ondblclick="window.open('add-alarmtranslate.jsp?translateLang=<%=translateLang%>&alarmName=<%=alarmName.getText()%>&alarmId=<%=alarmName.getId()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');"><%=alarmName.getUnicodeText()%>
                                                <span class="comment">
                                                    double click to edit
                                                </span>
                                </td>
                                <td>
                                    <img src="resources/images/edit.gif">
                                    <a href="#"
                                       onclick="window.open('edit-alarm.jsp?translateLang=<%=translateLang%>&alarmName=<%=alarmName.getText()%>&alarmId=<%=alarmName.getId()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');">Edit</a>
                                </td>
                                <td>
                                    <img src="resources/images/delete.gif">
                                    <a href="./remove-alarm.html?translateLang=<%=translateLang %>&alarmId=<%=alarmName.getId() %>">Remove</a>
                                </td>
                            </tr>
                            <%}%>
                            </tbody>
                        </table>
                    </div>
                </form>
            </td>
        </tr>
        <tr>
            <td>
                <button style="float:left" id="btnCancel" name="btnCancel"
                        onclick='return back("./all-programs.html");'>
                    <%=session.getAttribute("button.back") %>
                </button>
            </td>
        </tr>
    </table>
    <script type="text/javascript">
        var length = document.formAlarms.Lang_Filter.options.length;
        for (var i = 0; i < length; i++) {
            var translateLang = parseInt(<%=request.getParameter("translateLang")%>);
            if (document.formAlarms.Lang_Filter.options[i].value == translateLang) {
                document.formAlarms.Lang_Filter.selectedIndex = i;
                break;
            }
        }
    </script>
</div>

</body>
</html>
