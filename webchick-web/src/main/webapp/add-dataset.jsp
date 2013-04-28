<%--
    Document   : add-dataset
    Created on : Dec 18, 2011, 1:06:18 PM
    Author     : Administrator
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.List"%>

<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.LanguageDto"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.DataDaoImpl"/>
<jsp:directive.page import="com.agrologic.app.dao.DataDao"/>
<jsp:directive.page import="com.agrologic.app.dao.LanguageDao"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.LanguageDaoImpl"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long tableId = Long.parseLong(request.getParameter("tableId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    Long programId = Long.parseLong(request.getParameter("programId"));
    Integer position =  Integer.parseInt(request.getParameter("position"));

    /*
        DataDao dataDao = new DataDaoImpl();
        List<DataDto> dataList = dataDao.getAll();
        LanguageDao languageDao = new LanguageDaoImpl();
        List<LanguageDto> langList = languageDao.geAll();
    */
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <title>Add New Data Set</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <style type="text/css">
        span.growday  {
                font-weight: bold;
                font-size: small;
                color:#000000;
        }
        </style>
        <script type="text/javascript">
        function setDataSource() {
           var dataType = document.addForm.dataListBox.value;
           document.addForm.Ndatatype.value = dataType;
           return false;
        }
        function reset() {
            document.getElementById("msgStartDataID").innerHTML="";
            document.getElementById("msgEndDataID").innerHTML="";
            document.getElementById("msgStartpos").innerHTML="";
        }
        function validate() {
            var valid = true;
            reset();

            if (document.addForm.startdataId.value == "") {
                document.getElementById("msgStartDataID").innerHTML="&nbsp;Start DataID can't be empty";
                document.getElementById("msgStartDataID").style.color="RED";
                document.addForm.startdataId.focus();
                valid = false;
            }
            if (document.addForm.enddataId.value == "") {
                document.getElementById("msgEndDataID").innerHTML="&nbsp;End DataID can't be empty";
                document.getElementById("msgEndDataID").style.color="RED";
                document.addForm.enddataId.focus();
                valid = false;
            }
            if (document.addForm.startpos.value == "") {
                document.getElementById("msgStartpos").innerHTML="&nbsp;Start position can't be empty";
                document.getElementById("msgStartpos").style.color="RED";
                document.addForm.startpos.focus();
                valid = false;
            }
            if(!valid) {
                return false;
            }
            document.getElementById("loading").innerHTML="adding data set .....";
            document.getElementById("loading").style.color="GREEN";
        }

        function search() {
            var length = document.addForm.dataListBox.options.length;
            var num  = document.addForm.Ndatatype.value;
            var exist = 0;
            for(var i = 0; i < length; i++) {
                if(document.addForm.dataListBox.options[i].value == num) {
                    document.addForm.dataListBox.selectedIndex = i;
                    exist = 1;
                }
            }
            if(exist == 0) {
                document.getElementById("msgDataType").innerHTML="Data Source Does Not Exist ";
                document.getElementById("msgDataType").style.color="RED";
                document.addForm.Ndatatype.focus();
            } else {
                document.getElementById("msgDataType").innerHTML="";
                document.getElementById("msgDataType").style.color="BLACK";
                document.addForm.Ndatatype.focus();
            }
        }
        function closeWindow() {
            self.close();
            window.opener.location.reload(true);
        }
        function check() {
            if(document.addForm.chbox.checked == true) {
                document.addForm.langListBox.disabled = false;
            } else {
                document.addForm.langListBox.disabled = true;
            }
        }
        function encode(txtObj){
            var source = txtObj.value;
            var result = '';
            for (i=0; i < source.length; i++)
                result += '&#' + source.charCodeAt(i) + ';';
            return result;
        }

        function showProgress() {
            var start = document.getElementById("startdataId").value;
            var end = document.getElementById("enddataId").value;
            var dif = end-start;
            wait(100*dif);
            closeWindow();
        }
        function wait(msecs) {
            document.getElementById("loading").innerHTML="adding data set .";
            var text = document.getElementById("loading").innerHTML;
            var start = new Date().getTime();
            var cur = start
            while(cur - start < msecs)
            {
                document.getElementById("loading").innerHTML=text+".";
                document.getElementById("loading").style.color="GREEN";
                cur = new Date().getTime();
                text = document.getElementById("loading").innerHTML;
            }
        }
        </script>
    </head>
    <body onunload="showProgress();">
      <table class ="main" align="center" cellpadding="0" cellspacing="0" border="0" width="100%" style="padding:10px">
        <tr>
          <td>
            <h1>Add Data Set</h1>
            <h2>add data set to screen table </h2>
            <form id="addForm" name="addForm" action="./adddataset.html" method="post"  onsubmit="return validate();">
            <table border="0">
                <input type="hidden" id="programId" name="programId" value="<%=programId%>">
                <input type="hidden" id="screenId" name="screenId" value="<%=screenId%>">
                <input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
                <input type="hidden" id="display" name="display" value="yes">
                <input type="hidden" id="position" name="position" value="<%=position%>">
                <tr>
                    <td>Start Data ID </td>
                    <td><input id="startdataId" type="text" name="startdataId" maxlength="6"></td>
                    <td id="msgStartDataID"></td>
                </tr>
                <tr>
                    <td>End Data ID </td>
                    <td><input id="enddataId" type="text" name="enddataId" maxlength="6"></td>
                    <td id="msgEndDataID"></td>
                </tr>
                <tr>
                    <td>Table ID </td>
                    <td><input id="tableId" type="text" name="tableId" maxlength="6" value="<%=tableId%>"></td>
                </tr>
                <tr>
                    <td>Screen ID </td>
                    <td><input id="screenId" type="text" name="screenId" maxlength="6" value="<%=screenId%>"></td>
                </tr>
                <tr>
                    <td>Program ID </td>
                    <td><input id="programId" type="text" name="programId" maxlength="6" value="<%=programId%>"></td>
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
                        <button  id="btnAdd" name="btnAdd" type="submit"><%=session.getAttribute("button.ok") %></button>
                        <button  id="btnBack" name="btnBack" type="button" onclick='self.close();'><%=session.getAttribute("button.cancel") %></button>
                    </td>
                </tr>
            </table>
            </form>
          </td>
        </tr>
        <tr>
            <td>
                <from name="loadForm">
                    <span id="loading" name="loading"></span>
                </form>
            </td>
        </tr>
      </table>
    </body>
</html>
