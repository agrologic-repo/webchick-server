<%@ page import="com.agrologic.app.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="../../anerrorpage.jsp" %>
<%@ include file="../../language.jsp" %>

<c:set var="STATE_OFFLINE" value="<%=com.agrologic.app.model.CellinkState.STATE_OFFLINE%>"/>
<c:set var="STATE_STOP" value="<%=com.agrologic.app.model.CellinkState.STATE_STOP%>"/>
<c:set var="STATE_RUNNING" value="<%=com.agrologic.app.model.CellinkState.STATE_RUNNING%>"/>
<c:set var="STATE_START" value="<%=com.agrologic.app.model.CellinkState.STATE_START%>"/>
<c:set var="STATE_ONLINE" value="<%=com.agrologic.app.model.CellinkState.STATE_ONLINE%>"/>

<%  User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    request.setAttribute("role", user.getRole());
%>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><spring:message code="myfarms.page.title"/></title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <script type="text/javascript" src="resources/javascript/general.js">;</script>
    <script type="text/javascript">
        function disconnectCellink(cellinkId) {
            if (!confirm("Are you sure ?")) {
                return;
            }
            window.location.replace("./disconnect-cellink.html?userId=<%=user.getId()%>&cellinkId=" + cellinkId);
        }
    </script>
</head>
<body>
<div id="header">
    <%@include file="../../usermenuontop.jsp" %>
</div>
<div id="main-shell">
    <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
            <td colspan="2" width="100%">
                <h1><spring:message code="myfarms.page.header"/></h1>
                <h2><spring:message code="myfarms.page.sub.header"/></h2>
            </td>
        </tr>

        <c:forEach items="${cellinks}" var="cellink">
        <tr>
            <td width="100%">
                <form id="formFarms" name="formFarms" style="display:inline">
                    <table id="style=border-left-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-right-width: 0px; border-style: solid; border-collapse: collapse;" width="100%">
                        <tr>
                            <td colspan="2">
                                <c:choose>
                                <c:when test="${cellink.online}" >
                                <table id="farms-online" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="resources/images/chicken_house1.jpg"
                                                 onclick="javascript:window.location.href='./rmctrl-main-screen.html?userId=<c:out value="${cellink.userId}"/>&cellinkId=<c:out value="${cellink.id}"/>' "/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><c:out value="${cellink.name}"/></h1>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <a class="button"
                                                           href="rmctrl-main-screen.html?userId=<c:out value="${cellink.userId}"/>&cellinkId=<c:out value="${cellink.id}"/>&role=<%=user.getRole() %>"><%=session.getAttribute("button.show.houses") %>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="label.cellink.version"/><c:out value="${cellink.version}"/></td>
                                                </tr>
                                                <tr>
                                                    <td align="left"><spring:message code="label.cellink.status.online"/></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <c:choose>
                                                                <c:when test="${cellink.cellinkState.value == STATE_RUNNING}">
                                                                <td>
                                                                    <img src="resources/images/disconnect.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="#"
                                                                       onclick="disconnectCellink(<c:out value="${cellink.id}"/>)">
                                                                        <spring:message code="button.disconnect.cellink"/>

                                                                    </a>
                                                                </td>
                                                                </c:when>
                                                                </c:choose>
                                                                <td>
                                                                    <img src="resources/images/setting.png"
                                                                         style="cursor: pointer" border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<c:out value="${cellink.userId}"/>&cellinkId=<c:out value="${cellink.id}"/>">
                                                                        <spring:message code="button.setting"/>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="resources/images/refresh.gif"
                                                                         style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<c:out value="${cellink.userId}"/>">
                                                                        <spring:message code="button.refresh"/>
                                                                    </a>
                                                                </td>
                                                                <!----------------------------------------------------------------------------------------------------------->
                                                                <%--<td>--%>
                                                                    <%--<a href="./editcellinkrequest.html?userId=<c:out value="${cellink.userId}"/>&cellinkId=<c:out value="${cellink.id}"/>">--%>
                                                                        <%--<img src="resources/images/edit.gif" style="cursor: pointer" border="0" hspace="5"/>--%>
                                                                        <%--<%=session.getAttribute("button.edit")%>--%>
                                                                    <%--</a>--%>
                                                                <%--</td>--%>
                                                                <!--------------------------------------------------------------------------------------------------------------->
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                </c:when>
                                <c:otherwise>
                                <table id="farms-offline" border=0 cellpadding="10" cellspacing="10">
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <img class="link" src="resources/images/chicken_house1.jpg"/>
                                        </td>
                                        <td align="center">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h1><c:out value="${cellink.name}"/></h1>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table class="table-list-small" border="0">
                                                <tr>
                                                    <td>
                                                        <hr/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="label.cellink.version"/>
                                                        <c:out value="${cellink.version}"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><spring:message code="label.cellink.offline.since"/>
                                                        <c:out value="${cellink.formatedTime}" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table class="table-list">
                                                            <tr>
                                                                <td>
                                                                    <img src="resources/images/setting.png"
                                                                         style="cursor: pointer"
                                                                         title="<spring:message code="button.connect.cellink"/>"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./cellink-setting.html?userId=<c:out value="${cellink.userId}"/>&cellinkId=<c:out value="${cellink.id}"/>"><spring:message code="button.setting"/>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <img src="resources/images/refresh.gif"
                                                                         style="cursor: pointer"
                                                                         border="0" hspace="5"/>
                                                                    <a href="./my-farms.html?userId=<c:out value="${cellink.userId}"/>"><spring:message code="button.refresh"/>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <hr>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>