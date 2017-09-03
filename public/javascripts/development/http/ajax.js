'use strict';

$('#requestButton').click(function() {

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout = $('#timeoutField').val();
	var wait = $('#waitTimeField').val();
	$.ajax({
		method: "POST",
		url: Routes.apis.development.http.Http.ajaxdata().url + "?" + getToken(),
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
			$('#ajaxMessage').text(Messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(Messages(MessageKeys.STATUS)+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+Messages(MessageKeys.ERROR)+'&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});
