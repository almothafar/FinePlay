'use strict';

$(document).ready(function() {

	var campaignWidths = $('.campaign').map(function() {
		return $(this).outerWidth(true);
	}).get();
	var campaignsWidth = campaignWidths.reduce(function(prev, current, i, arr) {
		return prev + current;
	});
	$('#campaigns').width(campaignsWidth + 16);

	var isLTR = $('html').hasClass('dir-ltr');
	var centering = function() {

		var center = $('#campaigns-wrapper').width() / 2;
		var campaignHalfWith = $('.campaign').width() / 2;
		var centeringTarget = $('.campaign').filter(function() {

			var campaignElem = $(this);
			var campaignLeftPosition = campaignElem.position().left;
			var campaignCenter = campaignLeftPosition + campaignHalfWith;

			var start = center - campaignHalfWith;
			var end = center + campaignHalfWith;

			return start <= campaignCenter && campaignCenter <= end;
		});
		var centeringTargetIndex = centeringTarget.index();

		var leftWidthsOfCenteringTarget = $('.campaign').slice(0, centeringTargetIndex).map(function() {
			return $(this).outerWidth(true);
		}).get();
		var leftWidthOfCenteringTarget = leftWidthsOfCenteringTarget.length == 0 ? 0 : leftWidthsOfCenteringTarget.reduce(function(prev, current, i, arr) {
			return prev + current;
		});

		var positionOfWrapper = center - campaignHalfWith;
		var scrollPosition = positionOfWrapper - leftWidthOfCenteringTarget;

		if (isLTR) {

			scrollPosition = scrollPosition * -1;
		} else {

			switch (jQuery.support.rtlScrollType) {
				case "default":
					scrollPosition = $('#campaigns').width() + scrollPosition - $('.campaign').width() - campaignHalfWith;
					break;
				case "negative":
					scrollPosition = scrollPosition * +1;
					break;
				case "reverse":
					scrollPosition = scrollPosition * -1;
					break;
			}
		}
		$('#campaigns-wrapper').animate({
			scrollLeft: scrollPosition
		}, 300, 'swing');
	}

	var timeId;
	var scrollLeft;
	var waitStop = function() {

		scrollLeft = $('#campaigns-wrapper').scrollLeft();
		timeId = setTimeout(function() {

			var currentScrollLeft = $('#campaigns-wrapper').scrollLeft();
			if (scrollLeft != currentScrollLeft) {

				clearTimeout(timeId);
				scrollLeft = currentScrollLeft;
				waitStop();
				return;
			}

			centering();
		}, 100);
	}

	$('#campaigns-wrapper').on('touchend', function(e) {

		waitStop();
	});
});
