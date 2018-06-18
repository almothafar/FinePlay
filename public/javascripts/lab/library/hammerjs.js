'use strict';

$(document).ready(function() {

	$('#girlIcon').popover({
		template: '<div class="races-popover popover" role="tooltip"><div class="arrow"></div><h3 class="popover-header"></h3><div class="popover-body"></div></div>',
		trigger: 'manual',
		placement: 'top',
		html: true,
		content: document.querySelector('#races')
	});

	var input = function(){

		var girlIcon = $('#girlIcon');

		var notificationHtml =
			'<div class="alert bg-pink-200 alert-dismissible fade show p-0 d-flex justify-content-start" role="alert">' +
				'<div class="p-2"><i class="w-64p h-64p mt-1 bg-pink-100 rounded fa-3x notificationIcon">' + girlIcon.text() + '</i></div>' +
				'<div class="p-2 d-flex align-items-center"><button type="button" class="p-3 close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button></div>' +
			'</div>';

		notify('<div class="rounded">' + notificationHtml + '</div>', 3000);
	}

	new Hammer($('#girlIcon')[0]).on('tap press', function(e) {

		var girlIcon = $(e.target);

		console.dir(e);
		switch (e.type){
		case 'tap':

			input();
			break;
		case 'press':

			girlIcon.popover('show');
			break;
		default:

			break;
		}
	});

	$('.raceIcon').on('click', function(e){

		var raceIcon = $(e.target);
		var raceChar = raceIcon.text();
		$('#girlIcon').text(raceChar);

		$('#girlIcon').popover('hide');

		input();
	});
});
