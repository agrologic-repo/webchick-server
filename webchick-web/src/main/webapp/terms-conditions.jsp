<%--
    Document   : terms-conditions
    Created on : Dec 8, 2011, 9:39:34 AM
    Author     : Administrator
--%>


<!DOCTYPE html>
<html dir="<%=session.getAttribute("dir")%>">
<head>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="shortcut icon" href="resources/images/favicon.ico">
    <title>Webchick Terms and Conditions</title>
</head>
<body>
<div id="header">

</div>
<div id="main-shell">

    <h1>Webchick Terms and Conditions</h1>

    <table class="table-list">
        <tr>
            <td>
                Please choose your language below to view the appropriate Terms and Conditions.
            </td>
        </tr>
    </table>
    <table class="table-simple" border="0">
        <tr style="background-color: #D5EFFF;">
            <td style="vertical-align: middle;">Please choose your languge
                <select name="null" tabindex="" id="countrySelector" onchange="onChange()">
                    <option value="zh">Chinese</option>
                    <option value="en" selected>English</option>
                    <option value="iw">Hebrew</option>
                    <option value="ru">Russian</option>
                </select>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td>
                <textarea cols="120" rows="30" readonly="true">Webchick Online Standard Terms and Conditions.

                    PLEASE READ VERY CAREFULLY THESE TERMS AND CONDITIONS AND THE FAQ BEFORE REGISTERING FOR THE
                    AGROLOGIC WEBCHICK ONLINE PROGRAM. PARTICIPATION IN THE AGROLOGIC WEBCHICK ONLINE PROGRAM INDICATES
                    THAT YOU ACCEPT THESE TERMS AND CONDITIONS. IF YOU DO NOT ACCEPT THESE TERMS AND CONDITIONS, PLEASE
                    DO NOT REGISTER FOR OR PARTICIPATE IN THE AGROLOGIC WEBCHICK ONLINE PROGRAM.

                    1.Introduction. This agreement ("Agreement") between You and Agrologic Inc. ("Agrologic") consists
                    of these Agrologic webchick Online Program (the "Program") Standard Terms and Conditions ("Terms and
                    Conditions"). A description of the Program, as generally offered by Agrologic, is available at the
                    Program Frequently Asked Questions ("FAQ") URL, located at http://agrologic.com:8084/webchick, or
                    such other URL as Agrologic may provide from time to time. "You" or "Publisher" means any entity
                    identified in an enrollment form submitted by the same or affiliated persons, and/or any agency or
                    network acting on its (or their) behalf, which shall also be bound by the terms of this Agreement.
                </textarea>
            </td>
        </tr>
        <tr>
            <td>
                <button>I Agreed</button>
            </td>
        </tr>
    </table>

</div>
</body>
</html>
