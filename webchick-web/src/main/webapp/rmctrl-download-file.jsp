<%@ page import="java.io.File,java.io.FileInputStream" %>
<%
    try {
        String filename = request.getParameter("filename");
        //String filename ="c:\\2011\\01\\book.pdf";
        File fileToDownload = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(fileToDownload);
        //response.setContentType("text/plain");
        response.setContentType("application/octet-stream");
        //response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        int i;
        while ((i = fileInputStream.read()) != -1) {
            response.getOutputStream().write(i);
        }
        fileInputStream.close();
        response.getOutputStream().flush();

    } catch (Exception e) {// file IO errors
        e.printStackTrace();
    }
%>