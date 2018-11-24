'use strict';

if(messages("hasErrors")) {

	shake('#changePanel');
}

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		$('#changeButton').trigger("click");

		e.preventDefault();
	}
});
