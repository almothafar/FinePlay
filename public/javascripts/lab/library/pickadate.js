'use strict';

$('#datepicker').pickadate({
	selectYears: true,
	selectMonths: true,
	format: 'yyyy-mm-dd',
	formatSubmit: 'yyyy-mm-dd'
});

$('#timepicker').pickatime({
	format: 'h:i a',
	formatSubmit: 'HH:i:00.000'
});

$('.input-group .input-group-text').on('click', function(e){

	$(e.currentTarget).parent().parent().find('.picker__input').trigger("click");
	e.stopPropagation();
});

var jsdate =  moment(Messages("clientDateTime")).toDate();

var datepicker = $('#datepicker').pickadate( 'picker' );
datepicker.set('select', jsdate);
datepicker.set('highlight', jsdate);
$('#date').on('click', function(e){

	console.dir($('#datepicker').pickadate( 'picker' ).get());
	alert($('#datepicker').pickadate( 'picker' ).get());
});

var timepicker = $('#timepicker').pickatime( 'picker' );
timepicker.set('select', jsdate);
timepicker.set('highlight', jsdate);
$('#time').on('click', function(e){

	console.dir($('#timepicker').pickatime( 'picker' ).get());
	alert($('#timepicker').pickatime( 'picker' ).get());
});
