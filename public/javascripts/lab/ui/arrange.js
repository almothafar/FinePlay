'use strict';

$(".removeButtonColumn").css("width", "46px").hide();
$(".sortHandleColumn").css("width", "46px").hide();

$(function() {

	$( "#sortable" ).sortable({

		start: function( event, ui ) {

			var placeholderTds = ui.placeholder.find('>td');
			var itemTds = ui.item.find('>td');
			itemTds.eq(1).css('width', placeholderTds.eq(1).width());
			itemTds.eq(2).css('width', placeholderTds.eq(2).width());
		},
		update: function( event, ui ) {

			console.log("updated.");
		}
	});
	$( "#sortable" ).sortable( "disable" );
	$( "#sortable" ).disableSelection();
});

$("#editButton").on('click', function(e) {

	$(".removeButtonColumn").toggle();
	$(".sortHandleColumn").toggle();

	var isStartEdit = $("#editButton").attr('aria-pressed') == 'false';
	if(isStartEdit){

		$( "#sortable" ).sortable( "enable" );
		$("#editButton").text(Messages(MessageKeys.COMPLETE));
	}else{

		$( "#sortable" ).sortable( "disable" );
		$("#editButton").text(Messages(MessageKeys.EDIT));

		$("#sortable>tr").find("td:eq(2)").each(function(i){

			$(this).text(i);
		});
	}
});

$( "#sortable" ).on('click', function(e) {

	var eventSource = $(e.target);

	var isRemoveButton = eventSource.hasClass("fa-minus-circle");
	if(isRemoveButton){

		var tr = eventSource.parent().parent();
		$(tr).animate({
			opacity: "0"
		}, 100, function() {

			tr.remove();
		});
	}
});
