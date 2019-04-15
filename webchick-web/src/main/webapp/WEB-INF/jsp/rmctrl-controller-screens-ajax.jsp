<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="../../language.jsp" %>
<%--<%@ page errorPage="../../anerrorpage.jsp" %>--%>
<%@ page import="com.agrologic.app.model.Controller" %>
<%@ page import="com.agrologic.app.model.User" %>
<%@ page import="com.agrologic.app.dao.ControllerDao" %>
<%@ page import="com.agrologic.app.dao.DbImplDecider" %>
<%@ page import="com.agrologic.app.dao.DaoType" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Locale" %>

<jsp:useBean id="userId" scope="request" type="java.lang.Long"/>
<jsp:useBean id="cellink" scope="request" type="com.agrologic.app.model.Cellink"/>
<jsp:useBean id="controllerId" scope="request" type="java.lang.Long"/>
<jsp:useBean id="screenId" scope="request" type="java.lang.Long"/>

<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect("./index.htm");
        return;
    }

    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
    Collection<Controller> controllers = controllerDao.getActiveCellinkControllers(cellinkId);
    Controller controller = getController(controllers, controllerId);
    Locale oldLocal = (Locale) session.getAttribute("oldLocale");
    Locale currLocal = (Locale) session.getAttribute("currLocale");
    if (!oldLocal.equals(currLocal)) {
        response.sendRedirect("./rmctrl-controller-screens-ajax.html?lang=" + lang + "&userId=" + userId + "&cellinkId=" + cellinkId + "&controllerId=" + controllerId + "&screenId=" + screenId);
    }
%>
<%!
    Controller getController(Collection<Controller> controllers, Long controllerId) {
        for (Controller c : controllers) {
            if (c.getId().equals(controllerId)) {
                return c;
            }
        }
        return null;
    }
%>
<html dir="<%=session.getAttribute("dir")%>">
<head>
<title><%=session.getAttribute("all.screen.page.title")%></title>
<link rel="shortcut icon" href="resources/images/favicon.ico">
<link rel="stylesheet" type="text/css" href="resources/style/admincontent.css"/>
<link rel="stylesheet" type="text/css" href="resources/style/tabstyle.css"/>
<link rel="stylesheet" type="text/css" href="resources/style/progressbar.css"/>
<script type="text/javascript" src="resources/javascript/jquery.js">;</script>
<script type="text/javascript">
var doClearOld = true;
var isChanged = false;
var firstload = 1;
var timeoutID;
function getXMLObject() { //XML OBJECT
    var xmlHttp = false;
    try {
        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
    } catch (e) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
        } catch (e2) {
            xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
        }
    }
    if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
        xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
    }
    return xmlHttp;  // Mandatory Statement returning the ajax object created
}
var xmlhttp = new getXMLObject();	//xmlhttp holds the ajax object

function ajaxFunction() {
    var getdate = new Date();  //Used to prevent caching during ajax call
    if (xmlhttp) {
        xmlhttp.open("GET", "RCControllerScreenAjax?getdate=" + getdate.getTime() + "&userId=${userId}&cellinkId=${cellink.id}&controllerId=${controllerId}&screenId=${screenId}", true); //gettime will be the servlet name
        xmlhttp.onreadystatechange = handleServerResponse;
        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xmlhttp.send(null);
    }
    timeoutID = setTimeout("ajaxFunction()", 5000);
}

var uninitialized = 0;
var loading = 1;
var loaded = 2;
var interactive = 3;
var complete = 4;

function handleServerResponse() {
    var state = xmlhttp.readyState;
    var tableDataDiv = document.getElementById("tableData");
    switch (state) {
        case complete:
            if (xmlhttp.status == 200) {
                document.getElementById("loading").style.display = "none";
                var tableDataDiv = document.getElementById("tableData");
                tableDataDiv.innerHTML = xmlhttp.responseText;
                clearTimeout(timeoutID);
                timeoutID = window.setTimeout("ajaxFunction();", 10000);
            } else {
                var tableDataDiv = document.getElementById("tableData");
                var innerHTML = "<table class=\"infoMsg\"><tr><td><p>Loading please wait...</p></td></tr></table>"
                tableDataDiv.innerHTML = innerHTML;
            }
            break;
        case interactive:
            //var innerHTML = "<table class=\"infoMsg\"><tr><td><p>Loading please wait...</p></td></tr></table>"
            //tableDataDiv.innerHTML = innerHTML;
            break;

        case loaded:
            //var innerHTML = "<table class=\"infoMsg\"><tr><td><p>Loading please wait...</p></td></tr></table>"
            //tableDataDiv.innerHTML = innerHTML;//
            break;

        case loading:
            if (firstload == 1) {
                firstload = 0;
                document.getElementById("loading").style.display = "block";
            }

            break;

        case uninitialized:
            //document.getElementById("loading").style.display = "block";
            break;
    }
}

function blockAjax() {
    clearTimeout(timeoutID)
    timeoutID = setTimeout("ajaxFunction()", 30000);
}

function unblockAjax() {
    clearTimeout(timeoutID)
    timeoutID = setTimeout("ajaxFunction()", 10000);
}

function setAutoLoad() {
    setTimeout("ajaxFunction();", 0);
}

function getStatus(text) {
    var length = 12;
    var status = text.substr(0, length);
    if (status.charAt('8') == '1') {
        return true;
    }
    return false;
}

function wait(msecs) {
    var start = new Date().getTime();
    var cur = start
    while (cur - start < msecs) {
        cur = new Date().getTime();
    }
}

var DEC_0 = 0;
var DEC_1 = 1;
var DEC_2 = 2;
var DEC_3 = 3;
var HUMIDITY = 4
var TIME = 5;
var TIME_SEC = 6;
var DATE = 7;
var DEC_4 = 8;
var DEC_5 = 10;
var DEC_11 = 11;

var EMPTY_DELIM = "";
var DOT_DELIM = ".";
var TIME_DELIM = ":";
var DATE_DELIM = "/";

var DOT_CODE_1 = 190;
var DOT_CODE_2 = 110;
var TIME_CODE = 186;
var DATE_CODE_1 = 111;
var DATE_CODE_2 = 191;

var formats = new Array();
formats[DEC_0] = "0";
formats[DEC_1] = "0.0";
formats[DEC_2] = "0.00";
formats[DEC_3] = "0.000";
formats[HUMIDITY] = "0";
formats[TIME] = "00.00";
formats[TIME_SEC] = "0";
formats[DATE] = "00.00";
formats[DEC_4] = "0";
formats[DEC_5] = "0";
formats[DEC_11] ="000.0";
/*
 ** Returns the caret (cursor) position of the specified text field.
 ** Return value range is 0-oField.length.
 */
function doGetCaretPosition(oField) {
    // Initialize
    var iCaretPos = 0;
    // IE Support
    if (document.selection) {
        // Set focus on the element
        oField.focus();
        // To get cursor position, get empty selection range
        var oSel = document.selection.createRange();
        // Move selection start to 0 position
        oSel.moveStart('character', -oField.value.length);
        // The caret position is selection length
        iCaretPos = oSel.text.length;
    } else // Firefox support
    if (oField.selectionStart || oField.selectionStart == '0') {
        iCaretPos = oField.selectionStart;
    }

    // Return results
    return (iCaretPos);
}
/*
 * Sets the caret (cursor) position of the specified text field.
 * Valid positions are 0-oField.length.
 */
function doSetCaretPosition(oField, iCaretPos) {

    // IE Support
    if (document.selection) {
        // Set focus on the element
        oField.focus();
        // Create empty selection range
        var oSel = document.selection.createRange();
        // Move selection start and end to 0 position
        oSel.moveStart('character', -oField.value.length);
        // Move selection start and end to desired position
        oSel.moveStart('character', iCaretPos);
        oSel.moveEnd('character', 0);
        oSel.select();
    } else// Firefox support
    if (oField.selectionStart || oField.selectionStart == '0') {
        oField.selectionStart = iCaretPos;
        oField.selectionEnd = iCaretPos;
        oField.focus();
    }
}
/**
 *
 */
function IsNumeric(sText) {
    var validChars = "0123456789.:/";
    for (i = 0; i < sText.length; i++) {
        var ch = sText.charAt(i);
        if (validChars.indexOf(ch) == -1) {
            return false;
        }
    }
    return true;
}
/**
 *
 */
function validate() {
    var valid = true;
    var val = document.editForm.Nvalue.value;
    if (val == "") {
        document.getElementById("msgValue").innerHTML = "<%=session.getAttribute("edit.value.empty.message")%>";
        document.getElementById("msgValue").style.color = "RED";
        document.editForm.Nvalue.focus();
        valid = false;
    }
    if (IsNumeric(val) == false) {
        document.getElementById("msgValue").innerHTML = "<%=session.getAttribute("edit.value.error.message")%>";
        document.getElementById("msgValue").style.color = "RED";
        document.editForm.Nvalue.focus();
        valid = false;
    }

    if (!valid) {
        return false;
    } else {
        self.close();
    }
}

/**
 *
 */
function checkField(event, val, type) {
    if (event.keyCode == 13) {
        return;
    }

    if (val.value == "") {
        return;
    }

    if (IsNumeric(val.value) == false) {
        if (!isChanged) {
            var pos = val.value.length;
            var head = val.value.substring(0, pos - 2);
            var tail = val.value.substring(pos - 1, pos);
            val.value = head + tail;
            doSetCaretPosition(val, val.value.lenght - 1)
        } else {
            var pos = val.value.length;
            var head = val.value.substring(0, pos - 1);
            val.value = head;
        }
    }
    doDecPoint(event, val, type);
}

/**
 *
 */
function doDecPoint(event, val, type) {
    var txt = "";
    if (keyCode == 16) {
        return;
    }
    var keyCode = event.keyCode;
    if (keyCode >= 96) {// if input numers from left keyboard numbers
        keyCode = event.keyCode - 96;
    } else {// if input numers from top keyboard numbers
        keyCode = event.keyCode - 48;
    }
    if (!isChanged) {
        val.value = formats[type];
        var $caretpos = doGetCaretPosition(val);
        for (var i = 0; i <= val.value.length; i++) {
            var ch = val.value.charAt(i);
            if (ch.search('[0-9.:/]') != -1 || ch != " ") {
                if (i == $caretpos - 1) {
                    txt = txt + keyCode;
                    break;
                } else {
                    txt = txt + ch;
                }
            }
        }
        val.value = getFixFormat(txt, type, isChanged)
        isChanged = true;
    } else {
        var ch = event.keyCode;
        if (event.keyCode == DOT_CODE_1 ||
                event.keyCode == DOT_CODE_2 ||
                event.keyCode == TIME_CODE ||
                event.keyCode == DATE_CODE_1 ||
                event.keyCode == DATE_CODE_2) {

            val.value = val.value.substring(0, val.value.length - 1);
        } else {
            val.value = getFixFormat(val.value, type, isChanged);
        }
    }
}

/**
 *
 */
function getFixFormat(txt, t, changed) {
    var type = parseInt(t);
    switch (type) {
        case DEC_0:
            if (!changed) {
                var num = parseInt(txt);
                var result = num.toFixed(0);
                return result;
            } else {
                var num = parseInt(txt);
                if (num > 9999) {
                    var d = parseInt(num / 10000);
                    num = num - (d * 10000);
                }
                var result = num.toString();
                return result;
            }
            break;
        case DEC_1:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(DEC_1);
                return result;
            } else {
                var num = parseFloat(txt);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                var result = num.toFixed(DEC_1);
                return result;
            }
            break;
        case DEC_2:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(DEC_2);
                return result;
            } else {
                var num = parseFloat(txt);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                var result = num.toFixed(DEC_2);
                return result;
            }
            break;
        case DEC_3:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(DEC_3);
                return result;
            } else {
                var num = parseFloat(txt);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                var result = num.toFixed(DEC_3);
                return result;
            }
            break;
        case HUMIDITY:
            if (!changed) {
                var num = parseInt(txt);
                var result = num.toFixed(0);
                return result;
            } else {
                var num = parseInt(txt);
                if (num > 999) {
                    var d = parseInt(num / 1000);
                    num = num - (d * 1000);
                }
                var result = num.toString();
                return result;
            }
            break;
        case TIME:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(2);
                result = result.replace(DOT_DELIM, TIME_DELIM);
                return result;
            } else {
                var result = txt.replace(TIME_DELIM, DOT_DELIM);
                var num = parseFloat(result);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                result = num.toFixed(2);
                result = result.replace(DOT_DELIM, TIME_DELIM);
                return result;
            }
            break;
        case DATE:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(2);
                result = result.replace(DOT_DELIM, DATE_DELIM);
                return result;
            } else {
                var result = txt.replace(DATE_DELIM, DOT_DELIM);
                var num = parseFloat(result);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                result = num.toFixed(2);
                result = result.replace(DOT_DELIM, DATE_DELIM);
                return result;
            }
            break;
        case DEC_4:
            if (!changed) {
                var num = parseInt(txt);
                var result = num.toFixed(0);
                return result;
            } else {
                var num = parseInt(txt);
                if (num > 999999) {
                    var d = parseInt(num / 1000000);
                    num = num - (d * 1000000);
                }
                var result = num.toString();
                return result;
            }
            break;
        case DEC_5:
            if (!changed) {
                var num = parseInt(txt);
                var result = num.toFixed(0);
                return result;
            } else {
                var num = parseInt(txt);
                if (num > 99999) {
                    var d = parseInt(num / 100000);
                    num = num - (d * 100000);
                }
                var result = num.toString();
                return result;
            }
            break;
        case DEC_11:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(1);
                return result;
            } else {
                var num = parseFloat(txt);
                num = num * 10;
                if (num > 999.9) {
                    var d = parseInt(num / 1000);
                    num = num - (d * 1000);
                }
                var result = num.toFixed(1);
                return result;
            }
            break;
    }
}
function closeWindow() {
    self.close();
    window.opener.location.reload(true);
}
function keyDown(val) {
    if (doClearOld) {
        val.value = "";
        doClearOld = false;
    }
}

/**
 * Open popup to show alarm list foreach alarm type
 * @param el the name of element
 * @param event the mouse event
 */
function openPopup(el, event) {
    $('.popup').hide();
    event.preventDefault();
    $('#' + el).css( 'position', 'absolute' );
    $('#' + el).css( 'top', event.pageY );
    $('#' + el).css( 'left', event.pageX );
    $('#' + el).fadeIn(100);
    blockAjax()
}
/**
 * Close popup
 */
function closePopup() {
    $('.popup').fadeOut(100);
    unblockAjax();
}

</script>
<script type="text/javascript" src="resources/javascript/fhelp.js"></script>
</head>
<body onload="setAutoLoad()">
<div id="loading"></div>
<table width="100%">
    <tr>
        <td align="center">
            <fieldset style="-moz-border-radius:8px;  border-radius: 8px;  -webkit-border-radius: 8px;">
                <table border="0" cellPadding=1 cellSpacing=1 width="100%">
                    <tr>
                        <td align="center">
                            <h2><%=controller.getTitle()%>
                            </h2>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <%@include file="../../toplang.jsp" %>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
    </tr>
    <tr>
        <td style="text-align: center;">
            <form name="mainForm" action="./rmctrl-controller-screens-ajax.html" method="post">
                <fieldset style="-moz-border-radius:15px;  border-radius: 15px;  -webkit-border-radius: 15px;">
                    <div id="tableData">

                    </div>
                </fieldset>
            </form>
        </td>
    </tr>
</table>
</body>
</html>