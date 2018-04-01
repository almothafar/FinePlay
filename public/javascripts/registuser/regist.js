'use strict';

var currentDate = new Date();

var offsetSecond = currentDate.getTimezoneOffset() * -1 * 60;
$('#offsetSecond').attr('value', offsetSecond);

var shortZoneId = (/.*\((.*)\).*/.exec(currentDate.toString())||[])[1];
if(shortZoneId){
	$('#shortZoneId').attr('value', shortZoneId);
}

if(Messages("hasErrors")) {

	shake('#registPanel');
}

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		$('#applyButton').trigger("click");

		e.preventDefault();
	}
});
