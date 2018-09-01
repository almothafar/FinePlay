'use strict';

$('#alertButton').on('click', function(e){

	notify(Messages(MessageKeys.NOTIFICATION));
});
$('#stopAlertButton').on('click', function(e){

	notify(Messages(MessageKeys.NOTIFICATION), -1);
});
$('#optionalAlertButton').on('click', function(e){

	notify(Messages(MessageKeys.NOTIFICATION), 10000);
});

$('#successAlertButton').on('click', function(e){

	notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS));
});
$('#infoAlertButton').on('click', function(e){

	notifyAlert('info', '<strong>' + Messages(MessageKeys.INFO) + '</strong> ' + Messages(MessageKeys.INFO));
});
$('#warningAlertButton').on('click', function(e){

	notifyAlert('warning', '<strong>' + Messages(MessageKeys.WARNING) + '</strong> ' + Messages(MessageKeys.WARNING));
});
$('#dangerAlertButton').on('click', function(e){

	notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER));
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
