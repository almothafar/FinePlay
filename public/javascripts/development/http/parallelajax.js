'use strict';

$('#requestButton').click(function() {

	$('#ajaxProgress>.progress-bar').removeClass('bg-danger');
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxDescription').text('-');

	var timeout0 = $('#timeoutField0').val();
	var wait0 = $('#waitTimeField0').val();

	var timeout1 = $('#timeoutField1').val();
	var wait1 = $('#waitTimeField1').val();

	var timeout2 = $('#timeoutField2').val();
	var wait2 = $('#waitTimeField2').val();

	$.when(
		$.ajax({
			method:"POST",
			url:Routes.apis.development.http.Http.ajaxdata().url + "?" + getToken(),
			data:JSON.stringify({
				request: "dummyRequest",
				mock: {
					wait: parseInt(wait0),
					response:{
						"price": 123
					}
				}
			}),
			contentType: 'application/json',
			dataType: "json",
			timeout: timeout0
		})
		.then(
			function (responseJson) {
				return $.Deferred().resolve(responseJson);
			},
			function (responseJson) {
				return $.Deferred().resolve({"price": null});
			}
		),
		$.ajax({
			method:"POST",
			url:Routes.apis.development.http.Http.ajaxdata().url + "?" + getToken(),
			data:JSON.stringify({
				request: "dummyRequest",
				mock: {
					wait: parseInt(wait1),
					response:{
						"price": 456
					}
				}
			}),
			contentType: 'application/json',
			dataType: "json",
			timeout: timeout1
		})
		.then(
			function (responseJson) {
				return $.Deferred().resolve(responseJson);
			},
			function (responseJson) {
				return $.Deferred().resolve({"price": null});
			}
		),
		$.ajax({
			method:"POST",
			url:Routes.apis.development.http.Http.ajaxdata().url + "?" + getToken(),
			data:JSON.stringify({
				request: "dummyRequest",
				mock: {
					wait: parseInt(wait2),
					response:{
						"price": 789
					}
				}
			}),
			contentType: 'application/json',
			dataType: "json",
			timeout: timeout2
		})
		.then(
			function (responseJson) {
				return $.Deferred().resolve(responseJson);
			},
			function (responseJson) {
				return $.Deferred().resolve({"price": null});
			}
		)
	)
	.then(
		function (dealer0Json, dealer1Json, dealer2Json) {

			notifyAlert('info', 'Price '+dealer0Json['price']);
			notifyAlert('info', 'Price '+dealer1Json['price']);
			notifyAlert('info', 'Price '+dealer2Json['price']);

			$('#ajaxDialog').modal('hide');
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxProgress>.progress-bar').addClass('bg-danger');
			$('#ajaxCancel').prop('disabled', false);
			$('#ajaxMessage').text(Messages(MessageKeys.FAILURE));
			$('#ajaxDescription').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});
