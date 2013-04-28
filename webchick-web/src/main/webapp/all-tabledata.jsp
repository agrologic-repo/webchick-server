<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="anerrorpage.jsp"%>
<%@ page import="java.util.List"%>
<%@ include file="disableCaching.jsp" %>
<%@ include file="language.jsp" %>

<jsp:directive.page import="com.agrologic.app.web.UserRole"/>
<jsp:directive.page import="com.agrologic.app.model.UserDto"/>
<jsp:directive.page import="com.agrologic.app.model.ProgramDto"/>
<jsp:directive.page import="com.agrologic.app.model.ScreenDto"/>
<jsp:directive.page import="com.agrologic.app.model.TableDto"/>
<jsp:directive.page import="com.agrologic.app.model.DataDto"/>
<jsp:directive.page import="com.agrologic.app.model.LanguageDto"/>

<%  UserDto user = (UserDto)request.getSession().getAttribute("user");
    if(user == null) {
        response.sendRedirect("./index.htm");
        return;
    }
    ProgramDto program = (ProgramDto)request.getSession().getAttribute("program");
    ScreenDto screen = (ScreenDto)request.getSession().getAttribute("screen");
    List<TableDto> tables = screen.getTables();
    long tableId = Long.parseLong(request.getParameter("tableId"));
    long screenId = screen.getId();
    long programId = screen.getProgramId();
    List<LanguageDto> languages = (List<LanguageDto>)request.getSession().getAttribute("languages");
    String ptl = (String)request.getParameter("translateLang");
    if(ptl == null) {
        ptl = "1";
    }
    Long translateLang = Long.parseLong(ptl);
%>

<%! TableDto getTableToEdit(long tableId, List<TableDto> tables) {
        for(TableDto table:tables) {
            if(table.getId() == tableId) {
                return table;
            }
        }
        return new TableDto();
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>All Table Data</title>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <link rel="StyleSheet" type="text/css" href="css/menubar.css"/>
        <link rel="StyleSheet" type="text/css" href="css/admincontent.css"/>
        <script type="text/javascript" src="js/util.js"></script>
        <script type="text/javascript" src="js/general.js"></script>
        <script type="text/javascript">
            function validate() {
                if (document.editForm.Nprogramname.value == "") {
                        alert('Enter user name');
                        document.editForm.Nprogramname.focus();
                        return false;
                } else {
                        document.editForm.submit();
                }
            }
            function addData(tableId, position) {
                window.open("add-data.jsp?programId=<%=programId%>&screenId=<%=screenId%>&tableId="+tableId+"&position="+position,"mywindow","status=yes,width=450,height=230,left=350,top=400,screenX=100,screenY=100");
            }
            function removeData(programId,screenId,tableId,dataId) {
                if(confirm("This action will remove tabledata from database.\n Do you want to continue?") ==  true) {
                    window.document.location.replace("./removedata.html?programId="+programId+"&screenId="+screenId+"&tableId="+ tableId +"&dataId=" + dataId);
                }
            }
            function save(programId,screenId,tableId) {
                var showDataMap = new Hashtable();
                var posDataMap = new Hashtable();

                var chb = document.formTable.list;
                var poss = document.formTable.position;

                // if only one check table-list
                if(chb.tagName == "INPUT") {
                    var dataId = chb.value;
                    if (chb.checked ) {
                        showDataMap.put(dataId,"yes");
                    } else {
                        showDataMap.put(dataId,"no");
                    }
                    var poss = poss.value;
                    posDataMap.put(dataId,poss);
                // if two or more check table-list
                } else {
                    for( var i = 0; i < chb.length; i++) {
                        var dataId = chb[i].value;
                        if (chb[i].checked ) {
                            showDataMap.put(dataId,"yes");
                        } else {
                            showDataMap.put(dataId,"no");
                        }
                        var pos = poss[i].value;
                        posDataMap.put(dataId,pos);
                    }
                }
                window.document.location.replace("./save-datalist.html?programId="+programId+"&screenId="+screenId+"&tableId="+ tableId +"&showDataMap=" + showDataMap +"&posDataMap=" + posDataMap);
            }
            function checkedAll () {
                var form = document.getElementById('formTable');
                var checkall = document.getElementById('listall');

                for (var i = 0; i < form.elements.length; i++) {
                    if (checkall.checked == true) {
                        form.elements[i].checked = true;
                    } else {
                        form.elements[i].checked = false;
                    }
                }
             }
            function check(index) {
                var form = document.getElementById('formTable');
                var checkall = document.getElementById('listall');

                for (var i = 0; i < form.elements.length; i++) {
                    if (form.elements[i].value == index && form.elements[i].checked == false) {
                        checkall.checked = false;
                    }
                }
             }
            function back(link) {
                window.document.location.replace(link);
                return false;
            }
            function filterLanguages(programId,screenId,tableId)
        {
           var langId = document.formTable.Lang_Filter.value;
           window.document.location.replace("./all-tabledata.html?programId=" + programId+"&screenId="+ screenId +"&tableId="+ tableId +"&translateLang=" + langId);
           return false;
        }
        </script>
    </head>
    <body>
    <div id="header">
        <%@include file="usermenuontop.jsp"%>
    </div>
    <div id="main-shell">
      <table border="0" cellPadding=1 cellSpacing=1 width="100%">
        <tr>
          <td valign="top">
            <table border=0 cellPadding=1 cellSpacing=1>
              <%
                TableDto table = getTableToEdit(tableId, tables);
                int size = table.getDataList().size();
                int lastPos = 1;
                if(size > 0) {
                    lastPos = (table.getDataList().get(size-1)).getPosition() + 1;
                }
                List<DataDto> dataList = table.getDataList();
              %>
              <tr>
                <td>
                    <h1><%=session.getAttribute("data.page.header") %></h1>
                    <h2><%=session.getAttribute("data.page.sub.header") %></h2>
                    <h3>Program - <font color="teal"><%=program.getName()%></font>;
                        Screen - <font color="teal"><%=screen.getTitle()%></font>;
                        Table - <font color="teal"><%=table.getTitle()%></font>;</h3>
                </td>
                <td align="center" colspan="3">
                    <%@include file="messages.jsp" %>
                </td>
              </tr>
              <tr>
                <td>
                    <button id="btnAdd" name="btnAdd"
                            onclick="window.open('add-data.jsp?programId=<%=programId%>&screenId=<%=screenId%>&tableId=<%=table.getId()%>&position=<%=lastPos%>','mywindow','status=yes,width=550,height=450,left=350,top=400,screenX=100,screenY=100');">
                        <%=session.getAttribute("button.add.data") %>
                    </button>
                  <button id="btnCancel" style="float:left" name="btnCancel" onclick='return back("./all-tables.html?programId=<%=screen.getProgramId() %>&screenId=<%=table.getScreenId()%>&translateLang=<%=translateLang%>");'>
                      <%=session.getAttribute("button.back") %>
                  </button>
                </td>
              </tr>
              <tr>
                <td colSpan=3>
                <p>
                    <b><%=dataList.size()%></b> <%=session.getAttribute("label.records")%></p>
                <form id="formTable" name="formTable">
                    <table class="table-list" border="1" style="behavior:url(tablehl.htc) url(sort.htc);">
                        <thead>
                            <th class="centerHeader" width="100px">ID</th>
                            <th class="centerHeader" width="200px">Title</th>
                            <th class="leftHeader" width="240px">Text
                            <select id="Lang_Filter" name="Lang_Filter" onchange="return filterLanguages(<%=programId%>,<%=screenId%>, <%=tableId%>);">
                                <%for(LanguageDto l : languages){ %>
                                    <option value="<%=l.getId()%>"><%=l.getLanguage()%></option>
                                <%}%>
                            </select>
                            </th>
                            <th class="centerHeader" width="140px">Show All &nbsp;<input type="checkbox" id="listall" name="listall" title="Show" onclick="checkedAll();"></th>
                            <th class="centerHeader" width="100px">Position </th>
                            <th class="centerHeader" width="300px">Action</th>
                        </thead>
                        <% int cnt = 0;%>
                        <%for(DataDto data:dataList) {%>
                        <% if ((cnt % 2) == 0) {%>
                        <tr class="odd" onMouseOver="changeOdd(this);"  onmouseout="changeOdd(this)">
                        <%} else {%>
                        <tr class="even" onMouseOver="changeEven(this);"   onmouseout="changeEven(this)">
                        <%}%>
                            <td align="center" width="100px"><%=data.getType()%></td>
                            <td>
                                <% if(data.getIsRelay()) {%>
                                   <a href="./program-relays.html?programId=<%=screen.getProgramId()%>"><%=data.getTitle()%></a>
                                <%} else {%>
                                   <%=data.getTitle()%>
                                <%}%>
                            </td>
                            <td ondblclick="window.open('add-datatranslate.jsp?dataId=<%=data.getId()%>&langId=<%=translateLang%>&dataName=<%=data.getLabel()%>','mywindow','status=yes,width=300,height=250,left=350,top=400,screenX=100,screenY=100');"><%=data.getUnicodeLabel() %></td>
                            <td align="center" width="100px"><input type="checkbox" id="list" name="list" <%=data.isChecked()%> value="<%=data.getId()%>" onclick="check(<%=data.getId()%>);"></td>
                            <td align="center" width="100px"><input type="text" name="position" value="<%=data.getPosition()%>" size="5"></td>
                            <td align="center" width="200px">
                                <img src="img/close.png" title="Delete This Data" border="0"/>
                                <a href="javascript:removeData(<%=programId %>,<%=screenId %>,<%=tableId%>,<%=data.getId()%>);"><%=session.getAttribute("button.delete")%></a></td>
                        </tr>
                        <%cnt++;%>
                    <%}%>
                    </table>
                </form>
                </td>
              </tr>
              <tr>
                <td colspan="2" align="right">
                  <button id="btnCancel" style="float:left" name="btnCancel" onclick='return back("./all-tables.html?programId=<%=screen.getProgramId() %>&screenId=<%=table.getScreenId()%>&translateLang=<%=translateLang%>");'>
                      <%=session.getAttribute("button.back") %>
                  </button>
                  <button id="btnSave" style="float:left" name="btnSave" onclick='return save(<%=programId %>,<%=screenId %>,<%=table.getId()%>);'>
                      <%=session.getAttribute("button.save") %>
                  </button>
                </td>
              </tr>
            </table>
            <script language="Javascript">
                var length = document.formTable.Lang_Filter.options.length;
                for(var i = 0; i < length; i++) {
                    var translateLang = parseInt(<%=request.getParameter("translateLang")%>);
                    if(document.formTable.Lang_Filter.options[i].value == translateLang) {
                        document.formTable.Lang_Filter.selectedIndex=i;
                        break;
                    }
                }
            </script>
            <br />
          </td>
        </tr>
    </table>
    </div>
    </body>
</html>