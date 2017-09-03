'use strict';

$(document).ready(function () {

	$('#numericButton').on('click', function(){

		showMagnifyText($('#numericField').val());
	});

	$('#alphaButton').on('click', function(){

		showMagnifyText($('#alphaField').val());
	});
});
