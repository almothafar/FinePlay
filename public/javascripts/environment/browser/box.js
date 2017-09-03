'use strict';

var boxmap = {
	'$("#box").width()': $("#box").width(),
	'$("#box").innerWidth()': $("#box").innerWidth(),
	'$("#box").outerWidth()': $("#box").outerWidth(),
	'$("#box").outerWidth(true)': $("#box").outerWidth(true),
	'$("#box").prop("clientWidth")': $("#box").prop("clientWidth"),
	'$("#box").prop("offsetWidth")': $("#box").prop("offsetWidth"),
	'$("#box").prop("scrollWidth")': $("#box").prop("scrollWidth"),
	'document.querySelector("#box").clientWidth': document.querySelector("#box").clientWidth,
	'document.querySelector("#box").offsetWidth': document.querySelector("#box").offsetWidth,
	'document.querySelector("#box").scrollWidth': document.querySelector("#box").scrollWidth,
}

$.each(boxmap, function(key, value){

	$("tbody").append("<tr></tr>");
	$("tbody > tr:last-child").append("<td>"+key+"</td><td>"+value+"</td>");
});
