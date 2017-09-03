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

$('#richAlertButton').on('click', function(e){

	notify(''+
		'<div class="alert alert-rich alert-dismissible fade show p-0 d-flex justify-content-start" role="alert">' +
			'<div class="p-2"><img id="richAlertImg" src="' + Messages("img") + '" class="rounded"></div>' +
			'<div class="p-2 align-self-stretch w-100">' +
				'<strong>Rich notification!</strong><br> <a href="@controllers.home.routes.Home.index()" class="alert-link">Link</a> The quick brown fox jumps over the lazy dog.' +
			'</div>' +
			'<div class="p-2 d-flex align-items-center">' +
				'<button type="button" class="p-3 close" data-dismiss="alert" aria-label="Close">' +
					'<span aria-hidden="true">×</span><span class="sr-only">Close</span>' +
				'</button>' +
			'</div>' +
		'</div>'
	, 10000);
});

var ckEditor = CKEDITOR.replace( 'htmlEditor', {

	height: 100
});

var mySlider = $("#waitSlider").bootstrapSlider({

	formatter: function(value) {

		return 'Wait: ' + value;
	}
});

$('#htmlNotificationButton').on('click', function(e){

	var notificationHtml = ckEditor.getData();
	console.log(notificationHtml);
	var wait = mySlider.bootstrapSlider('getValue') * 1000;
	console.log(wait);

	notify('<div class="rounded w-100">' + notificationHtml + '</div>', wait);
});
