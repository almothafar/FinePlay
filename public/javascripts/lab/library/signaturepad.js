'use strict';

$(document).ready(function() {

	var wrapper = document.getElementById("signature-pad");
	var clearButton = wrapper.querySelector("#clearButton");
	var savePNGButton = wrapper.querySelector("#saveButton");
	var canvas = wrapper.querySelector("canvas");
	var signaturePad = new SignaturePad(canvas, {
		backgroundColor: 'rgb(255, 255, 255)'
	});

	function resizeCanvas() {

		var ratio =	Math.max(window.devicePixelRatio || 1, 1);

		canvas.width = canvas.offsetWidth * ratio;
		canvas.height = canvas.offsetHeight * ratio;
		canvas.getContext("2d").scale(ratio, ratio);

		signaturePad.clear();
	}

	window.onresize = resizeCanvas;
	resizeCanvas();

	function download(dataURL, filename) {

		console.log(filename);
		console.log(dataURL);

		bootbox.dialog({
			title: filename,
			centerVertical: true,
			message: '<image src="' + dataURL + '" class="w-100"></image>'
		});
	}

	clearButton.addEventListener("click", function (event) {
		signaturePad.clear();
	});

	savePNGButton.addEventListener("click", function (event) {
		if (signaturePad.isEmpty()) {
			alert("Please provide a signature first.");
		} else {
			var dataURL = signaturePad.toDataURL();
			download(dataURL, "signature.png");
		}
	});
});
