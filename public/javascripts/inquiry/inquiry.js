'use strict';

if(Messages("hasErrors")) {

	shake('#inquiryPanel');
}

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		$('#sendButton').trigger("click");

		e.preventDefault();
	}
});

$('#inquiryForm').parsley();

window.Parsley.on('form:error', function() {

	shake('#inquiryPanel');
});
