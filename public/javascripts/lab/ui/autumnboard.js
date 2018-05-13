'use strict';

$(document).ready(function() {

	$('#unlockView').on('slid.fs.unlock', function (e) {
		console.dir(e);

		$('#lockView').removeClass('d-flex').addClass('d-none');
		$('#homeView').removeClass('d-none');
		fadeInFromFront('#homeView');
	})

	$('.icon-board').sortable({});
	$('.icon-board').sortable('disable');
	new Hammer($('.icon-board')[0]).on('tap press', function(e) {
		console.dir(e);

		var target = $(e.target);

		switch (e.type){
		case 'tap':

			var isIconClose = target.hasClass('icon-close');
			if(isIconClose){

				target.closest('.icon-frame-wrapper').remove();
			}

			$('.icon-board').sortable('disable');
			$('.icon-frame').removeClass('undecided');
			break;
		case 'press':

			var isIcon = target.hasClass('icon-body');
			if(isIcon){

				$('.icon-board').sortable('enable');
				$('.icon-frame').addClass('undecided');
			}
			break;
		default:

			break;
		}
	});
});
