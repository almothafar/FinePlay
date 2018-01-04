'use strict';

$(document).ready(function () {

	$('.expandButton').on('click', function(){

		var expandButton = $(this);

		expandButton.closest('.article').removeClass('folding');
		expandButton.addClass('d-none');
	});
});
