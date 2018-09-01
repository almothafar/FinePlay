'use strict';

$(document).ready(function () {

	var drawerDirection;
	if(!$('html').hasClass('dir-rtl')){drawerDirection = "right";}else{drawerDirection = "left";}

	extendSurface('#drawer');
	var surface = $('#drawer').parent();
	var surfaceId = surface.attr('id');

	var drawerStyle = {
		"width": '260px',
		"height": '80%',
		"top": '10%',
		"bottom": '10%',
		"overflow": 'hidden'
	};
	drawerStyle[drawerDirection] = 0;
	surface.addClass('d-none').css(drawerStyle);

	$("#drawerCloseButton").on('click', function(event) {

		hideToEnd('#drawer', function(){

			$('#' + surfaceId).addClass('d-none');
		});
	});

	$("#drawerButton").on('click', function(event) {

		$('#' + surfaceId).removeClass('d-none');
		showFromEnd('#drawer');
	});
});
