<%--
    Document   : all-relays
    Created on : Oct 16, 2011, 3:05:31 PM
    Author     : Administrator
--%>

<%@page import="com.agrologic.app.model.LanguageDto"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.RelayDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramRelayDto"/>

<%  UserDto user = (UserDto) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    List<LanguageDto> languages = (List<LanguageDto>) request.getSession().getAttribute("languages");
    List<RelayDto> relayNames = (List<RelayDto>) request.getSession().getAttribute("relayNames");

    String translateLangStr = (String) request.getParameter("translateLang");
    if (translateLangStr == null) {
        translateLangStr = "1";
    }
    Long translateLang = Long.parseLong(translateLangStr);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title>Relay List</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script type="text/javascript" src="js/fglobal.js"></script>
        <script type="text/javascript" src="js/ftabs.js"></script>
        <script type="text/javascript" src="js/util.js"></script>
        <script type="text/javascript">
            function save() {
                var datas = document.addForm.dataid;
                var bits  = document.addForm.bits;
                var relaynames = document.addForm.relaynames;
                var dataMap = new Hashtable();
                var k = 0 ;
                for( var i = 0; i < datas.length; i++) {
                    var bitMap = new Hashtable();
                    var datavalue = datas[i].value;
                    for( var j = k; j < (k+16); j++) {
                        var bitvalue = bits[j].value;
                        var selected = relaynames[j].selectedIndex;
                        var relayText = relaynames[j].options[selected].innerHTML+"-"+selected;
                        bitMap.put(bitvalue,relayText);
                    }
                    dataMap.put(datavalue,bitMap);
                    k+=16;
                }
                document.getElementById("datamap").value=dataMap;
            }
            function back(link){
                window.document.location.replace(link);
                return false;
            }
            function filterLanguages()
            {
                var langId = document.formRelays.Lang_Filter.value;
                window.document.location.replace("./all-relays.html?translateLang=" + langId);
                return false;
            }
        </script>
    </head>
    <body>
        <div id="header">
            <%@include file="usermenuontop.jsp"%>
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
                        <%@include file="messages.jsp" %>
                    </td>
                </tr>
                <tr>
                    <td>
                        <button style="float:left" id="btnCancel" name="btnCancel" onclick='return back("./all-programs.html");'>
                            <%=session.getAttribute("button.back") %></button>
                        <button id="btnAdd" name="btnAdd" onclick="window.open('./add-relay.jsp?translateLang=<%=translateLang %>&relayId=<%=relayNames.size() %>','mywindow','status=yes, resize=yes, width=250,height=180,left=350,top=400,screenX=100,screenY=100');">
                            <%=session.getAttribute("button.add") %></button>
                    </td>
                </tr>
                <tr>
                    <td>
                        <form id="formRelays" name="formRelays">
                            <div style="height:420px; overflow: auto;">
                                <table class="table-list" border="1" cellpadding="1" cellspacing="0" style="behavior:url(tablehl.htc) url(sort.htc);">
                                    <input type="hidden" id="programId" name="translateLang" value="<%=translateLang %>">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Text</th>
                                            <th>Translate
                                            <select id="Lang_Filter" name="Lang_Filter"  onchange="return filterLanguages();">
                                                <%for (LanguageDto l : languages) {%>
                                                <option value="<%=l.getId()%>"><%=l.getLanguage()%></option>
                                                <%}%>
                                            </select>
                                            </th>
                                            <th colspan="2">
                                                Action
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (RelayDto relayName : relayNames) {%>
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
                                                <img src="img/edit.gif">
                                                <a href="#" onclick="window.open('edit-relay.jsp?translateLang=<%=translateLang%>&relayName=<%=relayName.getText()%>&relayId=<%=relayName.getId()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');">Edit
                                            </td>
                                            <td>
                                                <img src="img/del.gif">
                                                <a href="./remove-relay.html?translateLang=<%=translateLang %>&relayId=<%=relayName.getId() %>">Remove
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
                        <button style="float:left" id="btnCancel" name="btnCancel" onclick='return back("./all-programs.html");'>
                            <%=session.getAttribute("button.back") %></button>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">
                var length = document.formRelays.Lang_Filter.options.length;
                for(var i = 0; i < length; i++) {
                    var translateLang = parseInt(<%=request.getParameter("translateLang")%>);
                    if(document.formRelays.Lang_Filter.options[i].value == translateLang) {
                        document.formRelays.Lang_Filter.selectedIndex=i;
                        break;
                    }
                }
            </script>
        </div>

    </body>
</html>
