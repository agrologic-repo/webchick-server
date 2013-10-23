<%@ page import="com.agrologic.app.model.Flock" %>
<%
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Flock expansesFlock = (Flock) session.getAttribute("flock");
%>
<table>
    <tr>
        <td valign="top">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend>Spread</legend>
                <p>
                <span>
                <button style="width: 80px"
                        onclick="javascript:void(window.open('./rmctrl-add-spread.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                    Add
                </button>
                </span>
                </p>

                <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                    <tr>
                        <th nowrap>Total Spread</th>
                        <td align="center"><input type="text" readonly value="<%=expansesFlock.getSpreadAdd() %>">
                        </td>
                    </tr>
                    <tr>
                        <th>Total Sum</th>
                        <td align="center"><input type="text" readonly value="<%=expansesFlock.getTotalSpread() %>">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </td>
        <td valign="top">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend>Feed</legend>
                <p>
                <button style="width: 80px"
                        onclick="javascript:void(window.open('./rmctrl-add-feed.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                    Add
                </button>
                </p>
                <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                    <tr>
                        <th nowrap>Total Feed</th>
                        <td align="center"><input type="text" readonly value="<%=expansesFlock.getFeedAdd() %>">
                        </td>
                    </tr>
                    <tr>
                        <th>Total Expances</th>
                        <td align="center"><input type="text" readonly value="<%=expansesFlock.getTotalFeed() %>">
                        </td>
                    </tr>
                    <tr>
                        <th nowrap>Total Feed Consumption<br><span
                                style="font-size: smaller">(from controller)</span>
                        </th>
                        <td align="center"><input type="text" readonly></td>
                    </tr>
                </table>
            </fieldset>
        </td>
        <td valign="top">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend> Medicine</legend>
                <p>
                <span>
                <button style="width: 80px"
                        onclick="javascript:void(window.open('./rmctrl-add-medicine.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                    Add
                </button>
                </span>
                </p>
                <table>
                    <tr>
                        <th nowrap>Total</th>
                        <td><input type="text" value="<%=expansesFlock.getTotalMedic() %>"></td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td valign="top">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend> Labor</legend>
                <p>
                <span>
                <button style="width: 80px"
                        onclick="javascript:void(window.open('./rmctrl-add-labor.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                    Add
                </button>
                </span>
                </p>
                <table>
                    <tr>
                        <th>Total</th>
                        <td><input type="text" value="<%=expansesFlock.getTotalLabor() %>"></td>
                    </tr>
                </table>
            </fieldset>
        </td>
        <td valign="top">
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend> Transaction</legend>
                <p>
                <span>
                <button style="width: 80px"
                        onclick="javascript:void(window.open('./rmctrl-add-transaction.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                    Add
                </button>
                </span>
                </p>
                <table>
                    <tr>
                        <th nowrap>Total Expenses</th>
                        <th nowrap>Total Revenues</th>
                    </tr>
                    <tr>
                        <td><input type="text" readonly value="<%=expansesFlock.getExpenses() %>"></td>
                        <td><input type="text" readonly value="<%=expansesFlock.getRevenues() %>"></td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
</table>