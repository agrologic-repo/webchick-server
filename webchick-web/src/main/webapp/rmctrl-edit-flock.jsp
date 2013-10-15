<jsp:directive.page import="com.agrologic.app.model.Controller"/>
<jsp:directive.page import="com.agrologic.app.model.Flock"/>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="java.util.Collection" %>


<%  User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Collection<Controller> controllers = (Collection<Controller>) request.getAttribute("controllers");
    Flock editFlock = getFlock(controllers, controllerId, flockId);
%>
<%!
    Flock getFlock(Collection<Controller> controllers, Long controllerId, Long flockId) {
        for (Controller c : controllers) {
            if (c.getId() == controllerId) {
                for (Flock f : c.getFlocks()) {
                    if (f.getFlockId() == flockId) {
                        return f;
                    }
                }
            }
        }
        return null;
    }
%>


<!DOCTYPE html>

<html dir="<%=session.getAttribute("dir")%>">
<head>


    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">

    </script>
    <title>Edit Flock</title>
</head>
<body>
<table width="1024px" cellpadding="0" cellspacing="0" style="padding:0px;" align="center">
    <tr>
        <td width="100%" colspan="2">
            <%@include file="usermenuontop.jsp" %>
        </td>
    </tr>
    <tr>
        <form name="flockForm" action="./save-flock.html" method="post">

            <td valign="top" height="648px">
                <table border=0 cellPadding=1 cellSpacing=1 width="736">
                    <tr>
                        <td width="483">
                            <p>

                            <h1>Edit Flock - <%=editFlock.getFlockName()%>
                            </h1></p>
                        </td>
                    </tr>
                    <tr>

                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <p>

                                        <h2>flocks list</h2></p>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="0">
                                        <table class="table-list" border=1 width="100%" cellpadding="3"
                                               >
                                            <tr>
                                                <th></th>
                                                <th colspan="2">Quantity</th>
                                                <th>Price</th>
                                            </tr>
                                            <tr>
                                                <td>Male</td>
                                                <td colspan="2"><input type="text"
                                                                       value="<%=editFlock.getQuantityMale() %>"></td>
                                                <td><input type="text" size="5"
                                                           value="<%=editFlock.getCostChickMale() %>">&nbsp;$
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Female</td>
                                                <td colspan="2"><input type="text"
                                                                       value="<%=editFlock.getQuantityFemale() %>"></td>
                                                <td><input type="text" size="5"
                                                           value="<%=editFlock.getCostChickFemale() %>">&nbsp;$
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Total</td>
                                                <td colspan="2"><input type="text" readonly
                                                                       value="<%=editFlock.getTotalChicks() %>"></td>
                                                <td><input type="text" size="5" readonly
                                                           value="<%=editFlock.calcTotalChicksCost() %>">&nbsp;$
                                                </td>
                                            </tr>
                                            <tr>
                                                <th></th>
                                                <th>Start</th>
                                                <th>End</th>
                                                <th>Price</th>
                                            </tr>
                                            <tr>
                                                <td>Water</td>
                                                <td><input type="text" size="10"
                                                           value="<%=editFlock.getWaterBegin() %>"></td>
                                                <td><input type="text" size="10" value="<%=editFlock.getWaterEnd() %>">
                                                </td>
                                                <td><input type="text" size="5" value="<%=editFlock.getCostWater() %>">&nbsp;$
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Total</td>
                                                <td colspan="2"><input type="text" readonly
                                                                       value="<%=editFlock.getTotalWater() %>"></td>
                                                <td><input type="text" size="5" readonly
                                                           value="<%=editFlock.calcTotalWaterCost()%>">&nbsp;$
                                                </td>
                                            </tr>

                                            <tr>
                                                <th></th>
                                                <th>Start</th>
                                                <th>End</th>
                                                <th>Price</th>
                                            </tr>
                                            <tr>
                                                <td>Electricity</td>
                                                <td><input type="text" size="10"
                                                           value="<%=editFlock.getElectBegin() %>"></td>
                                                <td><input type="text" size="10" value="<%=editFlock.getElectEnd() %>">
                                                </td>
                                                <td><input type="text" size="5" value="<%=editFlock.getCostElect() %>">&nbsp;$
                                                </td>

                                            </tr>
                                            <tr>
                                                <td>Total</td>
                                                <td colspan="2"><input type="text" readonly
                                                                       value="<%=editFlock.getTotalElect() %>"></td>
                                                <td><input type="text" size="5" readonly
                                                           value="<%=editFlock.calcTotalElectCost()%>">&nbsp;$
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>

                    </tr>
                    <tr>
                        <td colspan="2">
                            <button onclick='return back("flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>");'>
                                &nbsp;Back
                            </button>
                            <button type="submit" id="btnSave" name="btnSave">&nbsp;Save &nbsp;</button>
                        </td>
                    </tr>
                </table>
            </td>
        </form>
    </tr>
</table>
</body>
</html>
