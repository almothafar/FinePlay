'use strict';

var video = document.createElement("video");
var canvasElement = document.getElementById("canvas");
var canvas = canvasElement.getContext("2d");
var loadingMessage = document.getElementById("loadingMessage");

function drawLine(begin, end, color) {

	canvas.beginPath();
	canvas.moveTo(begin.x, begin.y);
	canvas.lineTo(end.x, end.y);
	canvas.lineWidth = 4;
	canvas.strokeStyle = color;
	canvas.stroke();
}

navigator.mediaDevices.getUserMedia({

	video : true
}).then(function(stream) {

	$('#cameraNotEnable').remove();

	video.srcObject = stream;
	video.play();
	requestAnimationFrame(tick);
});

function tick() {

	loadingMessage.innerText = "Loading video..."
	if (video.readyState === video.HAVE_ENOUGH_DATA) {

		loadingMessage.hidden = true;
		canvasElement.hidden = false;

		canvasElement.height = video.videoHeight;
		canvasElement.width = video.videoWidth;
		canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);
		var imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);
		var code = jsQR(imageData.data, imageData.width, imageData.height);
		if (code) {

			drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#FF3B58");
			drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#FF3B58");
			drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#FF3B58");
			drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#FF3B58");
			console.dir(code);
			var data = code.data;

			var notificationHtml =
			'<div class="alert bg-orange alert-dismissible fade show p-0 d-flex justify-content-start" role="alert">' +
				'<div class="p-2"><img src="' + canvasElement.toDataURL() + '" class="w-96p h-96p rounded"></div>' +
				'<div class="p-2 align-self-stretch w-100"><strong class="text-blue-grey">Data</strong><br>' + data + '</div>' +
				'<div class="p-2 d-flex align-items-center"><button type="button" class="p-3 close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button></div>' +
			'</div>';

			notify('<div class="rounded">' + notificationHtml + '</div>', 3000);
		}
	}

	setTimeout(function () {

		requestAnimationFrame(tick);
	}, 10);
}
