<%@ page import="com.agrologic.app.model.Flock" %>
<%
    Long userId = Long.parseLong(request.getParameter("userId"));
    Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long flockId = Long.parseLong(request.getParameter("flockId"));
    Flock beginFlock = (Flock) session.getAttribute("flock");
%>
<!DOCTYPE html>
<html>
<head>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css">
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
                        </table>
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
                                               value="<%=beginFlock.getGasBegin() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalEnergyAmount('beginGas','endGas','totalGas')">
                                    </td>
                                    <td>
                                        <input size="6" type="text" id="beginCostGas" name="beginCostGas"
                                               value="<%=beginFlock.getCostGas() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalCost('beginGas','beginCostGas','endGas','endCostGas','totalCostGas')">
                                    </td>
                                    <td>
                                        <input size="10" type="text" id="beginFuel" name="beginFuel"
                                               value="<%=beginFlock.getFuelBegin() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalEnergyAmount('beginFuel','endFuel','totalFuel')">
                                    </td>
                                    <td>
                                        <input size="6" type="text" id="beginCostFuel" name="beginCostFuel"
                                               value="<%=beginFlock.getCostFuel() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalCost('beginFuel','beginCostFuel','endFuel','endCostFuel','totalCostFuel')">
                                    </td>
                                </tr>
                                <tr>
                                    <th>Left</th>
                                    <td>
                                        <input size="10" type="text" id="endGas" name="endGas"
                                               value="<%=beginFlock.getGasEnd() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalEnergyAmount('beginGas','endGas','totalGas')">
                                    </td>
                                    <td>
                                        <input size="6" type="text" id="endCostGas" name="endCostGas"
                                               value="<%=beginFlock.getCostGasEnd() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalCost('beginGas','beginCostGas','endGas','endCostGas','totalCostGas')">
                                    </td>
                                    <td>
                                        <input size="10" type="text" id="endFuel" name="endFuel"
                                               value="<%=beginFlock.getFuelEnd() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalEnergyAmount('beginFuel','endFuel','totalFuel')">
                                    </td>
                                    <td>
                                        <input size="6" type="text" id="endCostFuel" name="endCostFuel"
                                               value="<%=beginFlock.getCostFuelEnd() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalCost('beginFuel','beginCostFuel','endFuel','endCostFuel','totalCostFuel')">
                                    </td>
                                </tr>
                                <tr>
                                    <th width="auto">Total</th>
                                    <td><input size="10" type="text" readonly id="totalGas" id="totalGas"
                                               name="totalGas" value="<%=beginFlock.calcTotalQuantityGas() %>">
                                    </td>
                                    <td><input size="10" type="text" readonly id="totalCostGas"
                                               id="totalCostGas" name="totalCostGas"
                                               value="<%=beginFlock.getTotalGas()%>$"></td>
                                    <td><input size="10" type="text" readonly id="totalFuel" id="totalFuel"
                                               name="totalFuel" value="<%=beginFlock.calcTotalQuantityFuel() %>">
                                    </td>
                                    <td><input size="10" type="text" readonly id="totalCostFuel"
                                               name="totalCostFuel" value="<%=beginFlock.getTotalFuel()%>$"></td>
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
                                               value="<%=beginFlock.getQuantityMale() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalChicks()"></td>
                                    <td><input type="text" id="maleCost" name="maleCost"
                                               value="<%=beginFlock.getCostChickMale() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalChicks()">
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#D5EFFF">Female</td>
                                    <td><input type="text" id="femaleQuant" name="femaleQuant"
                                               value="<%=beginFlock.getQuantityFemale() %>"
                                               onkeyup="return checkField(event,this,0)"
                                               onblur="javascript:calcTotalChicks()"></td>
                                    <td><input type="text" id="femaleCost" name="femaleCost"
                                               value="<%=beginFlock.getCostChickFemale() %>"
                                               onkeyup="return checkField(event,this,1)"
                                               onblur="javascript:calcTotalChicks()">
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#D5EFFF">Total</td>
                                    <td><input type="text" id="totalQuant" name="totalQuant" readonly
                                               value="<%=beginFlock.getQuantityChicks() %>"></td>
                                    <td><input type="text" id="totalQuantCost" name="totalQuantCost" readonly
                                               value="<%=beginFlock.calcTotalChicksCost() %>$"></td>
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
                                                           value="<%=beginFlock.getElectBegin() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                               calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                                <td><input type="text" size="10" id="endElectMeter" name="endElectMeter"
                                                           value="<%=beginFlock.getElectEnd() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                               calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                                <td><input type="text" size="10" id="priceElectMeter"
                                                           name="priceElectMeter"
                                                           value="<%=beginFlock.getCostElect() %>"
                                                           onblur="javascript:calcTotalAmount('startElectMeter','endElectMeter','totElect'),
                                                               calcTotalMeterCost('startElectMeter','endElectMeter','priceElectMeter','totElectCost')">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Total Electricity</th>
                                                <td><input type="text" id="totElect"
                                                           value="<%=beginFlock.getQuantityElect() %>" size="10"
                                                           readonly></td>
                                            </tr>
                                            <tr>
                                                <th>Total Sum</th>
                                                <td><input type="text" id="totElectCost"
                                                           value="<%=beginFlock.getTotalElect() %>" size="10" readonly>
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
                                                           value="<%=beginFlock.getWaterBegin() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                                   calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                                <td><input type="text" size="10" id="endWaterMeter" name="endWaterMeter"
                                                           value="<%=beginFlock.getWaterEnd() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                                   calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                                <td><input type="text" size="10" id="priceWaterMeter"
                                                           name="priceWaterMeter"
                                                           value="<%=beginFlock.getCostWater() %>"
                                                           onblur="javascript:calcTotalAmount('startWaterMeter','endWaterMeter','totWater'),
                                                                   calcTotalMeterCost('startWaterMeter','endWaterMeter','priceWaterMeter','totWaterCost')">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Total Water</th>
                                                <td><input type="text" id="totWater"
                                                           value="<%=beginFlock.getQuantityWater() %>" size="10"
                                                           readonly></td>
                                            </tr>
                                            <tr>
                                                <th>Total Sum</th>
                                                <td><input type="text" id="totWaterCost" size="10"
                                                           value="<%=beginFlock.getTotalWater() %>" size="10" readonly>
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
</body>
</html>