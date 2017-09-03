'use strict';

var boxmap = {
	'$("#box").offset().top': $("#box_container>.box").offset().top,
	'$("#box").position().top': $("#box_container>.box").position().top,
	'$("#box").scrollTop()': $("#box_container>.box").scrollTop(),
	'document.querySelector("#box").getBoundingClientRect().top': document.querySelector("#box_container>.box").getBoundingClientRect().top,
	'document.querySelector("#box").offsetTop': document.querySelector("#box_container>.box").offsetTop,
	'document.querySelector("#box").scrollTop': document.querySelector("#box_container>.box").scrollTop,
}

$.each(boxmap, function(key, value){

	$("#table tbody").append("<tr></tr>");
	$("#table tbody > tr:last-child").append("<td>"+key+"</td><td>"+value+"</td>");
});

$('#box_container').scroll(function(){

	$("#table tbody>tr").eq(0).find('td:nth-child(2)').text($("#box_container>.box").offset().top);
	$("#table tbody>tr").eq(1).find('td:nth-child(2)').text($("#box_container>.box").position().top);
	$("#table tbody>tr").eq(2).find('td:nth-child(2)').text($("#box_container>.box").scrollTop());
	$("#table tbody>tr").eq(5).find('td:nth-child(2)').text(document.querySelector("#box_container>.box").getBoundingClientRect().top);
	$("#table tbody>tr").eq(3).find('td:nth-child(2)').text(document.querySelector("#box_container>.box").offsetTop);
	$("#table tbody>tr").eq(4).find('td:nth-child(2)').text(document.querySelector("#box_container>.box").scrollTop);
});

var boxmap_relative = {
	'$("#box").offset().top': $("#box_container_relative>.box").offset().top,
	'$("#box").position().top': $("#box_container_relative>.box").position().top,
	'$("#box").scrollTop()': $("#box_container_relative>.box").scrollTop(),
	'document.querySelector("#box").getBoundingClientRect().top': document.querySelector("#box_container_relative>.box").getBoundingClientRect().top,
	'document.querySelector("#box").offsetTop': document.querySelector("#box_container_relative>.box").offsetTop,
	'document.querySelector("#box").scrollTop': document.querySelector("#box_container_relative>.box").scrollTop,
}

$.each(boxmap_relative, function(key, value){

	$("#table_relative tbody").append("<tr></tr>");
	$("#table_relative tbody > tr:last-child").append("<td>"+key+"</td><td>"+value+"</td>");
});

$('#box_container_relative').scroll(function(){

	$("#table_relative tbody>tr").eq(0).find('td:nth-child(2)').text($("#box_container_relative>.box").offset().top);
	$("#table_relative tbody>tr").eq(1).find('td:nth-child(2)').text($("#box_container_relative>.box").position().top);
	$("#table_relative tbody>tr").eq(2).find('td:nth-child(2)').text($("#box_container_relative>.box").scrollTop());
	$("#table_relative tbody>tr").eq(5).find('td:nth-child(2)').text(document.querySelector("#box_container_relative>.box").getBoundingClientRect().top);
	$("#table_relative tbody>tr").eq(3).find('td:nth-child(2)').text(document.querySelector("#box_container_relative>.box").offsetTop);
	$("#table_relative tbody>tr").eq(4).find('td:nth-child(2)').text(document.querySelector("#box_container_relative>.box").scrollTop);
});
