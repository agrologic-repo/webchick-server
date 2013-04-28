<%--
    Document   : rmctrl-edit-flock.jsp
    Created on : Oct 24, 2010, 3:08:55 PM
    Author     : Valery Manakhimov
    Company    : Agrologic Ltd. �
    Version    : 0.1.1.1
--%>
<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.model.FlockDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    String message =(String)request.getSession().getAttribute("message");
    request.getSession().setAttribute("message",null);

    Boolean errorFlag = (Boolean)request.getSession().getAttribute("error");
    request.getSession().setAttribute("error",null);

    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    List<ControllerDto> controllers = (List<ControllerDto>)request.getSession().getAttribute("controllers");
    FlockDto editFlock = getFlock(controllers,controllerId,flockId);
%>
<%!
    FlockDto getFlock(List<ControllerDto> controllers,Long controllerId,Long flockId) {
        for(ControllerDto c:controllers) {
            if(c.getId() == controllerId) {
                for(FlockDto f:c.getFlocks()) {
                    if(f.getFlockId() == flockId) {
                        return f;
                    }
                }
            }
        }
        return null;
    }
%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="shortcut icon" href="img/favicon5.ico" title="AgroLogic Tm.">
        <link rel="StyleSheet" type="text/css" href="css/menubar.css">
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css">
        <script type="text/javascript">
        function back(link){
            window.document.location.replace(link);
            return false;
        }
        </script>
        <title>Edit Flock</title>
    </head>
    <body>
    <table width="1024px" cellpadding="0" cellspacing="0" style="padding:0px;" align="center">
      <tr>
        <td width="100%" colspan="2"><%@include file="usermenuontop.jsp"%></td>
      </tr>
      <tr>
        <form name="flockForm" action="./save-flock.html" method="post">

        <td valign="top" height="648px">
          <table border=0 cellPadding=1 cellSpacing=1 width="736">
            <tr>
              <td width="483">
               <p><h1>Edit Flock - <%=editFlock.getFlockName()%></h1></p>
              </td>
            </tr>
            <tr>

              <td>
                <table>
                  <tr>
                    <td>
                        <p><h2>flocks list</h2></p>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="0">
                      <table class="table-list" border=1 width="100%" cellpadding="3" style="behavior:url(tablehl.htc) url(sort.htc);">
                        <tr>
                            <th></th>
                            <th colspan="2">Quantity</th>
                            <th>Price</th>
                        </tr>
                        <tr>
                            <td>Male</td>
                            <td colspan="2"><input type="text" value="<%=editFlock.getQuantityMale() %>"></td>
                            <td><input type="text" size="5"  value="<%=editFlock.getPriceChickMale() %>">&nbsp;$</td>
                        </tr>
                        <tr>
                            <td>Female</td>
                            <td colspan="2"><input type="text" value="<%=editFlock.getQuantityFemale() %>"></td>
                            <td><input type="text" size="5"  value="<%=editFlock.getPriceChickFemale() %>">&nbsp;$</td>
                        </tr>
                        <tr>
                            <td>Total</td>
                            <td colspan="2"><input type="text" readonly value="<%=editFlock.getTotalChicks() %>"></td>
                            <td><input type="text" size="5" readonly value="<%=editFlock.calcTotalChicksPrice() %>">&nbsp;$</td>
                        </tr>
                        <tr>
                            <th></th>
                            <th>Start</th>
                            <th>End</th>
                            <th>Price</th>
                        </tr>
                        <tr>
                            <td>Water</td>
                            <td><input type="text" size="10" value="<%=editFlock.getWaterBegin() %>"></td>
                            <td><input type="text" size="10" value="<%=editFlock.getWaterEnd() %>"></td>
                            <td><input type="text" size="5" value="<%=editFlock.getPriceWater() %>">&nbsp;$</td>
                        </tr>
                        <tr>
                            <td>Total</td>
                            <td colspan="2"><input type="text" readonly value="<%=editFlock.getTotalWater() %>"></td>
                            <td><input type="text" size="5" readonly value="<%=editFlock.calcTotalWaterPrice()%>">&nbsp;$</td>
                        </tr>

                        <tr>
                            <th></th>
                            <th>Start</th>
                            <th>End</th>
                            <th>Price</th>
                        </tr>
                        <tr>
                            <td>Electricity</td>
                            <td><input type="text" size="10" value="<%=editFlock.getElectBegin() %>"></td>
                            <td><input type="text" size="10" value="<%=editFlock.getElectEnd() %>"></td>
                            <td><input type="text" size="5" value="<%=editFlock.getPriceElect() %>">&nbsp;$</td>

                        </tr>
                        <tr>
                            <td>Total</td>
                            <td colspan="2"><input type="text" readonly value="<%=editFlock.getTotalElect() %>"></td>
                            <td><input type="text" size="5" readonly value="<%=editFlock.calcTotalelectPrice()%>">&nbsp;$</td>
                        </tr>
                        </table>
                    </td>
                  </tr>
                </table>
              </td>

            </tr>
            <tr>
              <td colspan="2">
                <button onclick='return back("flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>");'>&nbsp;Back</button>
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
