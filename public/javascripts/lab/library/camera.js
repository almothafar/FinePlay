'use strict';

if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia){$("#MediaDevices").removeClass("badge-danger").addClass("badge-success")}

if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia){

	$('#start').removeAttr("disabled");
}

var playing = false;
var camera = new Camera("#finderFrame", "#pictureFrame", {

	square : true,
	successCallback : function(stream) {

		console.dir(stream);

		$('#start').prop('disabled', true);
		$('#pause').removeAttr("disabled");
		$('#stop').removeAttr("disabled");
		$('#take').removeAttr("disabled");
		playing = true;
	},
	errorCallback : function(err){

		console.dir(err);

		$('#start').removeAttr("disabled");
		$('#pause').prop('disabled', true);
		$('#stop').prop('disabled', true);
		$('#take').prop('disabled', true);
		playing = false;
	}
});

$("#start").on("click", function(){

	camera.start();
});

$("#pause").on("click", function(){

	$('#start').removeAttr("disabled");
	$('#pause').prop('disabled', true);
	$('#stop').prop('disabled', true);
	$('#take').prop('disabled', true);
	playing = false;

	camera.pause();
});

$("#stop").on("click", function(){

	$('#start').removeAttr("disabled");
	$('#pause').prop('disabled', true);
	$('#stop').prop('disabled', true);
	$('#take').prop('disabled', true);
	playing = false;

	camera.stop();
});

$("#take").on("click", function(){

	flash();
	camera.take(function(result){

		console.log(result.data);
		$('#picturePrint').attr('src', result.data);

		$('#printDialog').modal('show');
	});
});
