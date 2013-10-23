<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>

<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.model.Language" %>
<%@ page import="com.agrologic.app.model.Relay" %>
<%@ page import="com.agrologic.app.model.User" %>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Collection<Language> languages = (Collection<Language>) request.getAttribute("languages");
    Collection<Relay> relayNames = (Collection<Relay>) request.getAttribute("relayNames");

    String translateLangStr = request.getParameter("translateLang");
    if (translateLangStr == null) {
        translateLangStr = "1";
    }
    Long translateLang = Long.parseLong(translateLangStr);
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title>Relay Collection</title>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/menubar.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>
    <script type="text/javascript" src="resources/custom/javascript/general.js">;</script>
    <script type="text/javascript" src="resources/custom/javascript/util.js">;</script>
    <script type="text/javascript">
        function save() {
            var datas = document.addForm.dataid;
            var bits = document.addForm.bits;
            var relaynames = document.addForm.relaynames;
            var dataMap = new Hashtable();
            var k = 0;
            for (var i = 0; i < datas.length; i++) {
                var bitMap = new Hashtable();
                var datavalue = datas[i].value;
                for (var j = k; j < (k + 16); j++) {
                    var bitvalue = bits[j].value;
                    var selected = relaynames[j].selectedIndex;
                    var relayText = relaynames[j].options[selected].innerHTML + "-" + selected;
                    bitMap.put(bitvalue, relayText);
                }
                dataMap.put(datavalue, bitMap);
                k += 16;
            }
            document.getElementById("datamap").value = dataMap;
        }
        function filterLanguages() {
            var langId = document.formRelays.Lang_Filter.value;
            redirect("./all-relays.html?translateLang=" + langId);
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
                <h1>All Relays</h1>
                <h2>relay list</h2>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <jsp:include page="messages.jsp"/>
            </td>
        </tr>
        <tr>
            <td>
                <button style="float:left" id="btnCancelTop" name="btnCancelTop" onclick="redirect('./all-programs.html');">
                    <%=session.getAttribute("button.back") %>
                </button>
                <button id="btnAdd" name="btnAdd"
                        onclick="window.open('./add-relay.jsp?translateLang=<%=translateLang %>&relayId=<%=relayNames.size() %>','mywindow','status=yes, resize=yes, width=250,height=180,left=350,top=400,screenX=100,screenY=100');">
                    <%=session.getAttribute("button.add") %>
                </button>
            </td>
        </tr>
        <tr>
            <td>
                <form id="formRelays" name="formRelays">
                    <div style="height:420px; overflow: auto;">
                        <table class="table-list" border="1" cellpadding="1" cellspacing="0"
                               >
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
                            <% for (Relay relayName : relayNames) {%>
                            <tr>
                                <td>
                                    <%=relayName.getId()%>
                                </td>
                                <td>
                                    <%=relayName.getText()%>
                                </td>
                                <td ondblclick="window.open('add-relaytranslate.jsp?translateLang=<%=translateLang%>&relayName=<%=relayName.getText()%>&relayId=<%=relayName.getId()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');"><%=relayName.getUnicodeText()%>
                                                <span class="comment">
                                                    double click to edit
                                                </span>
                                </td>
                                <td>
                                    <img src="resources/custom/images/edit.gif">
                                    <a href="#"
                                       onclick="window.open('edit-relay.jsp?translateLang=<%=translateLang%>&relayName=<%=relayName.getText()%>&relayId=<%=relayName.getId()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');">Edit</a>
                                </td>
                                <td>
                                    <img src="resources/custom/images/delete.gif">
                                    <a href="./remove-relay.html?translateLang=<%=translateLang %>&relayId=<%=relayName.getId() %>">Remove</a>
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
        var length = document.formRelays.Lang_Filter.options.length;
        for (var i = 0; i < length; i++) {
            var translateLang = parseInt(<%=request.getParameter("translateLang")%>);
            if (document.formRelays.Lang_Filter.options[i].value == translateLang) {
                document.formRelays.Lang_Filter.selectedIndex = i;
                break;
            }
        }
    </script>
</div>
</body>
</html>
