<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="language.jsp" %>

<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.GasDao" %>
<%@ page import="com.agrologic.app.model.Gas" %>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    GasDao gasDao = DbImplDecider.use(DaoType.MYSQL).getDao(GasDao.class);
    Collection<Gas> gazList = gasDao.getAllByFlockId(flockId);

%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/calendar.css"/>
    <script type="text/javascript" src="resources/javascript/calendar.js">;</script>
    <script type="text/javascript">
        function calcTotalCost(amount, price, total) {
            // calculate cost

            var amountG = document.getElementById(amount);
            if (amountG.value == "") {
                amountG.value = "0";
            }
            var amountGas = parseInt(amountG.value);

            var priceG = document.getElementById(price);
            if (priceG.value == "") {
                priceG.value = "0.0";
            }
            var priceGas = parseFloat(priceG.value);

            var totalCost = 0.0;
            var totalCost = amountGas * priceGas;
            document.getElementById(total).value = totalCost;
        }
        function validate() {
            var amnt = document.getElementById('amount').value;
            var prc = document.getElementById('price').value;
            var strd = document.getElementById('startDate').value;
            var nmac = document.getElementById('numberAccount').value;
            var ttl = document.getElementById('total').value;

            if (amnt == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (prc == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (strd == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (nmac == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            if (ttl == "") {
                document.getElementById('msg').style.display = 'block';
                return;
            }
            document.addform.submit();

        }
    </script>
    <title>Add Gas</title>
</head>
<body>
<h1>Add Gas</h1>
<br>
<button type="button" onclick='self.close();'>Close</button>
<table id="msg" class="errMsg" align="center" style="display: none;">
    <tr>
        <td>Fields can't be empty</td>
    </tr>
</table>
<form id="addform" name="addform" action="./add-gaz.html" method="post">
    <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">
    <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
        <tr>
            <th align="center">Amount</th>
            <th align="center">Date</th>
            <th align="center">Price</th>
            <th align="center">Number Account</th>
            <th align="center">Total</th>
            <th align="center" width="100px">Action</th>
        </tr>
        <tr>
            <td><input type="text" id="amount" name="amount"
                       onblur="javascript:calcTotalCost('amount', 'price', 'total');"></td>
            <td><input type="text" id="startDate" name="startDate" size="10" readonly>
                <img src="resources/images/calendar.png" border="0" onclick="GetDate('start');"/></td>
            <td><input type="text" id="price" name="price" size="10"
                       onblur="javascript:calcTotalCost('amount', 'price', 'total');">
                <select id="currency" name="currency">
                    <option value="1">$</option>
                    <option value="2">&#8362;</option>
                </select>
            </td>
            <td><input type="text" id="numberAccount" name="numberAccount" value="" size="10"></td>
            <td><input type="text" id="total" name="total" readonly value="" size="10"></td>
            <td align="center"><img src="resources/images/plus1.gif" border="0" hspace="4">
                <a href="javascript:validate();">Add</a>
            </td>

        </tr>
        <%
            for (Gas gaz : gazList) {%>
        <tr>
            <td><%=gaz.getAmount() %>
            </td>
            <td><%=gaz.getDate() %>
            </td>
            <td><%=gaz.getPrice() %>
            </td>
            <td><%=gaz.getNumberAccount() %>
            <td><%=gaz.getTotal() %>
            </td>
            <td align="center"><img src="resources/images/close.png" border="0" hspace="4">
                <a href="javascript:window.location='./remove-gaz.html?cellinkId=<%=cellinkId%>&flockId=<%=flockId%>&gazId=<%=gaz.getId() %>';">Remove</a>
            </td>
        </tr>
        <%}%>
    </table>
</form>
</body>
</html>