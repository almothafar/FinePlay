'use strict';

$(document).ready(function () {

	var faders = $('.article>.article-content>.article-content-fader');

	var fader0 = faders.eq(0);
	var fader0Color = fader0.closest('body').css('background-color')
	fader0.css('background', 'linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, ' + fader0Color + ' 100%)')

	var fader1 = faders.eq(1);
	var fader1Color = fader1.closest('.card').css('background-color')
	fader1.css('background', 'linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, ' + fader1Color + ' 100%)')

	$('.expandButton').on('click', function(){

		var expandButton = $(this);

		expandButton.closest('.article').removeClass('folding');
		expandButton.addClass('d-none');
	});
});
