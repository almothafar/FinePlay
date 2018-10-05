'use strict';

$(document).ready(function() {

	var socket;

	$("#enterButton").on('click', function(e) {

		socket = new WebSocket(Routes.controllers.lab.application.Chat.enter().webSocketURL());

		socket.onopen = function(event) {
		}

		socket.onclose = function(event) {
		}

		socket.onmessage = function(event) {

			var message = JSON.parse(event.data);
			var messageBallon =
				'<div class="d-flex justify-content-start mb-2"><div class="card bg-light balloon-start-top rounded-pretty"><div class="card-body p-2">' +
					'<p class="card-title mb-0"><small>' + message.sender + '</small></p>' +
					'<p class="card-text">' + message.text + '</p>' +
				'</div></div></div>';

			$('#messages').prepend(messageBallon)
		}

		socket.onerror = function(event) {
		}

		$('#enterView').addClass('d-none');
		$('#roomView').removeClass('d-none');
	});

	$("#sendButton").on('click', function(e) {

		var messageText = $('#messageField').val();
		if(0 == messageText.trim().length){

			notifyAlert('info', 'Message is empty.');
		}else{

			var messageBallon =
				'<div class="d-flex justify-content-end mb-2"><div class="card bg-primary balloon-end-top rounded-pretty"><div class="card-body p-2">' +
					'<p class="card-title mb-0"><small>' + 'Self' + '</small></p>' +
					'<p class="card-text">' + messageText + '</p>' +
				'</div></div></div>';

			$('#messages').prepend(messageBallon)

			socket.send(messageText);
		}
	});
});
