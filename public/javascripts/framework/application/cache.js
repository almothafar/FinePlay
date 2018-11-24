'use strict';

// Sync

$('#setSyncButton').click(function() {

	var key = $('#setSyncKeyField').val();
	var value = $('#setSyncValueField').val();
	$.ajax({
		method:"POST",
		url:Routes.controllers.framework.application.Application.synccache().url + "?" + getToken(),
		data:JSON.stringify({ key: key, value: value }),
		contentType: 'application/json',
		dataType: "json"
	})
	.then(
		function (responseJson) {

			notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + messages(MessageKeys.SUCCESS), 1000);

			$('#setSyncKeyField').val(responseJson.key);
			$('#setSyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + messages(MessageKeys.DANGER), 1000);
		}
	);
});

// ASync

$('#getSyncButton').click(function() {

	var key = $('#getSyncKeyField').val();
	$.ajax({
		method:"POST",
		url:Routes.controllers.framework.application.Application.synccache().url + "?" + getToken(),
		data:JSON.stringify({ key: key }),
		contentType: 'application/json',
		dataType: "json"
	})
	.then(
		function (responseJson) {

			notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + responseJson.key + ":" + responseJson.value, 1000);

			$('#setSyncKeyField').val(responseJson.key);
			$('#setSyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + messages(MessageKeys.DANGER), 1000);
		}
	);
});

$('#setASyncButton').click(function() {

	var key = $('#setASyncKeyField').val();
	var value = $('#setASyncValueField').val();
	$.ajax({
		method:"POST",
		url:Routes.controllers.framework.application.Application.asynccache().url + "?" + getToken(),
		data:JSON.stringify({ key: key, value: value }),
		contentType: 'application/json',
		dataType: "json"
	})
	.then(
		function (responseJson) {

			notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + responseJson.key + ":" + responseJson.value, 1000);

			$('#setASyncKeyField').val(responseJson.key);
			$('#setASyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + messages(MessageKeys.DANGER), 1000);
		}
	);
});

$('#getASyncButton').click(function() {

	var key = $('#getASyncKeyField').val();
	$.ajax({
		method:"POST",
		url:Routes.controllers.framework.application.Application.asynccache().url + "?" + getToken(),
		data:JSON.stringify({ key: key }),
		contentType: 'application/json',
		dataType: "json"
	})
	.then(
		function (responseJson) {

			notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + responseJson.key + ":" + responseJson.value, 1000);

			$('#setASyncKeyField').val(responseJson.key);
			$('#setASyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + messages(MessageKeys.DANGER), 1000);
		}
	);
});
