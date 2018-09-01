'use strict';

$('#alertButton').on('click', function(e){

	tell(Messages(MessageKeys.ALERT));
});
$('#stopAlertButton').on('click', function(e){

	tell(Messages(MessageKeys.ALERT), -1);
});
$('#optionalAlertButton').on('click', function(e){

	tell(Messages(MessageKeys.ALERT), 10000);
});

$('#successAlertButton').on('click', function(e){

	tellAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS));
});
$('#infoAlertButton').on('click', function(e){

	tellAlert('info', '<strong>' + Messages(MessageKeys.INFO) + '</strong> ' + Messages(MessageKeys.INFO));
});
$('#warningAlertButton').on('click', function(e){

	tellAlert('warning', '<strong>' + Messages(MessageKeys.WARNING) + '</strong> ' + Messages(MessageKeys.WARNING));
});
$('#dangerAlertButton').on('click', function(e){

	tellAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER));
});

$('#htmlAlertButton').on('click', function(e){

	var alertHtml = $('#htmlText').val();
	console.log(alertHtml);
	var wait = mySlider.bootstrapSlider('getValue') * 1000;
	console.log(wait);

	tell('<div class="rounded w-100">' + alertHtml + '</div>', wait);
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

	tell('<div class="bg-secondary opacity-75 w-100">' + alertHtml + '</div>', 5000);
});
