<%--
    Document   : rmctrl-flock-management
    Created on : Mar 31, 2011, 11:44:21 AM
    Author     : JanL
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.ControllerDto" %>
<%@ include file="disableCaching.jsp" %>

<jsp:directive.page import="com.agrologic.app.model.FlockDto"/>
<jsp:directive.page import="java.util.List"/>
<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    List<ControllerDto> controllers = (List<ControllerDto>) request.getSession().getAttribute("controllers");
    ControllerDto editController = getController(controllers, controllerId);
    FlockDto editFlock = getFlock(controllers, controllerId, flockId);
%>
<%! FlockDto getFlock(List<ControllerDto> controllers, Long controllerId, Long flockId) {
    for (ControllerDto c : controllers) {
        if (c.getId() == controllerId) {
            for (FlockDto f : c.getFlocks()) {
                if (f.getFlockId() == flockId) {
                    return f;
                }
            }
        }
    }
    return null;
}

    ControllerDto getController(List<ControllerDto> controllers, Long controllerId) {
        for (ControllerDto c : controllers) {
            if (c.getId().equals(controllerId)) {
                return c;
            }
        }
        return null;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="ltr">
<head>
<title>Flock Manager</title>
<link rel="StyleSheet" type="text/css" href="css/tabView.css">
<link rel="StyleSheet" type="text/css" href="css/admincontent.css">
<link rel="stylesheet" type="text/css" href="css/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/tabView.js"></script>
<script type="text/javascript">
var isChanged = false;
var DEC_0 = 0;
var DEC_1 = 1;

var DOT_CODE_1 = 190;
var DOT_CODE_2 = 110;

var formats = new Array();
formats[DEC_0] = "0";
formats[DEC_1] = "00.00";
/**
 * Returns the caret (cursor) position of the specified text field.
 * Return value range is 0-oField.length.
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
/**
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
function IsNumeric(sText) {
    var validChars = "0123456789.";
    for (i = 0; i < sText.length; i++) {
        var ch = sText.charAt(i);
        if (validChars.indexOf(ch) == -1) {
            return false;
        }
    }
    return true;
}
function checkField(event, val, type) {
    if (IsNumeric(val.value) == false) {
        if (!isChanged) {
            var pos = val.value.length;
            var head = val.value.substring(0, pos - 2);
            var tail = val.value.substring(pos - 1, pos);
            val.value = head + tail;
            doSetCaretPosition(val, val.value.lenght - 1)
            return;
        } else {
            var pos = val.value.length;
            var head = val.value.substring(0, pos - 1);
            val.value = head;
            return;
        }
    }
    doDecPoint(event, val, type);
}
function doDecPoint(event, val, type) {
    var txt = "";
    var keyCode = event.keyCode;
    if (keyCode == 16) {
        return;
    }

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
            if (ch.search('[0-9.]') != -1 || ch != " ") {
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
        if (event.keyCode == DOT_CODE_1 || event.keyCode == DOT_CODE_2) {
            val.value = val.value.substring(0, val.value.length - 1);
        } else {
            val.value = getFixFormat(val.value, type, isChanged);
        }
    }
}
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
                if (num > 999999) {
                    var d = parseInt(num / 1000000);
                    num = num - (d * 1000000);
                }
                var result = num.toString();
                return result;
            }
            break;
        case DEC_1:
            if (!changed) {
                var num = parseFloat(txt);
                var result = num.toFixed(2);
                return result;
            } else {
                var result = txt;
                var num = parseFloat(result);
                num = num * 10;
                if (num > 99.9) {
                    var d = parseInt(num / 100);
                    num = num - (d * 100);
                }
                result = num.toFixed(2);
                return result;
            }
            break;
    }
}
function calcTotalChicks() {
    // calculate quantity
    var totalQuant = 0;
    var maleQ = document.getElementById("maleQuant");
    if (maleQ.value == "") {
        maleQ.value = "0";
    }
    var maleQuant = parseInt(maleQ.value);

    var femaleQ = document.getElementById("femaleQuant");
    if (femaleQ.value == "") {
        femaleQ.value = "0";
    }
    var femaleQuant = parseInt(femaleQ.value);
    totalQuant = maleQuant + femaleQuant
    document.getElementById("totalQuant").value = totalQuant;
    // calculate cost
    var totalQuantCost = 0.0;
    var maleC = document.getElementById("maleCost");
    if (maleC.value == "") {
        maleC.value = "0.0";
    }
    var maleCost = parseFloat(maleC.value);
    var maleQ = document.getElementById("maleQuant").value;
    var maleQuant = parseFloat(maleQ);

    var femaleC = document.getElementById("femaleCost");
    if (femaleC.value == "") {
        femaleC.value = "0.0";
    }

    var femaleCost = parseFloat(femaleC.value);
    var femaleQ = document.getElementById("femaleQuant").value;
    var femaleQuant = parseFloat(femaleQ);

    totalQuantCost = maleCost * maleQuant + femaleCost * femaleQuant;
    document.getElementById("totalQuantCost").value = totalQuantCost;
}
function calcTotalEnergyAmount(begin, end, total) {
    var totalAmount = 0;
    var begin = document.getElementById(begin);
    if (begin.value == "") {
        begin.value = "0";
    }
    var begin = parseInt(begin.value);

    var end = document.getElementById(end);
    if (end.value == "") {
        end.value = "0";
    }
    var end = parseInt(end.value);
    totalAmount = begin - end
    document.getElementById(total).value = totalAmount;
}
function calcTotalAmount(begin, end, total) {
    var totalAmount = 0;
    var beginNum = document.getElementById(begin);
    if (beginNum.value == "") {
        beginNum.value = "0";
    }
    var beginNumber = parseInt(beginNum.value);


    var endN = document.getElementById(end);
    if (endN.value == "") {
        endN.value = "0";
    }
    var endNumber = parseInt(endN.value);
    totalAmount = (endNumber - beginNumber);
    document.getElementById(total).value = totalAmount;
}
function calcTotalMeterCost(begin, end, cost, totalCost) {
    // calculate cost
    var totalC = 0.0;
    var beginM = document.getElementById(begin);

    if (beginM.value == "") {
        beginM.value = "0";
    }
    var beginMeter = parseInt(beginM.value);

    var endM = document.getElementById(end);
    if (endM.value == "") {
        endM.value = "0";
    }
    var endMeter = parseInt(endM.value);

    var costM = document.getElementById(cost);
    var costMeter = parseFloat(costM.value);
    totalC = (endMeter - beginMeter) * costMeter;
    totalC = totalC.toFixed(2);
    document.getElementById(totalCost).value = totalC;
}
function calcTotalCost(begin, beginCost, end, endCost, totalCost) {
    // calculate cost
    var totalC = 0.0;
    var beginA = document.getElementById(begin);
    if (beginA.value == "") {
        beginA.value = "0.0";
    }
    var beginAmount = parseFloat(beginA.value);
    var beginC = document.getElementById(beginCost).value;
    var beginCost = parseFloat(beginC);

    var endA = document.getElementById(end);
    if (endA.value == "") {
        endA.value = "0.0";
    }
    var endAmount = parseFloat(endA.value);
    var endC = document.getElementById(endCost).value;
    var endCost = parseFloat(endC);

    totalC = beginCost * beginAmount - endCost * endAmount;
    totalC = totalC.toFixed(2);
    document.getElementById(totalCost).value = totalC;
}
</script>
</head>
<body>
<table width="100%">
<tr>
    <td align="center">
        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
            <table width="85%" border="0">
                <tr>
                    <td width="20%">
                        <%@include file="toplang.jsp" %>
                    </td>
                    <td align="center">
                        <h1 style="text-align: center;"><%=session.getAttribute("flock.page.title")%>
                        </h1>
                    </td>
                    <td width="20%">
                        <a href="./rmctrl-main-screen-ajax.jsp?userId=<%=userId%>&cellinkId=<%=cellinkId%>&screenId=1&doResetTimeout=true">
                            <img src="img/display.png" style="cursor: pointer" border="0"/>
                            &nbsp;<%=session.getAttribute("button.screens")%>&nbsp;
                        </a>
                        <a href="flocks.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>">
                            <img src="img/chicken-icon.png" style="cursor: pointer" border="0"/>
                            <%=session.getAttribute("main.screen.page.flocks")%>
                        </a>
                    </td>
                </tr>
            </table>
        </fieldset>
    </td>
</tr>
<tr>
<td align="center">
<fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px; width: 85%">
<div class="TabView" id="TabView" style="text-align: left;">
<!-- *** Tabs ************************************************************** -->
<div class="Tabs" style="width: 85%">
    <a>Houses</a>
    <a>Begin&End</a>
    <a>Expanses</a>
    <a>Distribute</a>
    <a>Summary</a>
</div>
<!-- *** Pages ************************************************************* -->
<div class="Pages" style="width: 100%; height: 800px;">
<!-- *** Page0 Start *** -->
<div class="Page">
    <div class="Pad">
        <br>
        <br>
        <table>
            <tr>
                <td valign="top">
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend> Houses parameters</legend>
                        <form action="./save-houses.html" method="post">
                            <input type="hidden" name="userId" value="<%=userId%>">
                            <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
                            <input type="hidden" name="controllerId" value="<%=controllerId%>">
                            <input type="hidden" name="flockId" value="<%=flockId%>">
                            <%
                                Integer area = editController.getArea();
                            %>
                            <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                                <tr>
                                    <th nowrap>Area</th>
                                    <td align="center">
                                        <input type="text" id="area" name="area" value="<%=area%>">
                                    </td>
                                </tr>
                                <tr>
                                    <th nowrap>Currency</th>
                                    <td>
                                        <select id="currency" name="currency">
                                            <option value="1">Dollar - $</option>
                                            <option value="2">&#8362; - שקל</option>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <input type="submit" value="Save">
                        </form>
                    </fieldset>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- *** Page0 End ***** -->
<!-- *** Page1 Start *** -->
<div class="Page">
<div class="Pad">
<table>
<tr>
    <td valign="top">
        <table>
            <tr>
                <td valign="top">
                    <br><br>
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend> Energy</legend>
                        <p>You can insert gas amount and cost and <br>
                            get total amount usage and total cost.</p>
                        <table width="100%">
                            <tr>
                                <td colspan="2" align="center">
                                    <button style="width: 80px"
                                            onclick="window.open('./rmctrl-add-gas.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes')">
                                        Add Gas
                                    </button>
                                </td>
                                <td colspan="2" align="center">
                                    <button style="width: 80px"
                                            onclick="window.open('./rmctrl-add-fuel.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes')">
                                        Add Fuel
                                    </button>
                                </td>
                            </tr>
                            <table>
                                <form name="energyForm" action="./save-energy.html" method="post">
                                    <input type="hidden" name="userId" value="<%=userId%>">
                                    <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
                                    <input type="hidden" name="controllerId" value="<%=controllerId%>">
                                    <input type="hidden" name="flockId" value="<%=flockId%>">
                                    <input type="hidden" name="currency" value="$">
                                    <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
                                        <tr>
                                            <th width="auto"></th>
                                            <th colspan="2" align="center">Gas</th>
                                            <th colspan="2" align="center">Fuel</th>
                                        </tr>
                                        <tr>
                                            <th width="auto"></th>
                                            <th> Amount</th>
                                            <th> Price</th>
                                            <th> Amount</th>
                                            <th> Price</th>
                                        </tr>
                                        <tr>
                                            <th>Start</th>
                                            <td>
                                                <input size="10" type="text" id="beginGas" name="beginGas"
                                                       value="<%=editFlock.getGasBegin() %>"
                                                       onkeyup="return checkField(event,this,0)"
                                                       onblur="javascript:calcTotalEnergyAmount('beginGas','endGas','totalGas')">
                                            </td>
                                            <td>
                                                <input size="6" type="text" id="beginCostGas" name="beginCostGas"
                                                       value="<%=editFlock.getCostGas() %>"
                                                       onkeyup="return checkField(event,this,1)"
                                                       onblur="javascript:calcTotalCost('beginGas','beginCostGas','endGas','endCostGas','totalCostGas')">
                                            </td>
                                            <td>
                                                <input size="10" type="text" id="beginFuel" name="beginFuel"
                                                       value="<%=editFlock.getFuelBegin() %>"
                                                       onkeyup="return checkField(event,this,0)"
                                                       onblur="javascript:calcTotalEnergyAmount('beginFuel','endFuel','totalFuel')">
                                            </td>
                                            <td>
                                                <input size="6" type="text" id="beginCostFuel" name="beginCostFuel"
                                                       value="<%=editFlock.getCostFuel() %>"
                                                       onkeyup="return checkField(event,this,1)"
                                                       onblur="javascript:calcTotalCost('beginFuel','beginCostFuel','endFuel','endCostFuel','totalCostFuel')">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Left</th>
                                            <td>
                                                <input size="10" type="text" id="endGas" name="endGas"
                                                       value="<%=editFlock.getGasEnd() %>"
                                                       onkeyup="return checkField(event,this,0)"
                                                       onblur="javascript:calcTotalEnergyAmount('beginGas','endGas','totalGas')">
                                            </td>
                                            <td>
                                                <input size="6" type="text" id="endCostGas" name="endCostGas"
                                                       value="<%=editFlock.getCostGasEnd() %>"
                                                       onkeyup="return checkField(event,this,1)"
                                                       onblur="javascript:calcTotalCost('beginGas','beginCostGas','endGas','endCostGas','totalCostGas')">
                                            </td>
                                            <td>
                                                <input size="10" type="text" id="endFuel" name="endFuel"
                                                       value="<%=editFlock.getFuelEnd() %>"
                                                       onkeyup="return checkField(event,this,0)"
                                                       onblur="javascript:calcTotalEnergyAmount('beginFuel','endFuel','totalFuel')">
                                            </td>
                                            <td>
                                                <input size="6" type="text" id="endCostFuel" name="endCostFuel"
                                                       value="<%=editFlock.getCostFuelEnd() %>"
                                                       onkeyup="return checkField(event,this,1)"
                                                       onblur="javascript:calcTotalCost('beginFuel','beginCostFuel','endFuel','endCostFuel','totalCostFuel')">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th width="auto">Total</th>
                                            <td><input size="10" type="text" readonly id="totalGas" id="totalGas"
                                                       name="totalGas" value="<%=editFlock.calcTotalQuantityGas() %>">
                                            </td>
                                            <td><input size="10" type="text" readonly id="totalCostGas"
                                                       id="totalCostGas" name="totalCostGas"
                                                       value="<%=editFlock.getTotalGas()%>$"></td>
                                            <td><input size="10" type="text" readonly id="totalFuel" id="totalFuel"
                                                       name="totalFuel" value="<%=editFlock.calcTotalQuantityFuel() %>">
                                            </td>
                                            <td><input size="10" type="text" readonly id="totalCostFuel"
                                                       name="totalCostFuel" value="<%=editFlock.getTotalFuel()%>$"></td>
                                        </tr>
                                    </table>
                                    <br>
                                    <input type="submit" value="Save">
                                </form>
                    </fieldset>
                </td>
                <td valign="top">
                    <br><br>
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend>Chick quantity</legend>
                        <p>You can insert amount of chicks and cost and <br>
                            get total amount and total cost.</p>

                        <form name="chickForm" action="./save-begin-end.html" method="post">
                            <input type="hidden" name="userId" value="<%=userId%>">
                            <input type="hidden" name="cellinkId" value="<%=cellinkId%>">
                            <input type="hidden" name="controllerId" value="<%=controllerId%>">
                            <input type="hidden" name="flockId" value="<%=flockId%>">
                            <input type="hidden" name="currency" value="$">
                            <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
                                <th width="auto"></th>
                                <th width="auto">Quantity</th>
                                <th width="auto">Cost</th>
                                <tr>
                                    <td bgcolor="#D5EFFF">Male</td>
                                    <td><input type="text" id="maleQuant" name="maleQuant"
                                               value="<%=editFlock.getQuantityMale() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalChicks()"></td>
                                    <td><input type="text" id="maleCost" name="maleCost"
                                               value="<%=editFlock.getCostChickMale() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalChicks()">
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#D5EFFF">Female</td>
                                    <td><input type="text" id="femaleQuant" name="femaleQuant"
                                               value="<%=editFlock.getQuantityFemale() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalChicks()"></td>
                                    <td><input type="text" id="femaleCost" name="femaleCost"
                                               value="<%=editFlock.getCostChickFemale() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalChicks()">
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#D5EFFF">Total</td>
                                    <td><input type="text" id="totalQuant" name="totalQuant" readonly
                                               value="<%=editFlock.getQuantityChicks() %>"></td>
                                    <td><input type="text" id="totalQuantCost" name="totalQuantCost" readonly
                                               value="<%=editFlock.calcTotalChicksCost() %>$"></td>
                                </tr>
                            </table>
                            <br>
                            <input type="submit" value="Save">
                        </form>
                    </fieldset>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td valign="top">
        <table>
            <tr>
                <td valign="top">
                    <form action="./save-meter.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>"
                          method="post">
                        <br><br>
                        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                            <legend> Meter data</legend>
                            <p>You can insert start and end meter data and cost and<br>
                                get total amount usage and total cost.</p>
                            <table>
                                <tr>
                                    <td>
                                        <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
                                            <tr>
                                                <th width="auto" colspan="3" align="center">Electricity</th>
                                            </tr>
                                            <tr>
                                                <th width="auto">Start</th>
                                                <th width="auto">End</th>
                                                <th width="auto">Price</th>
                                            </tr>
                                            <tr>
                                                <td><input type="text" size="10" id="startElectMeter"
                                                           name="startElectMeter"
                                                           value="<%=editFlock.getElectBegin() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                           calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                                <td><input type="text" size="10" id="endElectMeter" name="endElectMeter"
                                                           value="<%=editFlock.getElectEnd() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                           calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                                <td><input type="text" size="10" id="priceElectMeter"
                                                           name="priceElectMeter" value="<%=editFlock.getCostElect() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                           calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Total Electricity</th>
                                                <td><input type="text" id="totElect"
                                                           value="<%=editFlock.getQuantityElect() %>" size="10"
                                                           readonly></td>
                                            </tr>
                                            <tr>
                                                <th>Total Sum</th>
                                                <td><input type="text" id="totElectCost"
                                                           value="<%=editFlock.getTotalElect() %>" size="10" readonly>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table border="1" style="border:1px solid #C6C6C6; border-collapse: collapse;">
                                            <tr>
                                                <th width="auto" colspan="3" align="center">Water</th>
                                            </tr>
                                            <tr>
                                                <th width="auto">Start</th>
                                                <th width="auto">End</th>
                                                <th width="auto">Price</th>
                                            </tr>
                                            <tr>
                                                <td><input type="text" size="10" id="startWaterMeter"
                                                           name="startWaterMeter"
                                                           value="<%=editFlock.getWaterBegin() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                               calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                                <td><input type="text" size="10" id="endWaterMeter" name="endWaterMeter"
                                                           value="<%=editFlock.getWaterEnd() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                               calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                                <td><input type="text" size="10" id="priceWaterMeter"
                                                           name="priceWaterMeter" value="<%=editFlock.getCostWater() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                               calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Total Water</th>
                                                <td><input type="text" id="totWater"
                                                           value="<%=editFlock.getQuantityWater() %>" size="10"
                                                           readonly></td>
                                            </tr>
                                            <tr>
                                                <th>Total Sum</th>
                                                <td><input type="text" id="totWaterCost" size="10"
                                                           value="<%=editFlock.getTotalWater() %>" size="10" readonly>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <input type="submit" value="Save">
                        </fieldset>
                    </form>
                </td>
                <td>
                <td valign="top">
                    <form action="./save-info.html?userId=<%=userId%>&cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>"
                          method="post">
                        <br><br>
                        <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                            <legend>Information</legend>
                            <p>You can insert information here </p>
                            <table>
                                <tr>
                                    <td>Hathery </td>
                                    <td><input style="width: 120px;" type="text" name="hathery"/></td>
                                </tr>
                                <tr>
                                    <td>Breder  </td>
                                    <td><input style="width: 120px;" type="text" name="breder"/></td>
                                </tr>
                            </table>
                            <br>
                            <button>Save</button>
                        </fieldset>
                    </form>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</div>
</div>
<!-- *** Page1 End ***** -->
<!-- *** Page2 Start *** -->
<div class="Page">
    <div class="Pad">
        <br>

        <p></p>
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
                                <td align="center"><input type="text" readonly value="<%=editFlock.getSpreadAdd() %>">
                                </td>
                            </tr>
                            <tr>
                                <th>Total Sum</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalSpread() %>">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </fieldset>
                </td>
                <td valign="top">
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend> Feed</legend>
                        <p>
                               <span>
                                   <button style="width: 80px"
                                           onclick="javascript:void(window.open('./rmctrl-add-feed.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=300,scrollbars=yes, resizable=yes'))">
                                       Add
                                   </button>
                               </span>
                        </p>
                        <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                            <tr>
                                <th nowrap>Total Feed</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getFeedAdd() %>">
                                </td>
                            </tr>
                            <tr>
                                <th>Total Expances</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalFeed() %>">
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
                                <td><input type="text" value="<%=editFlock.getTotalMedic() %>"></td>
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
                                <td><input type="text" value="<%=editFlock.getTotalLabor() %>"></td>
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
                                <td><input type="text" readonly value="<%=editFlock.getExpenses() %>"></td>
                                <td><input type="text" readonly value="<%=editFlock.getRevenues() %>"></td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
        <br>

        <p>
    </div>
</div>
<!-- *** Page2 End ***** -->
<!-- *** Page3 Start *** -->
<div class="Page">
    <div class="Pad">
        <br>
        <br>
        <table>
            <tr>
                <td valign="top">
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend> Total Distribution</legend>
                        <p>
                               <span>
                                   <button style="width: 80px"
                                           onclick="javascript:void(window.open('./rmctrl-add-distribute.jsp?cellinkId=<%=cellinkId%>&controllerId=<%=controllerId%>&flockId=<%=flockId%>', 'mywindow','width=850,height=500,scrollbars=yes, resizable=yes'))">
                                       Add
                                   </button>
                               </span>
                        </p>
                        <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                            <tr>
                                <th nowrap>Total Slaughter Male</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getFeedAdd() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Total Slaughter Female</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalFeed() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Total Slaughter Birds</th>
                                <td align="center"><input type="text" readonly></td>
                            </tr>
                            <tr>
                                <th nowrap>Total Weight</th>
                                <td align="center"><input type="text" readonly></td>
                            </tr>
                            <tr>
                                <th nowrap>Total Revenues</th>
                                <td align="center"><input type="text" readonly></td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- *** Page3 End ***** -->
<!-- *** Page4 Start *** -->
<div class="Page">
    <div class="Pad">
        <br>
        <br>
        <table>
            <tr>
                <td valign="top">
                    <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                        <legend> Summary</legend>
                        <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                            <tr>
                                <th nowrap>Chicks</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalChicks() %>">
                                </td>
                                <th nowrap>Spread</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalSpread() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Electricity</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalElect() %>">
                                </td>
                                <th nowrap>Feed</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalFeed() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Water</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalWater() %>">
                                </td>
                                <th nowrap>Medicine</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalMedic() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Disel</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalFuel() %>">
                                </td>
                                <th nowrap>Labor</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalLabor() %>">
                                </td>
                            </tr>
                            <tr>
                                <th nowrap>Gas</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getTotalGas() %>">
                                </td>
                                <th nowrap>Transaction</th>
                                <td align="center"><input type="text" readonly value="<%=editFlock.getExpenses() %>">
                                </td>
                            </tr>
                        </table>
                        <hr>
                        <table>
                            <tr>
                                <td>Total Revenues</td>
                                <td><input type="text" readonly value="<%=editFlock.calcTotalRevenues() %>"></td>
                            </tr>
                            <tr>
                                <td>Total Expenses</td>
                                <td><input type="text" readonly value="<%=editFlock.calcTotalExpenses() %>"></td>
                            </tr>
                            <tr>
                                <td>Cost per kg</td>
                                <td><input type="text" readonly value="<%=editFlock.calcTotCostPerKGBirds()%>"></td>
                            </tr>
                            <tr>
                                <td>Feed conversion</td>
                                <td><input type="text" readonly value=""></td>
                            </tr>
                            <tr>
                                <td>Total</td>
                                <td><input type="text" readonly value=""></td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- *** Page4 End ***** -->
</div>
</div>
</fieldset>
</td>
</tr>
</table>
<script type="text/javascript">
    tabview_initialize('TabView');
</script>
</body>
</html>

