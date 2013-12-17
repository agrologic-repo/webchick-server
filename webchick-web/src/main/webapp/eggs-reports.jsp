<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <style>
        label, input {
            display: block;
        }

        input.text {
            margin-bottom: 12px;
            width: 95%;
            padding: .4em;
        }

        h1 {
            font-size: 1.2em;
            margin: .6em 0;
        }

        div#eggs-reports-contain {
            /*width: 100%;*/
            margin: 20px 0;
        }

        div#eggs-reports-contain table {
            margin: 1em 0;
            border-collapse: collapse;
            /*width: 100%;*/
        }

        div#eggs-reports-contain table td, div#eggs-reports-contain table th {
            border: 1px solid #eee;
            padding: .6em 10px;
            text-align: left;
        }

        .ui-dialog .ui-state-error {
            padding: .3em;
        }

        .validateTips {
            border: 1px solid transparent;
            padding: 0.3em;
        }
    </style>


    <%--<script src="resources/javascript/jquery.tablesorter.js">;</script>--%>
    <%--<script src="resources/javascript/jquery.scrolltable.js">;</script>--%>
    <script>
        $(function () {
            $('.scrollTable').scrolltable({
                stripe: true,
                maxWidth: 200,
//        height:150,
                setWidths: true
            });
        });
    </script>
</head>
<body>
<table width="100%">
    <tr>
        <td valign="top" width="100%">
            <div id="eggs-reports-contain" class="ui-widget">
                <table id="eggs-reports" class="scrollTable">
                    <thead>
                    <tr class="ui-widget-header">
                        <th><%=session.getAttribute("table.eggs.day")%>
                        </th>
                        <th><%=session.getAttribute("table.eggs.birds")%>
                        </th>
                        <th><%=session.getAttribute("table.eggs.egg.quantity")%>
                        </th>
                        <th>Average eggs</th>
                        <th>Actual eggs</th>
                        <th>Cracked eggs (%)</th>
                        <th>Feed per bird</th>
                        <th>Water per bird</th>
                        <th>Feed per egg</th>
                        <th>Water per egg</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="item">
                        <tr id="<c:out value="${item.day}"/>">
                            <td><c:out value="${item.day}"/></td>
                            <td><c:out value="${item.numOfBirds}"/></td>
                            <td><c:out value="${item.eggQuantity}"/></td>
                            <td><c:out value="${(item.eggQuantity/item.numOfBirds)*100}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>

                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>
                            <td><c:out value="${item.eggQuantity - item.softShelled - item.cracked}"/></td>

                                <%--<td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"--%>
                                <%--value="${item.cracked/item.eggQuantity}"/></td>--%>
                                <%--<td><fmt:formatNumber type="number" maxFractionDigits="3"--%>
                                <%--value="${item.feedConsump/item.numOfBirds}" /></td>--%>
                                <%--<td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"--%>
                                <%--value="${item.waterConsump/item.numOfBirds}"/></td>--%>
                                <%--<td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"--%>
                                <%--value="${item.feedConsump/item.eggQuantity}"/></td>--%>
                                <%--<td><fmt:formatNumber type="percent" maxIntegerDigits="3" maxFractionDigits="2"--%>
                                <%--value="${item.waterConsump/item.eggQuantity}"/></td>--%>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
</table>
</body>
</html>
