'use strict';

$(document).ready(function () {

	$('#blockButton').on('click', function(){

		showCover();
		setTimeout(function(){

			hideCover();
		}, 3000)
	});
});
