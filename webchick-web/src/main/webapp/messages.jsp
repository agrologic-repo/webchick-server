<link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <c:when test="${error}">
                <div class="ui-widget">
                    <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                        <p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
                            <strong> Alert : </strong><c:out value="${message}"/></p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="ui-widget">
                    <div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;">
                        <p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
                            <strong> Info : </strong><c:out value="${message}"/></p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
