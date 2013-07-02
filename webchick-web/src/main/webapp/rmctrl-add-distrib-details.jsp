<%--
    Document   : rmctrl-add-distrib-details
    Created on : May 24, 2011, 12:10:30 PM
    Author     : JanL
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.dao.DistribDao"/>
<jsp:directive.page import="com.agrologic.app.dao.impl.DistribDaoImpl"/>
<jsp:directive.page import="com.agrologic.app.model.DistribDto"/>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    DistribDao distribDao = new DistribDaoImpl();
    Collection<DistribDto> distribList = distribDao.getAllByFlockId(flockId);
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="css/calendar.css"/>
    <style type="text/css">
        div.tableHolder {
            OVERFLOW: auto;
            WIDTH: 650px;
            POSITION: relative;
            HEIGHT: 150px;
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
    <script type="text/javascript" src="js/calendar.js"></script>
    <script type="text/javascript">
        function calcTotalCost(amount, price, total) {
            // calculate cost
            var amountG = document.getElementById(amount);
            if (amountG.value == "") {
                amountG.value = "0";
            }
            var amountSpread = parseInt(amountG.value);

            var priceG = document.getElementById(price);
            if (priceG.value == "") {
                priceG.value = "0.0";
            }
            var priceSpread = parseFloat(priceG.value);

            var totalCost = 0.0;
            var totalCost = amountSpread * priceSpread;
            document.getElementById(total).value = totalCost;
        }
        function validate() {
            var name = document.getElementById('name').value;
            var amnt = document.getElementById('amount').value;
            var prc = document.getElementById('price').value;

            if (amnt == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (prc == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (name == "") {
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
    <title>Add Distribution</title>
</head>
<body>
<h1>Distribution Details</h1>
<br>

<table id="msg" class="errMsg" align="center" style="display: none;">
    <tr>
        <td>Fields can't be empty</td>
    </tr>
</table>

<form id="addform" name="addform" action="./add-distrib-details.html" method="post">
    <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" id="controllerId" name="controllerId" value="<%=controllerId%>">
    <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">
    <button type="button" onclick='javascript:closePopup()'>Close</button>
    <input type="submit" value="Add">
    <br/><br/>
    <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
        <tr>
            <th>Date</th>
            <td><input type="text" id="startDate" name="startDate" size="10" readonly>
                <img src="img/calendar.png" border="0" onclick="GetDate('start');"/></td>
            <th>Number Account</th>
            <td><input type="text" id="account" name="account" size="10"></td>
            <th>Sex</th>
            <td>
                <select id="sex" name="sex">
                    <option value="none"></option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>Target</th>
            <td><input type="text" id="target" name="target" value="" size="10"></td>
            <th>Quantity Birds</th>
            <td><input type="text" id="quantbirds" name="quantbirds" value="" size="10"></td>
        </tr>
    </table>
    <br/>
    <br/>
    <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
        <tr>
            <th>Quantity A (kg)</th>
            <td><input type="text" id="QuantAKg" name="QuantAKg" size="10"></td>
            <th>Price A</th>
            <td><input type="text" id="PriceA" name="QuantBKg" size="10"></td>
            <th>Quantity A (%)</th>
            <td><input type="text" id="QuantA" name="QuantCKg" size="10" readonly></td>
        </tr>
        <tr>
            <th>Quantity B (kg)</th>
            <td><input type="text" id="QuantBKg" name="QuantCKg" size="10"></td>
            <th>Price B</th>
            <td><input type="text" id="PriceB" name="PriceB" size="10"></td>
            <th>Quantity B (%)</th>
            <td><input type="text" id="QuantB" name="QuantB" size="10" readonly></td>
        </tr>
        <tr>
            <th>Quantity C (kg)</th>
            <td><input type="text" id="QuantCKg" name="QuantCKg" value="" size="10"></td>
            <th>Price C</th>
            <td><input type="text" id="PriceC" name="PriceC" size="10"></td>
            <th>Quantity C (%)</th>
            <td><input type="text" id="QuantC" name="QuantC" size="10" readonly></td>
        </tr>
    </table>
    <br/>
    <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
        <tr>
            <th>Veterinary (kg)</th>
            <td><input type="text" id="VeterinKg" name="VeterinKg" readonly size="10"></td>
            <th>Veterinary (%)</th>
            <td><input type="text" id="Veterin" name="Veterin" readonly size="10"></td>
        </tr>
        <tr>
            <th>Another (kg)</th>
            <td><input type="text" id="AnotherKg" name="AnotherKg" readonly size="10"></td>
            <th>Another (%)</th>
            <td><input type="text" id="Another" name="Another" readonly size="10"></td>
        </tr>
    </table>
</form>
</body>
</html>
