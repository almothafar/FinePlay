'use strict';

var boxmap = {
	'$("#box_container").scrollTop()': $("#box_container").scrollTop(),
	'$("#box").scrollTop()': $("#box_container>.box").offset().top,
	'$("#box_container>.box").position().top': $("#box_container>.box").position().top,
	'document.querySelector("#box_container").scrollTop': document.querySelector("#box_container").scrollTop,
	'document.querySelector("#box_container>.box").getBoundingClientRect().top': document.querySelector("#box_container>.box").getBoundingClientRect().top,
}

$.each(boxmap, function(key, value){

	$("#table tbody").append("<tr></tr>");
	$("#table tbody > tr:last-child").append("<td>"+key+"</td><td>"+value+"</td>");
});

$('#box_container').scroll(function(){

	$("#table tbody>tr").eq(0).find('td:nth-child(2)').text($("#box_container").scrollTop());
	$("#table tbody>tr").eq(1).find('td:nth-child(2)').text($("#box_container>.box").offset().top);
	$("#table tbody>tr").eq(2).find('td:nth-child(2)').text($("#box_container>.box").position().top);
	$("#table tbody>tr").eq(3).find('td:nth-child(2)').text(document.querySelector("#box_container").scrollTop);
	$("#table tbody>tr").eq(4).find('td:nth-child(2)').text(document.querySelector("#box_container>.box").getBoundingClientRect().top);
});

//

var boxmap_relative = {
	'$("#box_container").scrollTop()': $("#box_container_relative").scrollTop(),
	'$("#box").scrollTop()': $("#box_container_relative>.box").offset().top,
	'$("#box_container>.box").position().top': $("#box_container_relative>.box").position().top,
	'document.querySelector("#box_container").scrollTop': document.querySelector("#box_container_relative").scrollTop,
	'document.querySelector("#box_container>.box").getBoundingClientRect().top': document.querySelector("#box_container_relative>.box").getBoundingClientRect().top,
}

$.each(boxmap_relative, function(key, value){

	$("#table_relative tbody").append("<tr></tr>");
	$("#table_relative tbody > tr:last-child").append("<td>"+key+"</td><td>"+value+"</td>");
});

$('#box_container_relative').scroll(function(){

	$("#table_relative tbody>tr").eq(0).find('td:nth-child(2)').text($("#box_container_relative").scrollTop());
	$("#table_relative tbody>tr").eq(1).find('td:nth-child(2)').text($("#box_container_relative>.box").offset().top);
	$("#table_relative tbody>tr").eq(2).find('td:nth-child(2)').text($("#box_container_relative>.box").position().top);
	$("#table_relative tbody>tr").eq(3).find('td:nth-child(2)').text(document.querySelector("#box_container_relative").scrollTop);
	$("#table_relative tbody>tr").eq(4).find('td:nth-child(2)').text(document.querySelector("#box_container_relative>.box").getBoundingClientRect().top);
});
