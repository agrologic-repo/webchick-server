var HLP_SHOW_POS_MOUSE = 0;
var HLP_SHOW_POS_CENTER = 1;
var HLP_SHOW_POS_V_MOUSE_H_CENTER = 2;

function ShowHelp (e, text, showPos, width, height, obj) {

    var posx = 0;
    var posy = 0;
    if (width == null)
    {
        width = 250;
    }
    if (height == null)
    {
        height = 250;
    }

    switch (showPos) {
        case HLP_SHOW_POS_MOUSE:
            if (!e) e = window.event;
            if (e.pageX || e.pageY) {
                posx = e.pageX;
                posy = e.pageY;
            } else if (e.clientX || e.clientY) {
                posx =
                e.clientX ;
                var top = document.body.scrollTop
                ? document.body.scrollTop
                : (window.pageYOffset
                    ? window.pageYOffset
                    : (document.body.parentElement
                        ? document.body.parentElement.scrollTop
                        : 0
                        )
                    );

                posy =
                e.clientY + top;
            }
            break;
        case HLP_SHOW_POS_CENTER:
            //Get screen dims
            var winWidth = GetWindowSize(true);
            var winHeight = GetWindowSize(false);
            posx = (winWidth / 2);
            posy = (winHeight / 2) - 85; //100-15
            break;
        case HLP_SHOW_POS_V_MOUSE_H_CENTER:
            var winWidth = GetWindowSize(true);
            if (!e) e = window.event;
            if (e.pageX || e.pageY) {
                posy = e.pageY;
            }
            else if (e.clientX || ev.clientY) {
                posy = e.clientY;
            }
            posx = (winWidth / 2);
            break;
    }
    var helpObj = document.getElementById("helpBox" + obj);
    var helpInner = document.getElementById("helpBoxInner" + obj);
    helpObj.style.display = 'block';
    helpInner.innerHTML = text;
    helpObj.style.left = (posx - (width / 2)) + "px";
    helpObj.style.top = (posy + 15) + "px";
    helpObj.style.width = width + "px";
    helpInner.style.width = (width - 5) + "px";
    helpObj.style.height = height + "px";
    helpInner.style.height = (height + 88) + "px";

}
function HideHelp(obj) {
    var helpObj = document.getElementById("helpBox"+obj);
    helpObj.style.display = 'none';
}
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
