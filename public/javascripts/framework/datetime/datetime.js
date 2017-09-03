'use strict';

$('.datePicker').pickadate({
	selectYears: true,
	selectMonths: true,
	format: 'yyyy/mm/dd',
	formatSubmit: 'yyyy-mm-dd'
});

$('.timePicker').pickatime({
	format: 'HH:i',
	formatSubmit: 'HH:i:00'
});

$('.picker__input').each(function(){

	$(this).nextAll('span:first').on('click', function(e){

		$(e.currentTarget).prevAll('.picker__input:first').trigger("click");
		e.stopPropagation();
	});
});
