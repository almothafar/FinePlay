'use strict';

$(document).ready(function() {

	$('#progressingButton').on('click', function(){

		var startButton = $(this);
		startButton.prop('disabled', true);

		var progressBarRate = $('#progressingBar>.progress-bar');
		progressBarRate.css('width', '0%');

		setTimeout(function() {

			var progress = 0;
			var timer = setInterval(function() {

				if(100 == progress){

					clearInterval(timer);
					startButton.prop('disabled', false);
					return;
				}

				progress++;
				progressBarRate.css('width', progress + '%');
			}, 30);
		}, 600);
	});
});