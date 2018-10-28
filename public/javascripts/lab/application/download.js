'use strict';

$('#ua').text(navigator.userAgent);

if (window.URL) {

	$("#URL").removeClass("badge-danger").addClass("badge-success")
}
if ("" == document.createElement('a').download) {

	$("#DownloadAttribute").removeClass("badge-danger").addClass(
			"badge-success")
}

var blob = new Blob([ "(｀・ω・´)\r\n(*´ω｀*)\r\n(´･ω･`)\r\n" ], {

	type : 'application/octet-binary'
});
var url = URL.createObjectURL(blob);
$("#local").attr("href", url);

$("#createIVDButton").on('click', function() {

	$("#createIVDButton").prop('disabled', true);
	$("#createIVDIcon").removeClass('d-none');

	var jobName = 'batch-createivd-job';

	var timeout = "10000";
	$.ajax({
		method:"GET",
		url: Routes.apis.batch.Batch.start(jobName, Messages("encodedUserId")).url + "?" + getToken(),
		data: $.param({
		}),
		contentType: 'text/plain',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			if(responseJson["error"]){

				notifyAlert('warning', '<strong>' + Messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
			}else{

				console.dir(responseJson);

				var jobName = responseJson["jobName"];
				var executionId = responseJson["executionId"];

				var timerId = setInterval(function(){

					var timeout = "10000";
					$.ajax({
						method:"GET",
						url: Routes.apis.batch.Batch.jobExecution(executionId, Messages("encodedUserId")).url + "?" + getToken(),
						data: $.param({
						}),
						contentType: 'text/plain',
						dataType: "json",
						timeout: timeout
					})
					.then(
						function (responseJson) {

							if(responseJson["error"]){

								clearInterval(timerId);
								notifyAlert('warning', '<strong>' + Messages(MessageKeys.WARNING) + '</strong> ' + responseJson["error"], 10000);
							}else{

								console.dir(responseJson);

								var jobExecution = responseJson["jobExecution"];
								var batchStatus = jobExecution["batchStatus"];

								switch(batchStatus){
									case "ABANDONED":
									case "FAILED":

										clearInterval(timerId);
										notifyAlert('warning', "Creating IVD... BatchStatus:" + batchStatus, 10000);
										break;
									case "STOPPING":
									case "STOPPED":
									case "STARTING":
									case "STARTED":

										notifyAlert('info', "Creating IVD... BatchStatus:" + batchStatus, 5000);
										break;
									case "COMPLETED":
									default:

										clearInterval(timerId);
										$("#createIVDButton").prop('disabled', false).addClass('d-none');
										$("#createIVDIcon").addClass('d-none');
										$('#downloadIVDButton').attr('href', $('#downloadIVDButton').attr('href').replace('-1', executionId));
										$('#downloadIVDButton').removeClass('d-none');
										break;
								};
							}
						},
						function (jqXHR, textStatus, errorThrown) {

							clearInterval(timerId);
							notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
						}
					);
				}, 5000);
			}
		},
		function (jqXHR, textStatus, errorThrown) {

			notifyAlert('danger', '<strong>' + Messages(MessageKeys.DANGER) + '</strong> ' + errorThrown, 10000);
		}
	);
});