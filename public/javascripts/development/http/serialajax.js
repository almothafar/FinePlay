'use strict';

$('#requestButton').click(function() {

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout0 = $('#timeoutField0').val();
	var wait0 = $('#waitTimeField0').val();

	var timeout1 = $('#timeoutField1').val();
	var wait1 = $('#waitTimeField1').val();

	var timeout2 = $('#timeoutField2').val();
	var wait2 = $('#waitTimeField2').val();

	var deferred = $.Deferred();
	deferred.resolve()
	.then(
		function() {

			return $.ajax({
				method:"POST",
				url:Routes.apis.development.http.Http.postData().url + "?" + getToken(),
				data:JSON.stringify({
					request: "dummyRequest",
					mock: {
						wait: parseInt(wait0),
						response:{
							"response": messages(MessageKeys.RESPONSE) + " 0"
						}
					}
				}),
				contentType: 'application/json',
				dataType: "json",
				timeout: timeout0
			})
			.then(
				function (responseJson) {
					notifyAlert('info', responseJson['response']);
				}
			);
		}
	)
	.then(
		function() {

			return $.ajax({
				method:"POST",
				url:Routes.apis.development.http.Http.postData().url + "?" + getToken(),
				data:JSON.stringify({
					request: "dummyRequest",
					mock: {
						wait: parseInt(wait1),
						response:{
							"response": messages(MessageKeys.RESPONSE)+ " 1"
						}
					}
				}),
				contentType: 'application/json',
				dataType: "json",
				timeout: timeout1
			})
			.then(
				function (responseJson) {
					notifyAlert('info', responseJson['response']);
				}
			);
		}
	)
	.then(
		function() {

			return $.ajax({
				method:"POST",
				url:Routes.apis.development.http.Http.postData().url + "?" + getToken(),
				data:JSON.stringify({
					request: "dummyRequest",
					mock: {
						wait: parseInt(wait2),
						response:{
							"response": messages(MessageKeys.RESPONSE) + " 2"
						}
					}
				}),
				contentType: 'application/json',
				dataType: "json",
				timeout: timeout2
			})
			.then(
				function (responseJson) {
					notifyAlert('info', responseJson['response']);
				}
			);
		}
	)
	.then(
		function () {

			$('#ajaxDialog').modal('hide');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});
