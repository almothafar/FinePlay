'use strict';

$('#getButton').click(function() {

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = $('#timeoutField').val();
	var wait = $('#waitTimeField').val();
	$.ajax({
		method:"GET",
		url: Routes.apis.development.http.Http.getData().url + "?" + getToken(),
		data: $.param({
			request: "dummyRequest",
			mock: JSON.stringify({
				wait: parseInt(wait),
				response:{
				}
			})
		}),
		contentType: 'application/json',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#ajaxDialog').modal('hide');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(messages(MessageKeys.STATUS)+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+messages(MessageKeys.ERROR)+'&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});

$('#postButton').click(function() {

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = $('#timeoutField').val();
	var wait = $('#waitTimeField').val();
	$.ajax({
		method: "POST",
		url: Routes.apis.development.http.Http.postData().url + "?" + getToken(),
		data: JSON.stringify({
			request: "dummyRequest",
			mock: {
				wait: parseInt(wait),
				response:{
				}
			}
		}),
		contentType: 'application/json',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#ajaxDialog').modal('hide');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(messages(MessageKeys.STATUS)+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+messages(MessageKeys.ERROR)+'&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});
