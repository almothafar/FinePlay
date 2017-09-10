'use strict';

$( "#sendButton" ).on('click', function(e) {

	var socket = new WebSocket(Routes.controllers.lab.application.WebSocket.connect().webSocketURL());

	socket.onopen = function(event){

		var timer = setTimeout(function() {

			socket.send("Hello.");
		}, 1000);
	}

	socket.onclose = function(event){
	}

	socket.onmessage = function(event){

		var time = event.data;
		$('#clock').html(time.replace(/(\d)/g, '<span>$1</span>'))
//		socket.close(1000, "Closed.");
	}

	socket.onerror = function(event){
	}
});
