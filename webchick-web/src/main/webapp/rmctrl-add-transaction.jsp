<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.TransactionDao" %>
<%@ page import="com.agrologic.app.model.Transaction" %>
<%--
    Document   : rmctrl-add-transaction
    Created on : Apr 20, 2011, 4:36:36 PM
    Author     : JanL
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="language.jsp" %>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    TransactionDao transactDao = DbImplDecider.use(DaoType.MYSQL).getDao(TransactionDao.class);
    Collection<Transaction> transactList = transactDao.getAll();
%>
<!DOCTYPE html>

<html dir="<%=session.getAttribute("dir")%>">
<head>

    <link rel="StyleSheet" type="text/css" href="resources/custom/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/custom/style/calendar.css"/>
    <script type="text/javascript" src="resources/custom/javascript/calendar.js">;</script>
    <script type="text/javascript">
        function validate() {
            var name = document.getElementById('name').value;
            var expn = document.getElementById('expenses').value;
            var rvns = document.getElementById('revenues').value;

            if (name == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (expn == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (rvns == "") {
                document.getElementById('msg').style.display = 'block';
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
            OVERFLOW: auto;
            WIDTH: 650px;
            POSITION: relative;
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
            TOP: expression(this.offsetParent.clientHeight - this.offsetParent.scrollHeight + this.offsetParent.scrollTop);
            HEIGHT: 20px;
            TEXT-ALIGN: left;
            text-wrap: suppress
        }
    </style>
    <title>Add Transaction</title>
</head>
<body>
<h1>Add Transaction</h1>
<br>
<button type="button" onclick='javascript:closePopup()'>Close</button>
<table id="msg" class="errMsg" align="center" style="display: none;">
    <tr>
        <td>Fields can't be empty</td>
    </tr>
</table>
<form id="addform" name="addform" action="./add-transact.html" method="post">
    <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" id="controllerId" name="controllerId" value="<%=controllerId%>">
    <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">

    <div class="tableHolder" style="width: 650px; height: 200px; padding-left:5px;padding-right:10px;">
        <table border="1" style="width: 650px;border:1px solid #C6C6C6; border-collapse: collapse;">
            <thead>
            <th align="center"> Name</th>
            <th align="center"> Expenses</th>
            <th align="center"> Revenues</th>
            <th align="center" width="100px">Action</th>
            </thead>
            <tr>
                <td><input type="text" id="name" name="name"></td>
                <td><input type="text" id="expenses" name="expenses"></td>
                <td><input type="text" id="revenues" name="revenues"></td>
                <td align="center">
                    <img src="resources/custom/images/plus1.gif" border="0" hspace="4"><a href="javascript:validate();">Add</a>
                </td>
            </tr>
            <% for (Transaction t : transactList) {%>
            <tr>
                <td><%=t.getName() %>
                </td>
                <td><%=t.getExpenses() %>
                </td>
                <td><%=t.getRevenues() %>
                </td>
                <td align="center"><img src="resources/custom/images/close.png" border="0" hspace="4">
                    <a href="javascript:window.location='./remove-transact.html?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>&transactId=<%=t.getId()%>';">Remove</a>
                </td>
            </tr>
            <%}%>
        </table>
    </div>
</form>
</body>
</html>