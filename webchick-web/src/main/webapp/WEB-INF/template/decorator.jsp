<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<html dir="<%=session.getAttribute("dir")%>">

<head>
    <link rel="StyleSheet" type="text/css" href="resources/style/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="resources/style/menubar.css"/>
    <style type='text/css'>
        #search {
            cursor: pointer;
        }

        #refresh {
            cursor: pointer;
        }

        #filter {
            cursor: pointer;
        }
    </style>
    <decorator:head/>
</head>
<body>
    <decorator:body/>
</body>
</html>