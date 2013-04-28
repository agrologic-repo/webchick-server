<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.CellinkDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    List<UserDto> users = (List<UserDto>)request.getSession().getAttribute("users");
    Long userId = Long.parseLong(request.getParameter("userId"));
    UserDto editUser = findUserToEdit(users, userId);
    List<CellinkDto> cellinks = editUser.getCellinks();
%>
<%! UserDto findUserToEdit(List<UserDto> users,Long userId) {
        for(UserDto u:users) {
            if(u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Add Cellink</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
        <link rel="SHORTCUT ICON" HREF="img/favicon5.ico">
        <link rel="StyleSheet" type="text/css" href="css/menubar.css">
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
        <script type="text/javascript">
        function reset() {
            document.getElementById("msgCellinkName").innerHTML="";
            document.getElementById("msgPassword").innerHTML="";
        }
        function validate() {
            var valid = true;
            reset();
            if (document.addForm.Ncellinkname.value == "") {
                    document.getElementById("msgCellinkName").innerHTML="&nbsp;Cellink name can't be empty";
                    document.getElementById("msgCellinkName").style.color="RED";
                    document.addForm.Ncellinkname.focus();
                    valid = false;
            }

            if (document.addForm.Npassword.value == "") {
                    document.getElementById("msgPassword").innerHTML="&nbsp;Password can't be empty";
                    document.getElementById("msgPassword").style.color="RED";
                    document.addForm.Npassword.focus();
                    valid = false;
            }

            if(!valid) {
                return false;
            }
        }
        function back(link) {
            window.document.location.replace(link);
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
         <td valign="top" height="648px">
           <form id="addForm" name="addForm" action="./addcellink.html?userId=<%=editUser.getId()%>" method="post">
           <table border=0 cellPadding=1 cellSpacing=1>
             <tr>
               <td>
                 <h1>Add Cellink</h1>
               </td>
             </tr>
            <tr>
                <td>
                    <h2>add cellink </h2>
                    <div><p style="color:red;">Boxes with an asterisk next to them are required</p></div>
                    <table>
                        <input id="Nuserid" readonly type="hidden" name="Nuserid" class=rightTitles value="<%=editUser.getId()%>">
                        <tr>
                            <td class="">Cellink name * </td>
                            <td><input id="Ncellinkname" type="text" name="Ncellinkname" style="width:100px"></td>
                            <td id="msgCellinkName"></td>
                        </tr>
                        <tr>
                        <td class="">Password * </td>
                            <td><input id="Npassword" type="text" name="Npassword" style="width:100px"></td>
                            <td id="msgPassword"></td>
                        </tr>
                        <tr>
                            <td class="">SIM Number</td>
                            <td><input id="Nsim" type="text" name="Nsim" style="width:100px"></td>
                        </tr>
                        <tr>
                            <td class="">Type</td>
                            <td>
                            <select id="Ntype" name="Ntype" style="width:120px" >
                                <option value="" selected></option>
                                <% for(String type:CellinkDto.getTypeList()) {%>
                                <option value="<%=type %>"><%=type %></option>
                                <%}%>
                            </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="">Version</td>
                            <td><input id="Nversion" type="text" name="Nversion" style="width:100px"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                  <button id="btnBack" name="btnBack" onclick='return back("./userinfo.html?userId=<%=editUser.getId() %>");'>
                      <%=session.getAttribute("button.cancel") %></button>
                  <button id="btnAdd" type="submit" name="btnAdd" onclick="return validate();">
                      <%=session.getAttribute("button.ok") %></button>
                </td>
                <td>&nbsp;</td>
              </tr>
            </table>
            </form>
          </td>
        </tr>
    </table>
    </div>
  </body>
</html>