'use strict';

$(document).ready(function () {

	injectStyle('.contentFrame:nth-child(1)>.imageContent', {
		'background-image': 'url(' + Messages("img1") + ')'
	});
	injectStyle('.contentFrame:nth-child(2)>.imageContent', {
		'background-image': 'url(' + Messages("img2") + ')'
	});
	injectStyle('.contentFrame:nth-child(3)>.imageContent', {
		'background-image': 'url(' + Messages("img3") + ')'
	});
	injectStyle('.contentFrame:nth-child(4)>.imageContent', {
		'background-image': 'url(' + Messages("img4") + ')'
	});
	injectStyle('.contentFrame:nth-child(5)>.imageContent', {
		'background-image': 'url(' + Messages("img5") + ')'
	});

	var breadcrumbHeight = 15 + $('.breadcrumb').outerHeight(true); //79

	var redraw = function(){

		var contentFrames = $('.contentFrame');
		contentFrames.each(function(frameIndex){

			var image = $(this).find('.imageContent');
			var imageTop = image.offset().top;
			var imageHeight = image.outerHeight();

			var framePosition = $('.contentFrame').eq(frameIndex).offset().top;
			var reviseFramePosition = framePosition - menuHeight() - breadcrumbHeight; //0 〜 -600

			image.css('background-position-y', reviseFramePosition / 10);

			var text = $(this).find('.textContent');
			text.css('left', reviseFramePosition + 'px');
		});
	}

	getContent().scroll(function(e){

		redraw();
	});

	var timer = false;
	$(window).resize(function() {

		if (timer !== false) {

			clearTimeout(timer);
			return;
		}
		timer = setTimeout(function() {

			redraw();
		}, 100);
	});

	redraw();
});
