<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page errorPage="anerrorpage.jsp" %>
<%@ page import="com.agrologic.app.model.ControllerDto" %>
<%@ page import="com.agrologic.app.model.Data" %>
<%@ page import="java.util.regex.Matcher" %>
<jsp:directive.page import="java.util.regex.Pattern"/>
<%
    Long controllerId = Long.parseLong(request.getParameter("controllerId"));
    Long screenId = Long.parseLong(request.getParameter("screenId"));
    Long tableId = Long.parseLong(request.getParameter("tableId"));
    Long dataId = Long.parseLong(request.getParameter("dataId"));
    Collection<ControllerDto> controllers = (Collection<ControllerDto>) request.getSession().getAttribute("controllers");
    ControllerDto controller = getController(controllers, controllerId);
    Data data = controller.getInterestData(screenId, tableId, dataId);

%>
<%! ControllerDto getController(Collection<ControllerDto> controllers, Long controllerId) {
    for (ControllerDto c : controllers) {
        if (c.getId().equals(controllerId)) {
            return c;
        }
    }
    return null;
}
%>

<%! Data getDataFromList(Collection<Data> dataList, Long dataId) {
    for (Data d : dataList) {
        if (d.getId().equals(dataId)) {
            return d;
        }
    }
    return null;
}
%>

<%! boolean isNumeric(String value) {
    Pattern p = Pattern.compile("([0-9]*).([0-9]*)");
    Matcher m = p.matcher(value);
    return m.matches(); //TRUE
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="<%=(String)request.getSession().getAttribute("dir")%>">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK REL="SHORTCUT ICON" HREF="img/favicon5.ico" TITLE="AgroLogic Ltd." type="image/x-icon"/>
<title><%=session.getAttribute("change.value") %>
</title>
<%--
        <script type="text/javascript" language="javascript">
        var isChanged = false;
        var DEC_0    = 0;
        var DEC_1    = 1;
        var DEC_2    = 2;
        var DEC_3    = 3;
        var HUMIDITY = 4
        var TIME     = 5;
        var TIME_SEC = 6;
        var DATE     = 7;
        var DEC_4    = 8;
        var DEC_5    = 10;

        var EMPTY_DELIM = "";
        var DOT_DELIM   = ".";
        var TIME_DELIM  = ":";
        var DATE_DELIM  = "/";

        var DOT_CODE_1 = 190;
        var DOT_CODE_2 = 110;
        var TIME_CODE = 186;
        var DATE_CODE_1 = 111;
        var DATE_CODE_2 = 191;

        var formats = new Array();
            formats[DEC_0]    ="0";
            formats[DEC_1]    ="0.0";
            formats[DEC_2]    ="0.00";
            formats[DEC_3]    ="0.000";
            formats[HUMIDITY] = "0";
            formats[TIME]     ="00.00";
            formats[TIME_SEC] ="0";
            formats[DATE]     ="00.00";
            formats[DEC_4]    ="0";
            formats[DEC_5]    ="0";
        /*
         * Returns the caret (cursor) position of the specified text field.
         * Return value range is 0-oField.length.
         */
        function doGetCaretPosition (oField) {
            // Initialize
            var iCaretPos = 0;
            // IE Support
            if (document.selection) {
                // Set focus on the element
                oField.focus ();
                // To get cursor position, get empty selection range
                var oSel = document.selection.createRange ();
                // Move selection start to 0 position
                oSel.moveStart ('character', -oField.value.length);
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
        function doSetCaretPosition (oField, iCaretPos) {
            // IE Support
            if (document.selection) {
                // Set focus on the element
                oField.focus();
                // Create empty selection range
                var oSel = document.selection.createRange ();
                // Move selection start and end to 0 position
                oSel.moveStart ('character', -oField.value.length);
                // Move selection start and end to desired position
                oSel.moveStart ('character', iCaretPos);
                oSel.moveEnd ('character', 0);
                oSel.select ();
            } else// Firefox support
                if (oField.selectionStart || oField.selectionStart == '0') {
                    oField.selectionStart = iCaretPos;
                    oField.selectionEnd = iCaretPos;
                    oField.focus ();
            }
        }
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
        function validate()
        {
            var valid = true;
            var val = document.editForm.Nvalue.value;
            if (val == "")
            {
                    document.getElementById("msgValue").innerHTML="<%=session.getAttribute("edit.value.empty.message")%>";
                    document.getElementById("msgValue").style.color="RED";
                    document.editForm.Nvalue.focus();
                    valid = false;
            }
            if (IsNumeric(val) == false) {
                    document.getElementById("msgValue").innerHTML="<%=session.getAttribute("edit.value.error.message")%>";
                    document.getElementById("msgValue").style.color="RED";
                    document.editForm.Nvalue.focus();
                    valid = false;
            }

            if(!valid)
            {
                return false;
            } else {
                self.close();
            }
        }
        function checkField (event,val,type){
            if (val.value == "") {
               //skip
            } else {
                var numeric = IsNumeric(val.value);
                if(numeric) {
                    if(!isChanged){
                        var pos = val.value.length;
                        var head = val.value.substring(0,pos-2);
                        var tail = val.value.substring(pos-1,pos);
                        val.value=head+tail;
                        doSetCaretPosition(val,val.value.lenght-1)

                    } else {
                        var pos = val.value.length;
                        var head = val.value.substring(0,pos-1);
                        val.value=head;
                    }
                }
            }
            doDecPoint(event,val,type);
        }
        function doDecPoint(event,val,type) {
            var txt = "";
            var keyCode = event.keyCode;
            if(keyCode == 16) {
                return;
            }
            if(keyCode >= 96) {// if input numers from left keyboard numbers
                keyCode = event.keyCode - 96;
            } else {// if input numers from top keyboard numbers
                keyCode = event.keyCode - 48;
            }

            if(!isChanged) {
                val.value = formats[type];
                var $caretpos = doGetCaretPosition(val);
                for (var i=0; i <= val.value.length; i++){
                    var ch = val.value.charAt(i);
                    if(ch.search('[0-9.:/]') != -1 || ch != " "){
                        if(i==$caretpos-1){
                            txt = txt + keyCode;
                            break;
                        } else {
                            txt = txt + ch;
                        }
                    }
                }
                val.value=getFixFormat(txt,type,isChanged)
                isChanged = true;
            } else {
                var ch = event.keyCode;
                if( event.keyCode == DOT_CODE_1 ||
                        event.keyCode == DOT_CODE_2 ||
                            event.keyCode == TIME_CODE ||
                                event.keyCode == DATE_CODE_1 ||
                                    event.keyCode == DATE_CODE_2 ) {

                    val.value = val.value.substring(0,val.value.length-1);
                } else {
                    val.value=getFixFormat(val.value,type,isChanged);
                }
            }
        }
        function getFixFormat(txt,t,changed) {
            var type = parseInt(t);
            switch(type){
                case DEC_0:

                    if(!changed) {
                        var num = parseInt(txt);
                        var result = num.toFixed(0);
                        return result;
                    } else {
                        var num = parseInt(txt);
                        if(num > 9999 ){
                            var d = parseInt(num/10000) ;
                            num=num-(d*10000);
                        }
                        var result = num.toString();
                        return result;
                    }
                    break;
                case DEC_1:
                    if(!changed) {
                        var num = parseFloat(txt);
                        var result = num.toFixed(1);
                        return result;
                    } else {
                        var num = parseFloat(txt);
                        num = num * 10;
                        if(num > 99.9 ){
                            var d = parseInt(num/100) ;
                            num=num-(d*100);
                        }
                        var result = num.toFixed(1);
                        return result;
                    }
                    break;
                case DEC_2:
                    if(!changed) {
                        var num = parseFloat(txt);
                        var result = num.toFixed(1);
                        return result;
                    } else {
                        var num = parseFloat(txt);
                        num = num * 10;
                        if(num > 99.9 ){
                            var d = parseInt(num/100) ;
                            num=num-(d*100);
                        }
                        var result = num.toFixed(2);
                        return result;
                    }
                    break;
                case DEC_3:
                    if(!changed) {
                        var num = parseFloat(txt);
                        var result = num.toFixed(1);
                        return result;
                    } else {
                        var num = parseFloat(txt);
                        num = num * 10;
                        if(num > 99.9 ){
                            var d = parseInt(num/100) ;
                            num=num-(d*100);
                        }
                        var result = num.toFixed(3);
                        return result;
                    }
                    break;
                case HUMIDITY:
                    if(!changed) {
                        var num = parseInt(txt);
                        var result = num.toFixed(0);
                        return result;
                    } else {
                        var num = parseInt(txt);
                        if(num > 999 ){
                            var d = parseInt(num/1000) ;
                            num=num-(d*1000);
                        }
                        var result = num.toString();
                        return result;
                    }
                    break;
                case TIME:
                    if(!changed) {
                        var num = parseFloat(txt);
                        var result = num.toFixed(2);
                        result = result.replace(DOT_DELIM,TIME_DELIM);
                        return result;
                    } else {
                        var result = txt.replace(TIME_DELIM,DOT_DELIM);
                        var num = parseFloat(result);
                        num = num*10;
                        if(num > 99.9 ){
                            var d = parseInt(num/100) ;
                            num=num-(d*100);
                        }
                        result=num.toFixed(2);
                        result = result.replace(DOT_DELIM,TIME_DELIM);
                        return result;
                    }
                    break;
                case DATE:
                    if(!changed) {
                        var num = parseFloat(txt);
                        var result = num.toFixed(2);
                        result = result.replace(DOT_DELIM,DATE_DELIM);
                        return result;
                    } else {
                        var result = txt.replace(DATE_DELIM,DOT_DELIM);
                        var num = parseFloat(result);
                        num = num*10;
                        if(num > 99.9 ){
                            var d = parseInt(num/100) ;
                            num=num-(d*100);
                        }
                        result=num.toFixed(2);
                        result = result.replace(DOT_DELIM,DATE_DELIM);
                        return result;
                    }
                    break;
                case DEC_4:
                      if(!changed) {
                        var num = parseInt(txt);
                        var result = num.toFixed(0);
                        return result;
                    } else {
                        var num = parseInt(txt);
                        if(num > 999999 ){
                            var d = parseInt(num/1000000) ;
                            num=num-(d*1000000);
                        }
                        var result = num.toString();
                        return result;
                    }
                    break;
                case DEC_5:
                      if(!changed) {
                        var num = parseInt(txt);
                        var result = num.toFixed(0);
                        return result;
                    } else {
                        var num = parseInt(txt);
                        if(num > 99999 ){
                            var d = parseInt(num/100000) ;
                            num=num-(d*100000);
                        }
                        var result = num.toString();
                        return result;
                    }
                    break;
                }
        }
        function closeWindow() {
                self.close();
                window.opener.location.reload(true);
        }
        //onload="doSetCaretPosition (document.editForm.Nvalue,document.editForm.Nvalue.value.length-1)"
        </script>
--%>
<script type="text/javascript" language="javascript">
var isChanged = false;
var doClearOld = true;
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
            if (ch.search('[0-9.:/]') != -1 || ch != " ") {
                if (i == $caretpos - 1) {
                    txt = txt + keyCode;
                    break;
                } else {
                    txt = txt + ch;
                }
            }
        }
        //alert(txt)

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
function getFixFormat(txt, t, changed) {
    //alert(txt)
    //alert(t)
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
                //alert(txt)
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
                //alert(txt)
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
</script>
<link rel="stylesheet" type="text/css" href="css/rmtstyle.css"/>
</head>
<body onload="doSetCaretPosition (document.editForm.Nvalue,document.editForm.Nvalue.value.length-1)"
      onunload="closeWindow();">
<h2><%=controller.getTitle() %>
</h2>
<h4><%=session.getAttribute("edit.value.comment")%> - <%=data.getUnicodeLabel() %>
</h4>

<form id="editForm" name="editForm" action="./rmtctrl-changevalue.html" method="get" onsubmit="return validate();">
    <table>
        <input id="controllerId" readonly type="hidden" name="controllerId" class=rightTitles value="<%=controllerId%>">
        <input id="dataId" readonly type="hidden" name="dataId" class=rightTitles value="<%=dataId%>">
        <tr>
            <td><%=session.getAttribute("edit.value.label")%> *</td>
            <td><input id="Nvalue" type="text" name="Nvalue" value="<%=data.getFormattedValue() %>" style="width:100px"
                       onkeyup="return checkField(event,this,'<%=data.getFormat()%>')"
                       onkeydown="return keyDown(this)"></td>
        </tr>
        <tr>
            <td id="msgValue" colspan="2"></td>
        </tr>
        <tr>
            <td colspan="2">
                <div>
                    <input type="submit" value="<%=session.getAttribute("button.change")%>">
                    <input class="button" type="button" onclick='self.close();'
                           value="<%=session.getAttribute("button.cancel")%>">
                </div>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
