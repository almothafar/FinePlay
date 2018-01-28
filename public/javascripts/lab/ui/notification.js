'use strict';

$('#notificationButton').on('click', function(e){

	notify(Messages(MessageKeys.NOTIFICATION), 10000);
});

$('#successAlertButton').on('click', function(e){

	notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS), 10000);
});
$('#infoAlertButton').on('click', function(e){

	notifyAlert('info', '<strong>' + Messages(MessageKeys.INFO) + '</strong> ' + Messages(MessageKeys.INFO), 10000);
});
$('#warningAlertButton').on('click', function(e){

	notifyAlert('warning', '<strong>' + Messages(MessageKeys.WARNING) + '</strong> ' + Messages(MessageKeys.WARNING), 10000);
});
$('#dangerAlertButton').on('click', function(e){

	notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER), 10000);
});

$('#htmlAlertButton').on('click', function(e){

	var notificationHtml = $('#htmlText').val();
	console.log(notificationHtml);
	var wait = mySlider.bootstrapSlider('getValue') * 1000;
	console.log(wait);

	notify('<div class="rounded w-100">' + notificationHtml + '</div>', wait);
});

var mySlider = $("#waitSlider").bootstrapSlider({

	formatter: function(value) {

		return 'Wait: ' + value;
	}
});
