'use strict';

$(document).ready(function () {

	extendSurface('#drawer');
	var surface = $('#drawer').parent();
	var surfaceId = surface.attr('id');

	surface.addClass('d-none').css({
		"width": '260px',
		"height": '80%',
		"top": '10%',
		"right": 0,
		"bottom": '10%',
		"overflow": 'hidden'
	});

	$("#drawerCloseButton").on('click', function(event) {

		hideToRight('#drawer', function(){

			$('#' + surfaceId).addClass('d-none');
		});
	});

	$("#drawerButton").on('click', function(event) {

		$('#' + surfaceId).removeClass('d-none');
		showFromRight('#drawer');
	});
});
