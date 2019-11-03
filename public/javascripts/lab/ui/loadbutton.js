'use strict';

$(document).ready(function () {

	var load = function(selector, icon){

		var lodingIcon = 'fas fa-circle-notch fa-spin';
		var iconSelector = selector + '>i';

		$(selector).attr('disabled', true);
		$(iconSelector).removeClass(icon);
		$(iconSelector).addClass(lodingIcon);

		$.ajax({
			method:"POST",
			url: Routes.apis.development.http.Http.postData().url + "?" + getToken(),
			data:JSON.stringify({
				request: "dummyRequest",
				mock: {
					wait:2*1000,
					response:{
					}
				}
			}),
			contentType: 'application/json',
			dataType: "json",
			timeout: 10 * 1000
		})
		.then(
			function (responseJson) {

				notifyAlert('success', '<strong>' + messages(MessageKeys.SUCCESS) + '</strong> ' + selector, 1000);

				$(iconSelector).removeClass(lodingIcon);
				$(iconSelector).addClass(icon);
				$(selector).attr('disabled', false);
			},
			function () {

				notifyAlert('danger', '<strong>' + messages(MessageKeys.DANGER) + '</strong> ' + selector, 1000);

				$(iconSelector).removeClass(lodingIcon);
				$(iconSelector).addClass(icon);
				$(selector).attr('disabled', false);
			}
		);
	};

	$('#loadButton0').on('click', function(){

		load('#loadButton0', 'fas fa-check-circle');
	});

	$('#loadButton1').on('click', function(){

		load('#loadButton1', 'fas fa-info-circle');
	});

	$('#loadButton2').on('click', function(){

		load('#loadButton2', 'fas fa-exclamation-triangle');
	});

	$('#loadButton3').on('click', function(){

		load('#loadButton3', 'fas fa-ban');
	});
});
