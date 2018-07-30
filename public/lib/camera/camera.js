var Camera = function(finderFrameSelector, pictureFrameSelector, options) {

	console.warn("unstable");

	// finder
	var finderFrame = document.querySelector(finderFrameSelector);

	if (finderFrame == null) {

		throw "finderFrame is null.";
	}

	finderFrame.style.overflow = "hidden";

	var finderFrameHeight = finderFrame.offsetHeight;
	var finderFrameWidth = finderFrame.offsetWidth;

	var finder = document.createElement('video');
	finder.height = finderFrameHeight;
	finder.width = finderFrameWidth;
	finderFrame.appendChild(finder);

	// picture
	var pictureFrame = document.querySelector(pictureFrameSelector);

	if (pictureFrame == null) {

		throw "pictureFrame is null.";
	}

	var pictureFrameHeight = pictureFrame.offsetHeight;
	var pictureFrameWidth = pictureFrame.offsetWidth;

	var picture = document.createElement('canvas');
	picture.height = pictureFrameHeight;
	picture.width = pictureFrameWidth;
	pictureFrame.appendChild(picture);

	var defaults = {

		square: false,
		successCallback: function(stream) {
			console.dir(stream);
		},
		errorCallback: function(error) {
			console.dir(error);
		}
	}

	if (options) {

		if (options.square) {
			defaults.square = options.square;
		}
		if (options.successCallback) {
			defaults.successCallback = options.successCallback;
		}
		if (options.errorCallback) {
			defaults.errorCallback = options.errorCallback;
		}
	}

	if (defaults.square && (finder.height != finder.width)) {

		throw "finderFrame is not square.";
	}

	var videoWidth;
	var videoHeight;

	var stream;

	var track;

	this.start = function() {

		navigator.mediaDevices.getUserMedia({

			video: true,
			audio: false
		}).then(function(stream) {

			finder.srcObject = stream;
			finder.play();
			track = stream.getVideoTracks()[0];

			var timer = setInterval(function() {

				if (finder.videoWidth != 0 && finder.videoHeight != 0) {

					clearInterval(timer);

					videoWidth = finder.videoWidth;
					videoHeight = finder.videoHeight;

					if (defaults.square) {

						var displayVideoScale;
						var videoScale;
						if (videoWidth > videoHeight) {

							var displayVideoScale = finder.width / videoWidth;
							videoScale = finderFrameHeight / (videoHeight * displayVideoScale);
						} else if (videoWidth < videoHeight) {

							var displayVideoScale = finder.height / videoHeight;
							videoScale = finderFrameWidth / (videoWidth * displayVideoScale);
						}

						finder.style.transform = "scale(" + videoScale + ")";
					}
				}
			}, 100);

			defaults.successCallback(stream);
		}).catch(function(error) {

			defaults.errorCallback(error);
		})
	}

	this.take = function(takeCallback) {

		if (finder.paused) {

			return;
		}

		var videoX = 0;
		var videoY = 0;

		var cropVideoX = videoX;
		var cropVideoY = videoY;
		var cropVideoWidth = videoWidth;
		var cropVideoHeight = videoHeight;

		if (defaults.square) {

			if (videoWidth > videoHeight) {

				cropVideoX = (videoWidth - videoHeight) / 2;
				cropVideoWidth = videoHeight;
			} else if (videoWidth < videoHeight) {

				cropVideoY = (videoHeight - videoWidth) / 2;
				cropVideoHeight = videoWidth;
			}
		}

		picture.width = cropVideoWidth;
		picture.height = cropVideoHeight;

		var pictureX = 0;
		var pictureY = 0;
		var pictureWidth = picture.width;
		var pictureHeight = picture.height;

		var widthPicutureScale = pictureFrameWidth / pictureWidth;
		var heightPicutureScale = pictureFrameHeight / pictureHeight;
		var picutureScale = Math.min(widthPicutureScale, heightPicutureScale);
		picture.style.transform = "scale(" + picutureScale + ")";

		var picutureScaleX = 0;
		var picutureScaleY = 0;
		if (widthPicutureScale == picutureScale) {

			picutureScaleY = (pictureFrameHeight - (cropVideoHeight * picutureScale));
		} else if (heightPicutureScale == picutureScale) {

			picutureScaleX = (pictureFrameWidth - (cropVideoWidth * picutureScale));
		}
		picture.style.transformOrigin = picutureScaleX + "px " + picutureScaleY + "px";

		var context = picture.getContext("2d");
		context.drawImage(finder,
			cropVideoX, cropVideoY, cropVideoWidth, cropVideoHeight,
			pictureX, pictureY, pictureWidth, pictureHeight);

		var image = picture.toDataURL(defaults.mimeType);

		takeCallback({
			data: image
		});
	}

	this.pause = function() {

		if (finder) {

			finder.pause();
		}
	}

	this.stop = function() {

		if (track) {

			track.stop();
		}
	}
}
