<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.Cellink" %>

<jsp:directive.page import="com.agrologic.app.model.User"/>

<% User user = (User) request.getSession().getAttribute("user");

    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    Cellink cellink = (Cellink) request.getSession().getAttribute("cellink");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<HTML>
<HEAD>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Tm.">

    <TITLE> Remote control menu </TITLE>
    <style type="text/css">
        body {
            background-color: white;
            margin: 0px;
            font: normal 11px tahoma
        }

        table {
            font: normal 11px tahoma;
            width: 165px;
        }

        .leftlink {
            color: white;
            text-decoration: none;
            text-align: left;
            float: left;
            line-height: 30px;
            padding-left: 5px;
            width: 165px;
            border-bottom: 1px solid #1B213B;
            cursor: pointer
        }

        .leftlinkh {
            background: black;
            color: black;
            text-decoration: none;
            text-align: left;
            display: block;
            border-bottom: 1px solid #1B213B;
            cursor: default
        }

        a, a:active, a:visited {
            outline: none
        }

        .lefttd {
            padding-left: 1px;
            padding-top: 1px;
            padding-bottom: 1px
        }
    </style>
    <script language="Javascript">
        function setFrameWidth() {

            if (window.parent) {
                if (window.parent.document.getElementById('menuFrame').style.width <= "180px") {
                    var frameSet = window.frameElement.parentNode;
                    frameSet.cols = "30px, *";
                    window.parent.document.getElementById('menuFrame').style.width = "30px";
                }
                else {
                    var frameSet = window.frameElement.parentNode;
                    frameSet.cols = "180px, *";
                    window.parent.document.getElementById('menuFrame').style.width = "180px";
                }
                moveArrow();
            }
            return false;
        }
        function moveArrow() {
            if (window.parent) {
                if (window.parent.document.getElementById('menuFrame').style.width <= "180px") {
                    document.getElementById("arrowmax").style.display = "none";
                }
                else {
                    document.getElementById("arrowmax").style.display = "block"
                }
            }
            return false;
        }
        function ChangeCols() {
            var frameSet = window.frameElement.parentNode;
            alert(frameSet.cols);
            frameSet.cols = "5%, *";
        }
    </script>
</HEAD>
<BODY>
<table width="165px" border="0" cellpadding="0" cellspacing="0">
    <tr class=leftlinkh>
        <td class=lefttd valign="middle" colspan="2">
            <a href="./all-users.html" target="_top" class="leftlink"><img src="img/home.gif" alt="Exit"
                                                                           style="border:0px;height:16px;width:16px;">&nbsp;&nbsp;&nbsp;&nbsp;Home</a>
        </td>
    </tr>

    <tr class=leftlinkh>
        <td class=lefttd valign="middle" colspan="2">
            <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=user.getId()%>&cellinkId=<%=cellink.getId()%>&screenId=1"
               target="body" class="leftlink" colspan="2"><img src="img/screen1.gif" alt="Screens" style="border:0px;">&nbsp;&nbsp;&nbsp;&nbsp;Screens
            </a>
        </td>
    </tr>
    <tr class=leftlinkh>
        <td class=lefttd valign="middle" colspan="2">
            <a href="rmctrl-underconstruction.jsp?userId=<%=user.getId()%>&cellinkId=<%=cellink.getId()%>" target="body"
               class="leftlink" colspan="2"><img src="img/support.png" alt="Help"
                                                 style="border:0px;height:16px;width:16px;">&nbsp;&nbsp;&nbsp;&nbsp;Help</a>
        </td>
    </tr>
    <tr>
        <td><img src="img/arrowright.gif" style="cursor: pointer; display: none" onclick="return setFrameWidth();"
                 id="arrowmax" title="Standard Screen Format"></td>
        <td align=right><img src="img/arrowleft.gif" style="cursor: pointer" onclick="return setFrameWidth();"
                             title="Wide Screen Format"></td>
    </tr>
    <tr>
        <td valign=bottom style="padding-left: 30px; padding-bottom: 50px">
            <img width="85px" height="30px" src="img/logolmi.gif" border="0" alt="Agrologic Remote Control Free"
                 style="margin-bottom: 10px">
        </td>
    </tr>
</table>
</BODY>
</HTML>
