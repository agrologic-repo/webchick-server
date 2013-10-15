<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class="table-pages" dir="${dir}" width="100%">
    <tr>
        <fmt:parseNumber var="fromInt" type="number" value="${from}"/>
        <fmt:parseNumber var="toInt" type="number" value="${to}"/>
        <fmt:parseNumber var="ofInt" type="number" value="${of}"/>
        <td><%=session.getAttribute("paging.showing")%> ${from}-${to} <%=session.getAttribute("paging.from")%> ${of}</td>
    </tr>
    <tr>
        <td>
            <c:choose>
                <c:when test="${ofInt % 25 > 0}">
                    <c:set var="pages" value="${ (ofInt / 25) + 1}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="pages" value="${ (ofInt / 25) }"/>
                </c:otherwise>
            </c:choose>
            <%--if no rows to show --%>
            <c:if test="${pages == 0}">
                <c:set var="pages" value="${1}"/>
            </c:if>
            <c:forEach var="i" begin="0" end="${pages - 1}">
                <c:choose>
                    <%--set active page --%>
                    <c:when test="${i * 25 <= fromInt && (i * 25) + 25 >= toInt}">
                        <a class="active" href="${param.navigate}?index=${i * 25}&searchText=${searchText}&state=${state}&role=${role}&company=${company}">${i + 1}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${param.navigate}?index=${i * 25}&searchText=${searchText}&state=${state}&role=${role}&company=${company}">${i + 1}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </td>
    </tr>
</table>

