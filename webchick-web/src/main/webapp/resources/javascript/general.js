/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//<!-- BEGIN HIDING
function changeEven(clazz) {
    if (clazz.className == 'selected') {
        clazz.className = 'even';
    } else {
        clazz.className = 'selected';
    }
}
function changeOdd(clazz) {
    if (clazz.className == 'selected') {
        clazz.className = 'odd';
    } else {
        clazz.className = 'selected';
    }
}

function redirect(uri) {
    window.location.href = uri;
}

function back(uri) {
    redirect(uri);
    return false;
}
// DONE HIDING -->
