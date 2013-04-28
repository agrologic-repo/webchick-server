<%--
    Document   : create-db-table
    Created on : Dec 7, 2011, 3:33:18 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<%@ page import="java.sql.*" %>

<HTML>
    <HEAD>
        <TITLE>Creating a Table</TITLE>
    </HEAD>

    <BODY>
        <H1>Creating a Table</H1>

        <%
            Connection connection = null;
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
                connection = DriverManager.getConnection("jdbc:odbc:data", "userName", "password");

                Statement statement = connection.createStatement();
                String command = "CREATE TABLE Employees (ID INTEGER, Name CHAR(50));";
                statement.executeUpdate(command);

            } catch (Exception e) {
                out.println("An error occurred.");
            }
        %>
        The Employees table was created.
    </BODY>
</HTML>