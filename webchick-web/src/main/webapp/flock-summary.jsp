<%@ page import="com.agrologic.app.model.Flock" %>
<%
    Flock summaryFlock = (Flock) session.getAttribute("flock");
%>
<!DOCTYPE html>
<html>
<head>
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
            <fieldset style="-moz-border-radius:5px;  border-radius: 5px;  -webkit-border-radius: 5px;">
                <legend> Summary</legend>
                <table border="1" style="width: 000px;border:1px solid #C6C6C6; border-collapse: collapse;">
                    <tr>
                        <th nowrap>Chicks</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalChicks() %>">
                        </td>
                        <th nowrap>Spread</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalSpread() %>">
                        </td>
                    </tr>
                    <tr>
                        <th nowrap>Electricity</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalElect() %>">
                        </td>
                        <th nowrap>Feed</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalFeed() %>">
                        </td>
                    </tr>
                    <tr>
                        <th nowrap>Water</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalWater() %>">
                        </td>
                        <th nowrap>Medicine</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalMedic() %>">
                        </td>
                    </tr>
                    <tr>
                        <th nowrap>Disel</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalFuel() %>">
                        </td>
                        <th nowrap>Labor</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalLabor() %>">
                        </td>
                    </tr>
                    <tr>
                        <th nowrap>Gas</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getTotalGas() %>">
                        </td>
                        <th nowrap>Transaction</th>
                        <td align="center"><input type="text" readonly value="<%=summaryFlock.getExpenses() %>">
                        </td>
                    </tr>
                </table>
                <hr>
                <table>
                    <tr>
                        <td>Total Revenues</td>
                        <td><input type="text" readonly value="<%=summaryFlock.calcTotalRevenues() %>"></td>
                    </tr>
                    <tr>
                        <td>Total Expenses</td>
                        <td><input type="text" readonly value="<%=summaryFlock.calcTotalExpenses() %>"></td>
                    </tr>
                    <tr>
                        <td>Cost per kg</td>
                        <td><input type="text" readonly value="<%=summaryFlock.calcTotCostPerKGBirds()%>"></td>
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
</body>
</html>