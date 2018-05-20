'use strict';

$(document).ready(function() {

	$('#progressRate').on('input change', function() {

		var progressRate = $('#progressRate').val();

		var progressBar = $('#progressingBar>.progress-bar');
		progressBar.css('width', progressRate + '%');
	});
});