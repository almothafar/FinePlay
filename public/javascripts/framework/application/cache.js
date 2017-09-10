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

			notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS), 1000);

			$('#setSyncKeyField').val(responseJson.key);
			$('#setSyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER), 1000);
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

			notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS), 1000);

			$('#setSyncKeyField').val(responseJson.key);
			$('#setSyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER), 1000);
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

			notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS), 1000);

			$('#setASyncKeyField').val(responseJson.key);
			$('#setASyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER), 1000);
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

			notifyAlert('success', '<strong>' + Messages(MessageKeys.SUCCESS) + '</strong> ' + Messages(MessageKeys.SUCCESS), 1000);

			$('#setASyncKeyField').val(responseJson.key);
			$('#setASyncValueField').val(responseJson.value);
		},
		function () {

			notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + Messages(MessageKeys.DANGER), 1000);
		}
	);
});
