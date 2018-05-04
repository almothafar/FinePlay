'use strict';

$(document).ready(function() {

	new Hammer($('.icon-board')[0]).on('tap press', function(e) {

		var target = $(e.target);

		console.dir(e);
		switch (e.type){
		case 'tap':

			var isIconClose = target.hasClass('icon-close');
			if(isIconClose){

				target.closest('.icon-frame-wrapper').remove();
			}

			$('.icon-frame').removeClass('undecided');
			break;
		case 'press':

			var isIcon = target.hasClass('icon-body');
			if(isIcon){

				$('.icon-frame').addClass('undecided');
			}
			break;
		default:

			break;
		}
	});
});
