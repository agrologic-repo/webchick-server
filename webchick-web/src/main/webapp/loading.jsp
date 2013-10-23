<div id="loading" style="position:relative;visibility: visible; width:100%; text-align:center; top:300px;">
    <table style="width: auto;height: 45px; border: 1px solid #6699FF; background: white;" align="center">
        <tr>
            <td>
                <img src="resources/custom/images/loader.gif" border="0" alt="" hspace="10"><%=session.getAttribute("page.loading")%>
            </td>
        </tr>
    </table>
</div>


<script type="text/javascript">
    var ld = (document.all);
    var ns4 = document.layers;
    var ns6 = document.getElementById && !document.all;
    var ie4 = document.all;
    if (ns4)
        ld = document.loading;
    else if (ns6)
        ld = document.getElementById("loading").style;
    else if (ie4)
        ld = document.all.loading.style;

    function init() {
        if (ns4) {
            ld.visibility = "hidden";
        } else if (ns6 || ie4) {
            ld.display = "none";
        }
    }
</script>
