'use strict';

$(".removeButtonColumn").css("width", "46px").addClass('d-none');
$(".sortHandleColumn").css("width", "46px").addClass('d-none touch-none');

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

	$(".removeButtonColumn").toggleClass('d-none');
	$(".sortHandleColumn").toggleClass('d-none');

	var isStartEdit = $("#editButton").attr('aria-pressed') == 'false';
	if(isStartEdit){

		$( "#sortable" ).sortable( "enable" );
		$("#editButton").text(messages(MessageKeys.COMPLETE));
	}else{

		$( "#sortable" ).sortable( "disable" );
		$("#editButton").text(messages(MessageKeys.EDIT));

		$("#sortable>tr").each(function(i){

			$(this).find("td").eq(2).text(i);
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
