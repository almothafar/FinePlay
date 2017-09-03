'use strict';

var cropper = new Cropper($('#image')[0], {

	aspectRatio: 16 / 9,
	crop: function(e) {

		console.dir(e);
	}
});
