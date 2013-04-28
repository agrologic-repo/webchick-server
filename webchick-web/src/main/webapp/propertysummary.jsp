<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>

<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.CellinkDto"/>
<jsp:directive.page import="com.agrologic.app.model.ControllerDto"/>
<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.web.CellinkState"/>
<%  String message =(String)request.getSession().getAttribute("message");
    request.getSession().setAttribute("message",null);

    Boolean errorFlag = (Boolean)request.getSession().getAttribute("error");
    request.getSession().setAttribute("error",null);

    List<UserDto> users = (List<UserDto>)request.getSession().getAttribute("users");
    List<CellinkDto> allCellinks = getTotalCellinks(users);
    List<ControllerDto> allControllers = getTotalControllers(allCellinks);
%>
<%! List<CellinkDto> getTotalCellinks(List<UserDto> users) {
        List<CellinkDto> totCellinks = new ArrayList<CellinkDto>();
        for(UserDto u:users) {
            List<CellinkDto> cellinks = u.getCellinks();
            Iterator iter= cellinks.iterator();
            while(iter.hasNext()) {
                totCellinks.add( (CellinkDto)iter.next());
            }
        }
        return totCellinks;
    }
%>

<%! List<ControllerDto> getTotalControllers(List<CellinkDto> cellinks) {
        List<ControllerDto> totControllers = new ArrayList<ControllerDto>();
        for(CellinkDto c:cellinks) {
            List<ControllerDto> controllers = c.getControllers();
            Iterator iter= controllers.iterator();
            while(iter.hasNext()) {
                totControllers.add( (ControllerDto)iter.next());
            }
        }
        return totControllers;
    }
%>
<%! int getNumberUsersByGroup(List<UserDto> users, int role) {
        int count = 0;

        for(UserDto u:users) {
            if(u.getRole() == role) {
                count++;
            }
        }
        return count;
    }
%>

<%! int getTotlalCellinks(List<UserDto> users) {
        int count = 0;
        for(UserDto u:users) {
            count+=u.getCellinks().size();
        }
        return count;
    }
%>
<%! int getOnlineCellinks(List<UserDto> users) {
        int count = 0;
        for(UserDto u:users) {
            List<CellinkDto> cellinks = u.getCellinks();
            for(CellinkDto c:cellinks) {
                if(c.getState() != CellinkState.STATE_OFFLINE) {
                    count++;
                }
            }
        }
        return count;
    }
%>
<%! int getOfflineCellinks(List<UserDto> users) {
        int count = 0;
        for(UserDto u:users) {
            List<CellinkDto> cellinks = u.getCellinks();
            for(CellinkDto c:cellinks) {
                if(c.getState() == CellinkState.STATE_OFFLINE) {
                    count++;
                }
            }
        }
        return count;
    }
%>
<%! int getRunningCellinks(List<UserDto> users) {
        int count = 0;
        for(UserDto u:users) {
            List<CellinkDto> cellinks = u.getCellinks();
            for(CellinkDto c:cellinks) {
                if(c.getState() == CellinkState.STATE_RUNNING) {
                    count++;
                }
            }
        }
        return count;
    }
%>
<%! int getCellinksByStates(List<UserDto> users,Integer state) {
        int count = 0;
        for(UserDto u:users) {
            List<CellinkDto> cellinks = u.getCellinks();
            for(CellinkDto c:cellinks) {
                if(c.getState() == state) {
                    count++;
                }
            }
        }
        return count;
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>All Users</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="img/favicon5.ico"/>
    <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
    <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
    <script type="text/javascript">
    function back(link){
        window.document.location.replace(link);
        return false;
    }
    </script>
</head>
<body>
    <div class="shell">
    <%@include file="usermenuontop.jsp"%>
      <table border="0" cellPadding=1 cellSpacing=1 width="100%">
    <tr>
      <td valign="top">
        <table border=0 cellPadding=1 cellSpacing=1 width="1024px">
          <tr>
            <td class="breadcrumb">
              <a class="small" href="./main.jsp">Home</a>&nbsp;&gt;&nbsp;
              <a class="small" href="./all-users.html">Users</a>
            </td>
          </tr>
          <tr>
            <td>
              <p><h1>Property Summary</h1></p>
            </td>
          </tr>
          <tr>
          <td valign="top">
            <table cellSpacing=1 cellPadding=1 border=0 align="left">
              <tr>
                <td>
                  <p><h2>User Property </h2></p>
                </td>
              </tr>
              <tr>
                <td valign="top">
                  <img border="0" src="TotalUserPieChart">
                </td>
              </tr>
            </table>
          </td>
          <td valign="top">
            <table cellSpacing=1 cellPadding=1 align=left border=0 width="100%">
              <tr>
                <td>
                  <p><h2>Cellink Property </h2></p>
                </td>
              </tr>
              <tr>
                <td>
                  <img border="0" src="TotalCellinkPieChart">
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan="5" align="right">
            <p><button id="btnBack" name="btnBack" onclick='return back("./all-users.html");'>&nbsp;Back</button></p>
          </td>
        </tr>
      </table>
    </td>
  </tr>
    </table>
    </div>

</body>
</html>