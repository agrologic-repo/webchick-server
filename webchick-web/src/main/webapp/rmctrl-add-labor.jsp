<%--
    Document   : rmctrl-add-labor
    Created on : Apr 20, 2011, 12:49:48 PM
    Author     : JanL
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.LaborDto"/>
<jsp:directive.page import="com.agrologic.app.dao.LaborDao"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.LaborDaoImpl"/>

<jsp:directive.page import="com.agrologic.app.model.WorkerDto"/>
<jsp:directive.page import="com.agrologic.app.dao.WorkerDao"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.WorkerDaoImpl"/>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    LaborDao laborDao = new LaborDaoImpl();
    List<LaborDto> laborList = laborDao.getAllByFlockId(flockId);

    WorkerDao workerDao = new WorkerDaoImpl();
    List<WorkerDto> workerList = workerDao.getAllByCellinkId(cellinkId);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <link rel="stylesheet" type="text/css" href="css/calendar.css"/>
        <script type="text/javascript" src="js/calendar.js"></script>
        <script type="text/javascript">
            function validate() {
                var strd  = document.getElementById('startDate').value;
                var hours = document.getElementById('hours').value;
                var date  = document.getElementById('startDate').value;

                if(strd == "") {
                    document.getElementById('msg').style.display='block';
                    return;
                }
                if(hours == ""){
                    document.getElementById('msg').style.display='block';
                    return;
                }
                if(date == "") {
                    document.getElementById('msg').style.display='block';
                    return;
                }
                document.addform.submit();

            }
            function closePopup() {
                window.close();
                if (window.opener && !window.opener.closed) {
                    window.opener.location.reload();
                }
            }
        </script>
        <style type="text/css">
            div.tableHolder {
                OVERFLOW:auto;
                WIDTH: 550px;
                POSITION:relative;
                HEIGHT: 200px;
            }
            thead th {
                Z-INDEX: 20;
                HEIGHT: 20px;
                POSITION: relative;
                TOP: expression(this.offsetParent.scrollTop-2);
                TEXT-ALIGN: center
            }
            tfoot td {
                Z-INDEX: 20;
                HEIGHT: 20px;
                POSITION: relative;
                TOP: expression(this.offsetParent.clientHeight - this.offsetParent.scrollHeight + this.offsetParent.scrollTop); HEIGHT: 20px;
                TEXT-ALIGN: left;
                text-wrap: suppress
            }
        </style>
        <title>Add Labor</title>
    </head>
    <body>
        <h1>Add Labor</h1>
        <br>
        <button type="button" onclick='javascript:closePopup()'>Close</button>
        <button type="button" onclick="window.location='./rmctrl-add-worker.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>'">Add Worker</button>
        <table id="msg" class="errMsg" align="center" style="display: none;">
            <tr>
                <td>Fields can't be empty</td>
            </tr>
        </table>
        <form id="addform" name="addform" action="./add-labor.html" method="post">
            <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
            <input type="hidden" id="controllerId" name="controllerId" value="<%=controllerId%>">
            <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">
            <div class="tableHolder" style="width: 550px; height: 200px; padding-left:5px;padding-right:10px;">
                <table border="1" style="width: 550px;border:1px solid #C6C6C6; border-collapse: collapse;">
                    <thead>
                    <th align="center">Name</th>
                    <th align="center">Date</th>
                    <th align="center">Hours</th>
                    <th align="center">Salary</th>
                    <th align="center" width="100px">Action</th>
                    </thead>
                    <tr>
                        <td>
                            <select id="workersids" name="workersids">
                                <option value="0" select></option>
                                <% for (WorkerDto w:workerList) {%>
                                    <option value="<%=w.getId() %>"><%=w.getName() %></option>
                                <%}%>
                            </select>
                        </td>
                        <td><input type="text" id="startDate" name="startDate" size="10" readonly>
                            <img src="img/calendar.png" border="0" onclick="GetDate('start');"/></td>
                        <td><input type="text" id="hours" name="hours" value="" size="10"></td>
                        <td><input type="text" id="salary" name="salary" readonly value="" size="10"></td>
                        <td align="center"><img src="img/plus1.gif" border="0" hspace="4">
                            <a href="javascript:validate();">Add</a>
                        </td>
                    </tr>
                    <%String name = "";%>
                    <%  for(LaborDto l:laborList) {%>
                        <tr>
                            <td>
                                <%for(WorkerDto w:workerList) {%>
                                    <%if(w.getId().equals(l.getWorkerId())) {
                                        name = w.getName();
                                    %>
                                    <%}%>
                                <%}%>
                            <%=name%>
                            </td>
                            <td><%=l.getDate() %></td>
                            <td><%=l.getHours() %></td>
                            <td><%=l.getSalary() %></td>
                            <td align="center"><img src="img/close.png" border="0" hspace="4">
                                <a href="javascript:window.location='./remove-labor.html?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>&laborId=<%=l.getId()%>';">Remove</a>
                            </td>
                        </tr>
                    <%}%>
                </table>
            </div>
        </form>
    </body>
</html>