'use strict';

$(document).ready(function () {

	extendMenu('#toolbar');

	$('#backButton').on('click', function(){

		var currentPosition = getContent().scrollTop();
		var beforePositions = $('div[id^="group_"]').map(function(){

				return offsetTopFromTarget('#system_content', this);
			}).filter(function(){

				return this < currentPosition;
			});
		var beforePosition = beforePositions[beforePositions.length - 1];
		if(beforePosition){

			scrollContent(beforePosition);
		} else {

			scrollContent(0);
		}
	});

	$('#nextButton').on('click', function(){

		var currentPosition = getContent().scrollTop();
		var nextPositions = $('div[id^="group_"]').map(function(){

				return offsetTopFromTarget('#system_content', this);
			}).filter(function(){

				return currentPosition < this;
			});
		var nextPosition = nextPositions[0];
		if(nextPosition){

			scrollContent(nextPosition);
		} else {

			scrollContent(($('#breadcrumbContainer').outerHeight() + 15 + $('#contentsContainer').outerHeight()) - creditHeight());
		}
	});
});
