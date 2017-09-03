'use strict';

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		location.href = $('#topButton').attr("href");

		e.preventDefault();
	}
});
