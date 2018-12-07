<%@ page import="com.agrologic.app.graph.GenerateGraph" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page errorPage="anerrorpage.jsp" %>--%>
<%@ include file="language.jsp" %>

<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <title><spring:message code="history.graph.page.title"/>
    </title>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="resources/style/jquery.tablescroll.css"/>
    <style media="screen" type="text/css">
        .loadingClass {
            position: absolute;
            z-index: 1;
            top: 100px;
            left: 0px;
            visibility: visible;
        }

        .contentClass {
            position: absolute;
            z-index: 2;
            top: 0px;
            left: 0px;
            visibility: hidden
        }
    </style>
    <script type="text/javascript" src="resources/javascript/jquery.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery-ui.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablescroll.js">;</script>
    <script type="text/javascript" src="resources/javascript/jquery.tablesorter.js">;</script>
</head>
</head>
<body onload="downLoad()">
<div id="loadingDiv" class="loadingClass" style="width: 100%">
    <table width="100%">
        <tr>
            <td align="center"><p><strong><em><spring:message code="graph.please.wait.while.page.loading"/>
            </em></strong></p></td>
        </tr>
    </table>
</div>
<div id="contentDiv" class="contentClass" style="width: 100%">
    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
        <table width="100%">
            <tr>
                <td>
                    <table>
                        <tr>
                            <td valign="top">
                                <table width="100%">
                                    <tr>
                                        <td align="center"><p><strong><em><spring:message code="graph.please.wait.while.page.loading"/>
                                        </em></strong></p></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </fieldset>
</div>
</body>
</html>