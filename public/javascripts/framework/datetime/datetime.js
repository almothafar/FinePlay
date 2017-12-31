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

$('.input-group .input-group-text').on('click', function(e){

	$(e.currentTarget).parent().parent().find('.picker__input').trigger("click");
	e.stopPropagation();
});
