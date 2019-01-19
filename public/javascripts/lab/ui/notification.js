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
		'<div class="toast alert-progress show mw-100 bg-dark" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">' +
			'<div class="toast-header bg-dark text-light">' +
				'<img src="/assets/images/favicons/apple-touch-icon.png" class="rounded mr-2 w-24p h-24p" alt="...">' +
				'<strong class="mr-auto">Bootstrap</strong>' +
				'<small>11 mins ago</small>' +
				'<button type="button" class="ml-2 mb-1 close text-light" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
			'</div>' +
			'<div class="alert-progress-bar w-100 h-4p d-flex justify-content-start">' +
				'<div class="h-4p bg-orange grow-width"></div>' +
			'</div>' +
			'<div class="toast-body d-flex justify-content-start bg-dark text-light opacity-75">' +
				'Progress' +
			'</div>' +
		'</div>';

	notify('<div class="opacity-75 w-100">' + alertHtml + '</div>', 5000);
});
