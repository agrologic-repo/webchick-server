function GetWindowSize(getWidth) {
  var myWidth = 0, myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myWidth = window.innerWidth;
    myHeight = window.innerHeight;
  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myWidth = document.documentElement.clientWidth;
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myWidth = document.body.clientWidth;
    myHeight = document.body.clientHeight;
  }
  //window.alert( 'Width = ' + myWidth + ', Height = ' + myHeight);
  if (getWidth) 
    return myWidth
  else
    return myHeight
  
}
function FindPosX(obj)
{
    var curleft = 0;
    if(obj.offsetParent)
    {
        while(1) 
        {
            curleft += obj.offsetLeft;
            if(!obj.offsetParent)
                break;
            obj = obj.offsetParent;
        }
    }
    else if(obj.x)
    {
        curleft += obj.x;
    }
    return curleft;
}
function FindPosY(obj)
{
    var curtop = 0;
    if(obj.offsetParent)
    {
        while(1)
        {
            curtop += obj.offsetTop;
            if(!obj.offsetParent)
                break;
            obj = obj.offsetParent;
        }
    }
    else if(obj.y)
    {
        curtop += obj.y;
    }
    return curtop;
}
function FindHeight(obj)
{
    return (obj.offsetHeight);
}
function FindWidth(obj)
{
    return (obj.offsetWidth);
}
function InsertAtCursor(fld, val)
{
	if (document.selection) 
	{
		//ie support
		fld.focus();
		sel = document.selection.createRange();
		sel.text = val;
	}
	else if (fld.selectionStart || fld.selectionStart == "0") 
	{
		//moz/netscape support
		var startPos = fld.selectionStart;
		var endPos = fld.selectionEnd;
		fld.value = fld.value.substring(0, startPos) + val + fld.value.substring(endPos, fld.value.length);
	} 
	else 
	{
		fld.value += val;
	}
}
