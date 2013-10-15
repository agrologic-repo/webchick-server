<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.FeedDao" %>
<%@ page import="com.agrologic.app.dao.FeedTypeDao" %>
<%@ page import="com.agrologic.app.dao.mysql.impl.FeedDaoImpl" %>
<%@ page import="com.agrologic.app.dao.mysql.impl.FeedTypeDaoImpl" %>
<%@ page import="com.agrologic.app.model.Feed" %>
<%@ page import="com.agrologic.app.model.FeedType" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="language.jsp" %>

<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    FeedDao feedDao = DbImplDecider.use(DaoType.MYSQL).getDao(FeedDaoImpl.class);
    Collection<Feed> feedList = feedDao.getAllByFlockId(flockId);
    FeedTypeDao feedTypeDao = DbImplDecider.use(DaoType.MYSQL).getDao(FeedTypeDaoImpl.class);
    Collection<FeedType> feedTypeList = feedTypeDao.getAllByCellinkId(cellinkId);
%>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>

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
            var amountFeed = parseInt(amountG.value);

            var priceG = document.getElementById(price);
            if (priceG.value == "") {
                priceG.value = "0.0";
            }
            var priceFeed = parseFloat(priceG.value);

            var totalCost = 0.0;
            var totalCost = amountFeed * priceFeed;
            document.getElementById(total).value = totalCost;
        }
        function validate() {
            var amnt = document.getElementById('amount').value;
            var strd = document.getElementById('startDate').value;
            var nmac = document.getElementById('numberAccount').value;

            if (amnt == "") {
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
            WIDTH: 750px;
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
    <title>Add Feed</title>
</head>
<body>
<h1>Add Feed</h1>
<br>
<button type="button" onclick='javascript:closePopup()'>Close</button>
<button type="button"
        onclick="window.location='./rmctrl-add-feedtype.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>'">
    Add Feed Type
</button>
<table id="msg" class="errMsg" align="center" style="display: none;">
    <tr>
        <td>Fields can't be empty</td>
    </tr>
</table>
<form id="addform" name="addform" action="./add-feed.html" method="post">
    <input type="hidden" id="cellinkId" name="cellinkId" value="<%=cellinkId%>">
    <input type="hidden" id="controllerId" name="controllerId" value="<%=controllerId%>">
    <input type="hidden" id="flockId" name="flockId" value="<%=flockId%>">

    <div class="tableHolder" style="width: 750px; height: 200px; padding-left:5px;padding-right:10px;">
        <table border="1" style="width: 750px;border:1px solid #C6C6C6; border-collapse: collapse;">
            <thead>
            <th align="center">Amount</th>
            <th align="center">Date</th>
            <th align="center">Feed Type</th>
            <th align="center">Number Account</th>
            <th align="center">Total</th>
            <th align="center" width="100px">Action</th>
            </thead>
            <tr>
                <td><input type="text" id="amount" name="amount"></td>
                <td><input type="text" id="startDate" name="startDate" size="10" readonly>
                    <img src="resources/images/calendar.png" border="0" onclick="GetDate('start');"/></td>
                <td>
                    <select id="feedtypeid" name="feedtypeid">
                        <option value="0" select></option>
                        <% for (FeedType f : feedTypeList) {%>
                        <option value="<%=f.getId() %>"><%=f.getFeedType() %>
                        </option>
                        <%}%>
                    </select>
                </td>
                <td><input type="text" id="numberAccount" name="numberAccount" value=""></td>
                <td><input type="text" id="total" name="total" readonly value="" size="10"></td>
                <td align="center"><img src="resources/images/plus1.gif" border="0" hspace="4">
                    <a href="javascript:validate();">Add</a>
                </td>
            </tr>
            <%for (Feed feed : feedList) {%>
            <tr>
                <td><%=feed.getAmount()%>
                </td>
                <td><%=feed.getDate()%>
                </td>
                <%String feedType = "";%>
                <td>
                    <%for (FeedType f : feedTypeList) {%>
                    <%
                        if (feed.getType().equals(f.getId())) {
                            feedType = f.getFeedType();
                    %>
                    <%}%>
                    <%}%>
                    <%=feedType%>
                </td>
                <td><%=feed.getNumberAccount()%>
                <td><%=feed.getTotal()%>
                </td>
                <td align="center"><img src="resources/images/close.png" border="0" hspace="4">
                    <a href="javascript:window.location='./remove-feed.html?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>&feedId=<%=feed.getId()%>';">Remove</a>
                </td>
            </tr>
            <%}%>
        </table>
    </div>
</form>
</body>
</html>