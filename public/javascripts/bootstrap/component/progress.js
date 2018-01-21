'use strict';

$(document).ready(function() {

	$('#progressingButton').on('click', function() {

		var startButton = $(this);

		var progressRate = $('#progressRate').val();

		var progressBar = $('#progressingBar>.progress-bar');
		progressBar.css('width', progressRate + '%');
	});
});