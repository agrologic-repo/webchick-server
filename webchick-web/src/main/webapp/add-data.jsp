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

    DataDao dataDao = new DataDaoImpl();
    List<DataDto> dataList = dataDao.getAll();
    LanguageDao languageDao = new LanguageDaoImpl();
    List<LanguageDto> langList = languageDao.geAll();
%>

<%!
    DataDto searchData(List<DataDto> dataList, long id) {

        for (DataDto d:dataList) {
            if(d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <title>Add New Data</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script type="text/javascript">
        var actionName = ''
        function optionToAddScreen(divToShow) {
            if(divToShow == "one"){
                    document.getElementById('addOne').style.display = "inline";
                    document.getElementById('addMulti').style.display = "none";
            }
            if(divToShow == "mul") {
                    document.getElementById('addOne').style.display = "none";
                    document.getElementById('addMulti').style.display = "inline";
            }
        }
        function setDataSource() {
           var dataType = document.addForm.dataListBox.value;
           document.addForm.Ndatatype.value = dataType;
           return false;
        }

        function translateData(langid) {

            var dataId = document.addForm.Ndatatype.value
            if(dataId =="") {
                return;
            }
            var url = "translate-data?dataId=" + encodeURIComponent(dataId) + "&langId="+langid;
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
                alert(msg);
            }
        }

        function resetMulti() {
            document.getElementById("msgStartDataID").innerHTML="";
            document.getElementById("msgEndDataID").innerHTML="";
            document.getElementById("msgStartpos").innerHTML="";
        }
        function validateMulti() {
            var valid = true;
            resetMulti();

            if (document.addMultiForm.startdataId.value == "") {
                document.getElementById("msgStartDataID").innerHTML="&nbsp;Start DataID can't be empty";
                document.getElementById("msgStartDataID").style.color="RED";
                document.addMultiForm.startdataId.focus();
                valid = false;
            }
            if (document.addMultiForm.enddataId.value == "") {
                document.getElementById("msgEndDataID").innerHTML="&nbsp;End DataID can't be empty";
                document.getElementById("msgEndDataID").style.color="RED";
                document.addMultiForm.enddataId.focus();
                valid = false;
            }
            if (document.addMultiForm.startpos.value == "") {
                document.getElementById("msgStartpos").innerHTML="&nbsp;Start position can't be empty";
                document.getElementById("msgStartpos").style.color="RED";
                document.addMultiForm.startpos.focus();
                valid = false;
            }
            if(!valid) {
                return false;
            }
            actionName = "addMultiForm";
            document.getElementById("loading").innerHTML="adding data set .....";
            document.getElementById("loading").style.color="GREEN";
        }
        function reset() {
            document.getElementById("msgDataType").innerHTML="";
            document.getElementById("msgNewDataLabel").innerHTML="";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ndatatype.value == "") {
                document.getElementById("msgDataType").innerHTML="&nbsp;Data Source can't be empty";
                document.getElementById("msgDataType").style.color="RED";
                document.addForm.Ndatatype.focus();
                valid = false;
            }
            if(document.addForm.chbox.checked == true) {
                if (document.addForm.Nlabel.value == "") {
                    document.getElementById("msgNewDataLabel").innerHTML="&nbsp;New Label can't be empty";
                    document.getElementById("msgNewDataLabel").style.color="RED";
                    document.addForm.Nlabel.focus();
                    valid = false;
                } else {
                    document.addForm.Nlabel.value = encode(document.addForm.Nlabel);
                }
            }

            if(!valid) {
                return false;
            }
            actionName = "addForm";
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
            if(actionName == "addForm") {
                closeWindow();
            } else {
                var start = document.getElementById("startdataId").value;
                var end = document.getElementById("enddataId").value;
                var dif = end - start;
                wait(100*dif);
                closeWindow();
            }
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
            <h1>Add Data</h1>
            <h2>add data to screen table </h2>
            <br>
            <div><br>
                <input type="radio" name="group1" id="one" checked onclick="optionToAddScreen('one')">&nbsp;Add one data
                <input type="radio" name="group1" id="mul" onclick="optionToAddScreen('mul')">&nbsp;Add data set <br>
            </div>
            <br>
            <div id="addOne" style="display:block;">
            <form id="addForm" name="addForm" action="./addtabledata.html" method="post"  onsubmit="return validate();">
            <table width="100%" align="left" border="0">
                <input type="hidden" id="programId" name="programId" value="<%=programId%>">
                <input type="hidden" id="screenId" name="screenId" value="<%=screenId%>">
                <input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
                <input type="hidden" id="display" name="display" value="yes">
                <input type="hidden" id="position" name="position" value="<%=position%>">
                <tr>
                    <td>Data source </td>
                    <td><input id="Ndatatype" type="text" name="Ndatatype" style="width:80px; height:20px;font-size:medium" maxlength="6">&nbsp;
                        <button name="btnSearch" type="button" onclick="search();">Search</button><!--input type="button"  id="btnSearch" name="btnSearch" onclick='search();' value="Search"-->
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td colspan="2" id="msgDataType" align="left"></td>
                </tr>
                <tr>
                    <td>Choose Data</td>
                    <td colspan="3">
                        <select id="dataListBox" name="dataListBox" style="width:auto;height:25px;font-size:medium; " onchange="return setDataSource();">
                            <%for(DataDto data : dataList){ %>
                                <option value="<%=data.getType()%>"><%=data.getUnicodeLabel()%>&nbsp;(<%=data.getType()%>)</option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <button name="btnTranslate" type="button" onclick="translateData(1);">English</button>
                        <button name="btnTranslate" type="button" onclick="translateData(2);">Hebrew</button>
                        <button name="btnTranslate" type="button" onclick="translateData(3);">Russian</button>
                        <button name="btnTranslate" type="button" onclick="translateData(4);">Chinese</button>
                        <button name="btnTranslate" type="button" onclick="translateData(5);">French</button>
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
                    <td>Choose Language </td>
                    <td colspan="3">
                        <select id="langListBox" name="langListBox" style="width:auto;height:25px;font-size:medium;" disabled>
                            <%for(LanguageDto l : langList){ %>
                                <option value="<%=l.getId()%>"><%=l.getLanguage()%></option>
                            <%}%>
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
                        <button  id="btnAdd" name="btnAdd" type="submit">
                            <%=session.getAttribute("button.ok") %></button>
                        <button  id="btnBack" name="btnBack" type="button" onclick='self.close();'>
                            <%=session.getAttribute("button.cancel") %></button>
                    </td>
                </tr>
            </table>
            </form>
            </div>
            <div  id="addMulti" style="display:none;">
            <form id="addMultiForm" name="addMultiForm" action="./adddataset.html" method="post"  onsubmit="return validateMulti();">
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
            </div>
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
