<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
  <title></title>
</head>
<jsp:useBean id="cellink" scope="request" type="com.agrologic.app.model.Cellink"/>
<body>
<h1><c:out value="${cellink.name}"/></h1>
</body>
</html>
