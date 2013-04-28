<%@ include file="disableCaching.jsp" %>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.LanguageDto"/>

<jsp:directive.page import="com.agrologic.app.dao.LanguageDao"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.LanguageDaoImpl"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long alarmId = Long.parseLong(request.getParameter("alarmId"));
    String alarmName = request.getParameter("alarmName");
    Long translateLang = Long.parseLong(request.getParameter("translateLang"));
    LanguageDao languageDao = new LanguageDaoImpl();
    LanguageDto langDto = languageDao.getById(translateLang);
%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
        <script language="Javascript">
        function reset() {
            document.getElementById("msgTranslation").innerHTML="";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ntranslate.value == "") {
                    document.getElementById("msgTranslation").innerHTML="&nbsp;Translation can't be empty";
                    document.getElementById("msgTranslation").style.color="RED";
                    document.addForm.Ntranslate.focus();
                    valid = false;
                    alert(valid)
            }
            document.addForm.Ntranslate.value = encode();

            if(!valid) {
                return false;
            }
        }
        function closeWindow() {
                self.close();
                window.opener.location.replace("./all-alarms.html?translateLang=<%=translateLang%>");
        }
        function check() {
            if(document.addForm.chbox.checked == true) {
                document.addForm.langListBox.disabled = false;
            } else {
                document.addForm.langListBox.disabled = true;
            }
        }
        function encode(){
          if(document.addForm.Ntranslate.value != ''){
             var vText = document.addForm.Ntranslate.value;
             var vEncoded = convertToUnicode(vText);
             return vEncoded;
          }
        }
        function convertToUnicode(source) {
          result = '';
          for (i=0; i<source.length; i++)
            result += '&#' + source.charCodeAt(i) + ';';
          return result;
        }
        </script>
        <title>Add Translation</title>
    </head>
    <body onunload="closeWindow();">
      <table class ="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
        <tr>
          <td>
            <h1>Add Translation</h1>
            <p><h2>add alarm translation </h2>
            <form id="addForm" name="addForm" action="./addalarmtranslate.html" method="post" onsubmit="return validate();">
            <table width="100%" align="left" border="0">
                <input type="hidden" id="alarmId" name="alarmId" value="<%=alarmId%>">
                <input type="hidden" id="langId" name="langId" value="<%=langDto.getId() %>">
                <tr>
                    <td align="left">Insert &nbsp;<%=alarmName%> in <%=langDto.getLanguage() %>
                </tr>
                <tr>
                    <td align="left"><input id="Ntranslate" type="text" name="Ntranslate">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2" id="msgTranslation" align="left"></td>
                </tr>
                <tr>
                    <td>
                        <button id="btnAdd" name="btnAdd" type="submit"><img src="img/plus1.gif" >&nbsp;Add</button>
                        <button type="button" onclick='self.close();'><img src="img/close.png" >&nbsp;Close</button>
                    </td>
                </tr>
            </table>
            </form>
        </table>
    </body>
</html>

