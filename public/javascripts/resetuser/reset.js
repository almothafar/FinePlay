'use strict';

if(Messages("hasErrors")) {

	shake('#resetPanel');
}

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		$('#applyButton').trigger("click");

		e.preventDefault();
	}
});
