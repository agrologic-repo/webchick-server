<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.MedicineDao" %>
<%@ page import="com.agrologic.app.model.Medicine" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="language.jsp" %>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    MedicineDao medicineDao = DbImplDecider.use(DaoType.MYSQL).getDao(MedicineDao.class);
    Collection<Medicine> medicineList = medicineDao.getAllByFlockId(flockId);
%>
<!DOCTYPE html>

<html dir="<%=session.getAttribute("dir")%>">
<head>

    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/calendar.css"/>
    <style type="text/css">
        div.tableHolder {
            OVERFLOW: auto;
            WIDTH: 550px;
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
    <script type="text/javascript" src="resources/javascript/calendar.js">;</script>
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
    <title>Add Medicine</title>
</head>
<body>
<h1>Add Medicine</h1>
<br>
<button type="button" onclick='javascript:closePopup()'>Close</button>
<table id="msg" class="errMsg" align="center" style="display: none;">
    <tr>
        <td>Fields can't be empty</td>
    </tr>
</table>
<form id="addform" name="addform" action="./add-medicine.html" method="post">
    <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" id="controllerId" name="controllerId" value="<%=controllerId%>">
    <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">

    <div class="tableHolder" style="width: 550px; height: 200px; padding-left:5px;padding-right:10px;">
        <table border="1" style="width: 500px;border:1px solid #C6C6C6; border-collapse: collapse;">
            <thead>
            <th>Amount</th>
            <th>Name</th>
            <th>Price</th>
            <th>Total</th>
            <th>Action</th>
            </thead>
            <tbody>
            <tr>
                <td><input type="text" id="amount" name="amount" value="" size="10"
                           onblur="javascript:calcTotalCost('amount', 'price', 'total');"></td>
                <td><input type="text" id="name" name="name" value="" size="10"></td>
                <td><input type="text" id="price" name="price" value="" size="10"
                           onblur="javascript:calcTotalCost('amount', 'price', 'total');"></td>
                <td><input type="text" id="total" name="total" readonly value="" size="10"></td>
                <td align="center"><img src="resources/images/plus1.gif" border="0" hspace="4">
                    <a href="javascript:validate();">Add</a>
                </td>
            </tr>
            <%for (Medicine medicine : medicineList) {%>
            <tr>
                <td><%=medicine.getAmount() %>
                </td>
                <td><%=medicine.getName() %>
                </td>
                <td><%=medicine.getPrice() %>
                </td>
                <td><%=medicine.getTotal() %>
                </td>
                <td align="center"><img src="resources/images/close.png" border="0" hspace="4">
                    <a href="javascript:window.location='./remove-medicine.html?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>&medicineId=<%=medicine.getId() %>';">Remove</a>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
    </div>
</form>
</body>
</html>