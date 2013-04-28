function expand(s)
{
    var td = s;
    var div = td.getElementsByTagName("div").item(0);
    td.className = "menuHover";
    div.className = "menuHover";
}

function collapse(s)
{
    var td = s;
    var div = td.getElementsByTagName("div").item(0);
    td.className = "menuNormal";
    div.className = "menuNormal";
}
