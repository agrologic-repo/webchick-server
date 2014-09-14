<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>

<%
    StringBuffer url = request.getRequestURL();
    String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
    if (uri != null) {
        url = new StringBuffer(uri);
    }

    String paramString = "";
    Map<String, String[]> params = request.getParameterMap();
    if (!params.isEmpty()) {
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals("lang")) {
                continue;
            } else {
                String[] values = params.get(key);
                if (!values[0].equals("null")) {
                    String valueString = values[0];
                    paramString += "&" + key + "=" + valueString;
                }
            }
        }
    }
    String domain = (String) session.getAttribute("domain");
    if (domain == null) {
        domain = "agrologic";
    }
%>

<form name="formLang" style="display:inline;">
    <table class="lang-container">
        <tr>
            <td valign="top">

                <% String lng = (String) request.getAttribute("lang");%>
                <% if (lng == null || lng.equals("en")) {%>
                <img src="resources/images/usa.png" title="<%=session.getAttribute("language.english") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>

                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>
                <% } else if (lng.equals("iw")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <img src="resources/images/israel.png" title="<%=session.getAttribute("language.hebrew") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>

                <% } else if (lng.equals("zh")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <img src="resources/images/china.png" title="<%=session.getAttribute("language.chinese") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>

                <% } else if (lng.equals("fr")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <img src="resources/images/french.png" title="<%=session.getAttribute("language.french") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>

                <% } else if (lng.equals("ru")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <img src="resources/images/russian.png" title="<%=session.getAttribute("language.russian") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>

                <% } else if (lng.equals("de")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <img src="resources/images/german.png" title="<%=session.getAttribute("language.german") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>

                <a name="NewURL" href="<%=url%>?lang=tr<%=paramString%>"><img src="resources/images/turkey.png"
                                                                              title="<%=session.getAttribute("language.turkish") %>"
                                                                              border="0"/></a>
                <% } else if (lng.equals("tr")) {%>
                <a name="NewURL" href="<%=url%>?lang=en<%=paramString%>"><img src="resources/images/usa.png"
                                                                              title="<%=session.getAttribute("language.english") %>"
                                                                              border="0"/></a>
                <% if (domain.startsWith("agrologic") || domain.startsWith("localhost") || domain.startsWith("192.168.40.3")) { %>
                <a name="NewURL" href="<%=url%>?lang=iw<%=paramString%>"><img src="resources/images/israel.png"
                                                                              title="<%=session.getAttribute("language.hebrew") %>"
                                                                              border="0"/></a>
                <%}%>
                <a name="NewURL" href="<%=url%>?lang=zh<%=paramString%>"><img src="resources/images/china.png"
                                                                              title="<%=session.getAttribute("language.chinese") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=fr<%=paramString%>"><img src="resources/images/french.png"
                                                                              title="<%=session.getAttribute("language.french") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=ru<%=paramString%>"><img src="resources/images/russian.png"
                                                                              title="<%=session.getAttribute("language.russian") %>"
                                                                              border="0"/></a>
                <a name="NewURL" href="<%=url%>?lang=de<%=paramString%>"><img src="resources/images/german.png"
                                                                              title="<%=session.getAttribute("language.german") %>"
                                                                              border="0"/></a>
                <img src="resources/images/turkey.png" title="<%=session.getAttribute("language.turkish") %>"
                     style="filter: alpha(opacity=30);opacity: .30;"/>
                <%}%>
                <%--turkey.png--%>
            </td>
        </tr>
    </table>
</form>