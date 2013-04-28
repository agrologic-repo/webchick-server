<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<%@ page import="java.util.List"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    List<ProgramDto> programs = (List<ProgramDto>)request.getSession().getAttribute("allprograms");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Add program</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script type="text/javascript">
        function reset() {
            document.getElementById("msgProgramId").innerHTML="";
            document.getElementById("msgName").innerHTML="";
        }
        function validate()
        {
            var valid = true;
            reset();
            if (document.addForm.Nprogramid.value == "")
            {
                    document.getElementById("msgProgramId").innerHTML="&nbsp;Program ID can't be empty";
                    document.getElementById("msgProgramId").style.color="RED";
                    document.addForm.Nprogramid.focus();
                    valid = false;
            }

            if (document.addForm.Nname.value == "")
            {
                    document.getElementById("msgName").innerHTML="&nbsp;Program name can't be empty";
                    document.getElementById("msgName").style.color="RED";
                    document.addForm.Nname.focus();
                    valid = false;
            }


            if (!IsNumeric(document.addForm.Nprogramid.value)) {
                    document.getElementById("msgProgramId").innerHTML="&nbsp;Invalid value in program id ";
                    document.getElementById("msgProgramId").style.color="RED";
                    document.addForm.Nprogramid.focus();
                    valid = false;
            }

            if(!valid) {
                return false;
            }
        }
        function IsNumeric(sText)
        {
           var ValidChars = "0123456789.";
           var IsNumber=true;
           var Char;

           for (i = 0; i < sText.length && IsNumber == true; i++)
              {
              Char = sText.charAt(i);
              if (ValidChars.indexOf(Char) == -1)
                 {
                 IsNumber = false;
                 }
              }
           return IsNumber;

        }
        function back(link) {
            window.document.location.replace(link);
            return false;
        }
        var req;
        function validateProgId() {
            var programId  = document.getElementById("Nprogramid");
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
            <%@include file="usermenuontop.jsp"%>
        </div>
        <div id="main-shell">
      <table border="0" cellPadding=1 cellSpacing=1 width="100%">
      <tr>
        <td valign="top" height="648px">
          <table border=0 cellPadding=1 cellSpacing=1 width="736">
            <tr>
              <td width="483">
                <p><h1>Add Program</h1></p>
              </td>
            </tr>
            <tr>
              <td>
                <p><h2>add new program</h2>
                <div><p style="color:red;">Boxes with an asterisk next to them are required</div>
                <form id="addForm" name="addForm" action="./addprogram.html" method="post">
                  <table width="auto" align="left">
                    <tr>
                        <td align="left">Select Program *</td>
                        <td colspan="2">
                            <select id="Selectedprogramid" name="Selectedprogramid" style="width:120px;">
                                <%for(ProgramDto program : programs){ %>
                                    <option value="<%=program.getId()%>"><%=program.getName()%></option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">Program ID * </td>
                        <td><input id="Nprogramid" type="text" name="Nprogramid" style="width:120px"  onchange="validateProgId();"></td>
                        <td id="msgProgramId"></td>
                    </tr>
                    <tr>
                        <td align="left">Program name * </td>
                        <td><input id="Nname" type="text" name="Nname" style="width:120px"></td>
                        <td id="msgName"></td>
                    </tr>
                    <tr>
                        <td>
                            <button id="btnCancel" name="btnCancel" onclick='return back("./all-programs.jsp");'><%=session.getAttribute("button.cancel") %></button>
                            <button id="btnAdd" type="submit" name="btnAdd" onclick="return validate();"><%=session.getAttribute("button.ok") %></button>
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
