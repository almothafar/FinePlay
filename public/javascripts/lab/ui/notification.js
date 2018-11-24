'use strict';

$('#alertButton').on('click', function(e){

	notify(messages(MessageKeys.NOTIFICATION));
});
$('#stopAlertButton').on('click', function(e){

	notify(messages(MessageKeys.NOTIFICATION), -1);
});
$('#optionalAlertButton').on('click', function(e){

	notify(messages(MessageKeys.NOTIFICATION), 10000);
});

$('#successAlertButton').on('click', function(e){

	notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + messages(MessageKeys.SUCCESS));
});
$('#infoAlertButton').on('click', function(e){

	notifyAlert('info', '<strong>' + messages(MessageKeys.INFO) + '</strong> ' + messages(MessageKeys.INFO));
});
$('#warningAlertButton').on('click', function(e){

	notifyAlert('warning', '<strong>' + messages(MessageKeys.WARNING) + '</strong> ' + messages(MessageKeys.WARNING));
});
$('#dangerAlertButton').on('click', function(e){

	notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + messages(MessageKeys.DANGER));
});

$('#htmlAlertButton').on('click', function(e){

	var notifyHtml = $('#htmlText').val();
	console.log(notifyHtml);
	var wait = mySlider.bootstrapSlider('getValue') * 1000;
	console.log(wait);

	notify('<div class="rounded w-100">' + notifyHtml + '</div>', wait);
});

var mySlider = $("#waitSlider").bootstrapSlider({

	formatter: function(value) {

		return 'Wait: ' + value;
	}
});

$('#progressAlertButton').on('click', function(e){

	var alertHtml = '' +
		'<div class="alert alert-progress bg-dark text-light opacity-75 rounded-0 fade alert-dismissible show text-left" role="alert">' +
			'<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
				'<span aria-hidden="true">×</span>' +
			'</button>' +
			'Progress' +
			'<div class="alert-progress-bar position-absolute w-100 h-4p d-flex justify-content-start">' +
				'<div class="h-4p bg-orange grow-width"></div>' +
			'</div>' +
		'</div>';

	notify('<div class="bg-secondary opacity-75 w-100">' + alertHtml + '</div>', 5000);
});
