<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose>
    <c:when test="${empty message}">
        <table align="center" width="250px">
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${errorFlag}">
                <table class="errMsg" align="center">
                    <tr>
                        <td><img src="img/unsuccess.gif"/>&nbsp;&nbsp;&nbsp;
                            <b> <c:out value="${message}"/></b>
                        </td>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                <table class="infoMsg" align="center">
                    <tr>
                        <td><img src="img/success.png"/>&nbsp;&nbsp;&nbsp;
                            <b> <c:out value="${message}"/></b>
                        </td>
                    </tr>
                </table>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
