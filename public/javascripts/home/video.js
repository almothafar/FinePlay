'use strict';

var videoFrame = $('#trainingVideo')[0];
videoFrame.requestFullscreen = videoFrame.requestFullscreen || videoFrame.webkitRequestFullscreen || videoFrame.mozRequestFullScreen || videoFrame.msRequestFullscreen;
if(videoFrame.requestFullscreen){

	$('#fullScreenButtonContainer').removeClass("d-none");
	$('#fullScreenButton').on('click', function(){

		videoFrame.requestFullscreen()
	});
}
