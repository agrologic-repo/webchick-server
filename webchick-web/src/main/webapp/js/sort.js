/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
    //
    // global variables
    //
    var tbody=null;					
    var theadrow=null;
    var colCount = null;


    var reverse = false;
    var lastclick = -1;					// stores the object of our last used object

    var oTR = null;
    var oStatus = null;
    var none = 0;

    function init() {

        // get TBODY - take the first TBODY for the table to sort
        tbody = element.tBodies(0);
        if (!tbody) return;

        //Get THEAD  
        var thead = element.tHead;
        if (!thead)  return;

        theadrow = thead.children[0]; //Assume just one Head row
        if (theadrow.tagName != "TR") return;

        theadrow.runtimeStyle.cursor = "hand";

        colCount = theadrow.children.length;

        var l, clickCell;
        for (var i=0; i<colCount; i++) 
        {
            // Create our blank gif
            l=document.createElement("IMG");
            l.src="img/empty.gif";
            l.id="srtImg";
            l.width=9;
            l.height=5;

            clickCell = theadrow.children[i];

            clickCell.selectIndex = i;
            clickCell.insertAdjacentText("beforeEnd","   ");
            clickCell.insertAdjacentElement("beforeEnd", l)
            clickCell.attachEvent("onclick", doClick);
        }

    }

    //
    // doClick handler
    // 
    //
    function doClick(e) 
    {
        var clickObject = e.srcElement;

        while (clickObject.tagName != "TH")
        {
            clickObject = clickObject.parentElement;
        }


        // clear the sort images in the head
        var imgcol= theadrow.all('srtImg');
        for(var x = 0; x < imgcol.length; x++) 
            imgcol[x].src = "img/empty.gif";

        if(lastclick == clickObject.selectIndex)
        {
            if(reverse == false)
            {
                clickObject.children[0].src = "img/downarrow.gif";
                reverse = true;
            }
            else 
            {
                clickObject.children[0].src = "img/uparrow.gif";
                reverse = false;
            }
        }
        else
        {
            reverse = false;
            lastclick = clickObject.selectIndex;
            clickObject.children[0].src = "img/uparrow.gif";
        }

        insertionSort(tbody, tbody.rows.length-1,  reverse, clickObject.selectIndex);
        return false;
    }

    function insertionSort(t, iRowEnd, fReverse, iColumn)
    {


        var iRowInsertRow, iRowWalkRow, current, insert;
        for ( iRowInsert = 0 + 1 ; iRowInsert <= iRowEnd ; iRowInsert++ )
        {
            if (iColumn) {	
                if( typeof(t.children[iRowInsert].children[iColumn]) != "undefined")
                    textRowInsert = t.children[iRowInsert].children[iColumn].innerText;
                else
                    textRowInsert = "";
            } else {
                textRowInsert = t.children[iRowInsert].innerText;
            }

            for ( iRowWalk = 0; iRowWalk <= iRowInsert ; iRowWalk++ )
            {
                if (iColumn) {
                    if(typeof(t.children[iRowWalk].children[iColumn]) != "undefined")
                        textRowCurrent = t.children[iRowWalk].children[iColumn].innerText;
                    else
                        textRowCurrent = "";
                } else {
                    textRowCurrent = t.children[iRowWalk].innerText;
                }

                //
                // We save our values so we can manipulate the numbers for
                // comparison
                //
                current = textRowCurrent;
                insert  = textRowInsert;


                //  If the value is not a number, we sort normally, else we evaluate	
                //  the value to get a numeric representation
                //
                if ( !isNaN(current) ||  !isNaN(insert)) 
                {
                    current= eval(current);
                    insert= eval(insert);
                }
                else
                {
                    current	= current.toLowerCase();
                    insert	= insert.toLowerCase();
                }


                if ( (   (!fReverse && insert < current)
                    || ( fReverse && insert > current) )
                    && (iRowInsert != iRowWalk) )
                {
                    eRowInsert = t.children[iRowInsert];
                    eRowWalk = t.children[iRowWalk];
                    t.insertBefore(eRowInsert, eRowWalk);
                    iRowWalk = iRowInsert; // done
                }
            }
        }
    }


