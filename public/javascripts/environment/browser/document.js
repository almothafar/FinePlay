'use strict';

if(document.fullscreenEnabled){$("#Fullscreen").removeClass("badge-danger").addClass("badge-success")}
if(document.visibilityState){$("#PageVisibility").removeClass("badge-danger").addClass("badge-success")}

var documentMap = new Object();
$.each(document, function(key, val){

	var valType = typeof val;
	if('object' == valType || 'function' ==  valType ){return true;}
	documentMap[key] = val;
});

var documentKeys = Object.keys(documentMap);
documentKeys.sort();

$.each(documentKeys, function(i, key){

	$("tbody").append("<tr></tr>");
	$("tbody > tr:last-child").append("<td>"+key+"</td><td>"+documentMap[key]+"</td>");
});
